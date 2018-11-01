package com.me94me.example_navigatioin_resource.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;

import java.util.ArrayDeque;
import java.util.ArrayList;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;


/**
 * NavDestination表示整体导航图中的一个节点。
 *
 * 每个目的地都与{@link Navigator}相关联，后者知道如何导航到此特定目的地
 *
 * 目标声明它们支持的一组{@link #putAction（int，int）actions}
 * 这些操作形成目的地的导航API
 *
 * 每个目标都有一组{@link #getDefaultArguments（）默认参数}，当{@link NavController＃navigate（int，Bundle）导航}到该目的地时将应用这些参数
 * 这些参数可以在导航时被覆盖
 */
public class NavDestination {

    /**
     * 检索给定id的合适名称
     * @param context 用于获取资源的Context
     * @param id 用与获取名称的id
     * @return 合法的id返回资源名不合法的id返回id本身
     */
    static String getDisplayName(Context context, int id) {
        try {
            return context.getResources().getResourceName(id);
        } catch (Resources.NotFoundException e) {
            return Integer.toString(id);
        }
    }
    //导航器
    private final Navigator mNavigator;
    //包含该目的地的NavGraph
    private NavGraph mParent;
    //id
    private int mId;
    //描述标签
    private CharSequence mLabel;
    //默认参数
    private Bundle mDefaultArgs;
    private ArrayList<NavDeepLink> mDeepLinks;
    //缓存的NavAction
    private SparseArrayCompat<NavAction> mActions;

