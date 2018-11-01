package com.me94me.example_navigatioin_resource.navigation;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ActivityNavigator implements cross-activity navigation.
 */
@Navigator.Name("activity")
public class ActivityNavigator extends Navigator<ActivityNavigator.Destination> {
    private static final String EXTRA_NAV_SOURCE =
            "android-support-navigation:ActivityNavigator:source";
    private static final String EXTRA_NAV_CURRENT =
            "android-support-navigation:ActivityNavigator:current";

    private Context mContext;
    private Activity mHostActivity;

    public ActivityNavigator(Context context) {
        mContext = context;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                mHostActivity = (Activity) context;
                break;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
    }


    Context getContext() {
        return mContext;
    }

    @Override
    public Destination createDestination() {
        return new Destination(this);
    }

    @Override
    public boolean popBackStack() {
        if (mHostActivity != null) {
            int destId = 0;
            final Intent intent = mHostActivity.getIntent();
            if (intent != null) {
                destId = intent.getIntExtra(EXTRA_NAV_SOURCE, 0);
            }
            mHostActivity.finish();
            dispatchOnNavigatorNavigated(destId, BACK_STACK_DESTINATION_POPPED);
            return true;
        }
        return false;
    }

    /**
     * 执行导航动作
     */
    @Override
    public void navigate(Destination destination, Bundle args,NavOptions navOptions) {
        if (destination.getIntent() == null) {
            throw new IllegalStateException("Destination " + destination.getId()
                    + " does not have an Intent set.");
        }
        //构造新的Intent
        Intent intent = new Intent(destination.getIntent());
        if (args != null) {
            intent.putExtras(args);
            //动态数据模式
            String dataPattern = destination.getDataPattern();
            if (!TextUtils.isEmpty(dataPattern)) {
                // 使用args填充数据模式以构建有效的URI
                StringBuffer data = new StringBuffer();
                Pattern fillInPattern = Pattern.compile("\\{(.+?)\\}");
                Matcher matcher = fillInPattern.matcher(dataPattern);
                while (matcher.find()) {
                    String argName = matcher.group(1);
                    if (args.containsKey(argName)) {
                        matcher.appendReplacement(data, "");
                        data.append(Uri.encode(args.getString(argName)));
                    } else {
                        throw new IllegalArgumentException("Could not find " + argName + " in "
                                + args + " to fill data pattern " + dataPattern);
                    }
                }
                matcher.appendTail(data);
                intent.setData(Uri.parse(data.toString()));
            }
        }
        if (navOptions != null && navOptions.shouldClearTask()) {
            //最终转换成了FLAG_ACTIVITY_CLEAR_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        if (navOptions != null && navOptions.shouldLaunchDocument()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else if (!(mContext instanceof Activity)) {
            // 如果我们没有从Activity上下文启动，我们必须在新任务中启动。
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (navOptions != null && navOptions.shouldLaunchSingleTop()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        if (mHostActivity != null) {
            final Intent hostIntent = mHostActivity.getIntent();
            if (hostIntent != null) {
                final int hostCurrentId = hostIntent.getIntExtra(EXTRA_NAV_CURRENT, 0);
                if (hostCurrentId != 0) {
                    intent.putExtra(EXTRA_NAV_SOURCE, hostCurrentId);
                }
            }
        }
        final int destId = destination.getId();
        //将该目的地设置当前
        intent.putExtra(EXTRA_NAV_CURRENT, destId);
        NavOptions.addPopAnimationsToIntent(intent, navOptions);
        //依然调用了startActivity
        mContext.startActivity(intent);
        if (navOptions != null && mHostActivity != null) {
            int enterAnim = navOptions.getEnterAnim();
            int exitAnim = navOptions.getExitAnim();
            if (enterAnim != -1 || exitAnim != -1) {
                enterAnim = enterAnim != -1 ? enterAnim : 0;
                exitAnim = exitAnim != -1 ? exitAnim : 0;
                //执行了activity的转场动画
                mHostActivity.overridePendingTransition(enterAnim, exitAnim);
            }
        }

        // 您无法从新Activity的调用者弹出后台堆栈，因此我们不会将此导航器添加到控制器的后台堆栈中
        dispatchOnNavigatorNavigated(destId, BACK_STACK_UNCHANGED);
    }

    /**
     * NavDestination for activity navigation
     */
    public static class Destination extends NavDestination {
        private Intent mIntent;
        private String mDataPattern;

        /**
         * Construct a new activity destination. This destination is not valid until you set the
         * Intent via {@link #setIntent(Intent)} or one or more of the other set method.
         *
         *
         * @param navigatorProvider The {@link NavController} which this destination
         *                          will be associated with.
         */
        public Destination(NavigatorProvider navigatorProvider) {
            this(navigatorProvider.getNavigator(ActivityNavigator.class));
        }

        /**
         * Construct a new activity destination. This destination is not valid until you set the
         * Intent via {@link #setIntent(Intent)} or one or more of the other set method.
         *
         * @param activityNavigator The {@link ActivityNavigator} which this destination
         *                          will be associated with. Generally retrieved via a
         *                          {@link NavController}'s
         *                          {@link NavigatorProvider#getNavigator(Class)} method.
         */
        public Destination(Navigator<? extends Destination> activityNavigator) {
            super(activityNavigator);
        }

        @Override
        public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs) {
            super.onInflate(context, attrs);
            TypedArray a = context.getResources().obtainAttributes(attrs,
                    R.styleable.ActivityNavigator);
            String cls = a.getString(R.styleable.ActivityNavigator_android_name);
            if (!TextUtils.isEmpty(cls)) {
                // TODO Replace with ComponentName.createRelative() when minSdkVersion is 23
                if (cls.charAt(0) == '.') {
                    cls = context.getPackageName() + cls;
                }
                setComponentName(new ComponentName(context, cls));
            }
            setAction(a.getString(R.styleable.ActivityNavigator_action));
            String data = a.getString(R.styleable.ActivityNavigator_data);
            if (data != null) {
                setData(Uri.parse(data));
            }
            setDataPattern(a.getString(R.styleable.ActivityNavigator_dataPattern));
            a.recycle();
        }

        /**
         * 将导航设置为在导航到此目标时启动。
         * @param intent Intent to associated with this destination.
         * @return this {@link Destination}
         */
        public Destination setIntent(Intent intent) {
            mIntent = intent;
            return this;
        }

        /**
         * 获取与此目标关联的Intent。
         * @return
         */
        public Intent getIntent() {
            return mIntent;
        }

        /**
         * 设置显式{@link ComponentName}以导航到。
         *
         * @param name The component name of the Activity to start.
         * @return this {@link Destination}
         */
        public Destination setComponentName(ComponentName name) {
            if (mIntent == null) {
                mIntent = new Intent();
            }
            mIntent.setComponent(name);
            return this;
        }

        /**
         * 获取与此目标关联的显式{@link ComponentName}（如果有）
         * @return
         */
        public ComponentName getComponent() {
            if (mIntent == null) {
                return null;
            }
            return mIntent.getComponent();
        }

        /**
         * 设置导航到此目标时发送的操作。
         * @param action The action string to use.
         * @return this {@link Destination}
         */
        public Destination setAction(String action) {
            if (mIntent == null) {
                mIntent = new Intent();
            }
            mIntent.setAction(action);
            return this;
        }

        /**
         * 获取用于启动Activity的Action（如果有）
         */
        public String getAction() {
            if (mIntent == null) {
                return null;
            }
            return mIntent.getAction();
        }

        /**
         * 设置导航到此目标时发送的静态数据URI。
         *
         * 要使用基于导航时传入的参数而更改的动态URI，请使用{@link #setDataPattern（String）}，这将在存在参数时优先
         * @param data A static URI that should always be used.
         * @see #setDataPattern(String)
         * @return this {@link Destination}
         */
        public Destination setData(Uri data) {
            if (mIntent == null) {
                mIntent = new Intent();
            }
            mIntent.setData(data);
            return this;
        }

        /**
         * 获取用于启动Activity的数据URI（如果有）
         */
        public Uri getData() {
            if (mIntent == null) {
                return null;
            }
            return mIntent.getData();
        }

        /**
         * 设置导航到此目标时发送的动态数据URI模式。
         *
         * 如果在导航时存在非空参数Bundle，则<code> {argName} </ code>形式的任何段都将替换为参数中的URI编码字符串
         * @param dataPattern 一个URI模式，其中包含<code> {argName} </ code>形式的段，它们将被参数Bundle中的字符串的URI编码版本替换。
         * @see #setData
         * @return this {@link Destination}
         */
        public Destination setDataPattern(String dataPattern) {
            mDataPattern = dataPattern;
            return this;
        }

        /**
         * 获取动态数据URI模式（如果有）
         */
        public String getDataPattern() {
            return mDataPattern;
        }
    }
}