    /**
     * 创建NavDestinations应该通过{@link Navigator#createDestination}.
     */
    public NavDestination(@NonNull Navigator<? extends NavDestination> navigator) {
        mNavigator = navigator;
    }
    /**
     * 在{@link NavInflater#inflate(Resources, XmlResourceParser, AttributeSet)}中调用
     * 当从资源文件加载目的地时调用
     *
     * @param context 上下文
     * @param attrs 加载过程中的属性
     */
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs) {
        final TypedArray a = context.getResources().obtainAttributes(attrs,
                R.styleable.Navigator);
        //id
        setId(a.getResourceId(R.styleable.Navigator_android_id, 0));
        //label
        setLabel(a.getText(R.styleable.Navigator_android_label));
        a.recycle();
    }
    /**
     * 当一个目的地被加入NavGraph时通过{@link NavGraph#addDestination}被设置
     * @param parent
     */
    void setParent(NavGraph parent) {
        mParent = parent;
    }
    /**
     * 获取包含该目的地的的{@link NavGraph}
     * @return
     */
    public NavGraph getParent() {
        return mParent;
    }

    /**
     * 返回目的地的唯一ID
     * 该id被android资源系统自动生成
     * @return this destination's ID
     */
    public int getId() {
        return mId;
    }

    /**
     * 设置目的地的唯一id
     * @param id this destination's new ID
     */
    public void setId(@IdRes int id) {
        mId = id;
    }
    /**
     * 设置描述的标签
     */
    public void setLabel(@Nullable CharSequence label) {
        mLabel = label;
    }

    /**
     * 获取该目的地的描述性标签
     */
    @Nullable
    public CharSequence getLabel() {
        return mLabel;
    }

    /**
     * 返回该目的地的{@link Navigator}.
     */
    @NonNull
    public Navigator getNavigator() {
        return mNavigator;
    }


    /**
     * 返回该目的地的默认参数bundle
     */
    public @NonNull Bundle getDefaultArguments() {
        if (mDefaultArgs == null) {
            mDefaultArgs = new Bundle();
        }
        return mDefaultArgs;
    }
    /**
     * 设置该目的地的默认参数
     */
    public void setDefaultArguments(@Nullable Bundle args) {
        mDefaultArgs = args;
    }
    /**
     * 将一组参数合并到此目标的当前默认参数中。具有相同键的新值将使用这些键替换旧值。
     */
    public void addDefaultArguments(@NonNull Bundle args) {
        getDefaultArguments().putAll(args);
    }




    /**
     * Add a deep link to this destination. Matching Uris sent to
     * {@link NavController#onHandleDeepLink(Intent)} will trigger navigating to this destination.
     * <p>
     * In addition to a direct Uri match, the following features are supported:
     * <ul>
     *     <li>Uris without a scheme are assumed as http and https. For example,
     *     <code>www.example.com</code> will match <code>http://www.example.com</code> and
     *     <code>https://www.example.com</code>.</li>
     *     <li>Placeholders in the form of <code>{placeholder_name}</code> matches 1 or more
     *     characters. The String value of the placeholder will be available in the arguments
     *     {@link Bundle} with a key of the same name. For example,
     *     <code>http://www.example.com/users/{id}</code> will match
     *     <code>http://www.example.com/users/4</code>.</li>
     *     <li>The <code>.*</code> wildcard can be used to match 0 or more characters.</li>
     * </ul>
     * These Uris can be declared in your navigation XML files by adding one or more
     * <code>&lt;deepLink app:uri="uriPattern" /&gt;</code> elements as
     * a child to your destination.
     * <p>
     * Deep links added in navigation XML files will automatically replace instances of
     * <code>${applicationId}</code> with the applicationId of your app.
     * Programmatically added deep links should use {@link Context#getPackageName()} directly
     * when constructing the uriPattern.
     * @param uriPattern The uri pattern to add as a deep link
     * @see NavController#onHandleDeepLink(Intent)
     */
    public void addDeepLink(@NonNull String uriPattern) {
        if (mDeepLinks == null) {
            mDeepLinks = new ArrayList<>();
        }
        mDeepLinks.add(new NavDeepLink(uriPattern));
    }

    /**
     * Determines if this NavDestination has a deep link matching the given Uri.
     * @param uri The Uri to match against all deep links added in {@link #addDeepLink(String)}
     * @return The matching {@link NavDestination} and the appropriate {@link Bundle} of arguments
     * extracted from the Uri, or null if no match was found.
     */
    @Nullable
    Pair<NavDestination, Bundle> matchDeepLink(@NonNull Uri uri) {
        if (mDeepLinks == null) {
            return null;
        }
        for (NavDeepLink deepLink : mDeepLinks) {
            Bundle matchingArguments = deepLink.getMatchingArguments(uri);
            if (matchingArguments != null) {
                return Pair.create(this, matchingArguments);
            }
        }
        return null;
    }

    /**
     * Build an array containing the hierarchy from the root down to this destination.
     *
     * @return An array containing all of the ids from the root to this destination
     */
    @NonNull
    int[] buildDeepLinkIds() {
        ArrayDeque<NavDestination> hierarchy = new ArrayDeque<>();
        hierarchy.add(this);
        while (hierarchy.peekFirst().getParent() != null) {
            hierarchy.addFirst(hierarchy.peekFirst().getParent());
        }
        int[] deepLinkIds = new int[hierarchy.size()];
        int index = 0;
        for (NavDestination destination : hierarchy) {
            deepLinkIds[index++] = destination.getId();
        }
        return deepLinkIds;
    }

    /**
     * 返回该目的地的ActionId
     * 如果在此目的地找不到，则将以递归方式检查此目标的{@link #getParent（）parent}。
     * @param id action ID to fetch
     * @return 与id相匹配的NavAction或者为空
     */
    @Nullable
    public NavAction getAction(@IdRes int id) {
        NavAction destination = mActions == null ? null : mActions.get(id);
        //如果为空从parent中查找
        return destination != null
                ? destination

                : getParent() != null ? getParent().getAction(id) : null;
    }
    /**
     * 为目的地设置Action
     * @param actionId action ID to bind
     * @param destId destination ID for the given action
     */
    public void putAction(@IdRes int actionId, @IdRes int destId) {
        putAction(actionId, new NavAction(destId));
    }
    public void putAction(@IdRes int actionId, @NonNull NavAction action) {
        if (actionId == 0) {
            throw new IllegalArgumentException("Cannot have an action with actionId 0");
        }
        if (mActions == null) {
            mActions = new SparseArrayCompat<>();
        }
        mActions.put(actionId, action);
    }
    /**
     * 移除Action
     * @param actionId action ID to remove
     */
    public void removeAction(@IdRes int actionId) {
        if (mActions == null) {
            return;
        }
        mActions.delete(actionId);
    }

    /**
     * 导航到目的地
     *
     * 使用{@link #getNavigator（）配置的navigator}导航到此destination.
     * Apps不应该直接调用它，而是使用{@link NavController}的导航方法来确保一致的后栈跟踪和行为。
     *
     * @param args arguments to the new destination
     * @param navOptions options for navigation
     */
    @SuppressWarnings("unchecked")
    public void navigate(@Nullable Bundle args, @Nullable NavOptions navOptions) {
        Bundle defaultArgs = getDefaultArguments();
        Bundle finalArgs = new Bundle();
        //合并参数
        finalArgs.putAll(defaultArgs);
        if (args != null) {
            finalArgs.putAll(args);
        }
        mNavigator.navigate(this, finalArgs, navOptions);
    }
}
