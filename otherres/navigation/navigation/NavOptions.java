package com.me94me.example_navigatioin_resource.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.IdRes;

/**
 * 导航操作的特殊选项
 */
public class NavOptions {
    //启动模式
    //SingleTop
    static final int LAUNCH_SINGLE_TOP = 0x1;
    //Document
    static final int LAUNCH_DOCUMENT = 0x2;
    //ClearTask
    static final int LAUNCH_CLEAR_TASK = 0x4;

    //NavOptions
    private static final String KEY_NAV_OPTIONS = "android-support-nav:navOptions";
    //启动模式
    private static final String KEY_LAUNCH_MODE = "launchMode";
    //退出
    private static final String KEY_POP_UP_TO = "popUpTo";
    //是否从回退栈中弹出指定的目的地
    private static final String KEY_POP_UP_TO_INCLUSIVE = "popUpToInclusive";
    //进入动画
    private static final String KEY_ENTER_ANIM = "enterAnim";
    //退出动画
    private static final String KEY_EXIT_ANIM = "exitAnim";
    //入栈时动画操作
    private static final String KEY_POP_ENTER_ANIM = "popEnterAnim";
    //弹出栈时动画操作
    private static final String KEY_POP_EXIT_ANIM = "popExitAnim";

    /**
     * 将{@link #getPopEnterAnim（）pop enter}和{@link #getPopExitAnim（）pop exit}动画添加到Intent中，
     * 以便以后使用{@link #applyPopAnimationsToPendingTransition（Activity）}
     *
     * 这是{@link ActivityNavigator}自动调用的
     *
     * @param intent Intent being started with the given NavOptions
     * @param navOptions NavOptions containing the pop animations.
     * @see #applyPopAnimationsToPendingTransition(Activity)
     * @see #getPopEnterAnim()
     * @see #getPopExitAnim()
     */
    public static void addPopAnimationsToIntent(Intent intent,NavOptions navOptions) {
        if (navOptions != null) {
            intent.putExtra(KEY_NAV_OPTIONS, navOptions.toBundle());
        }
    }

    /**
     * 将应用给定Activity的Intent中的任何pop动画于{@link Activity＃overridePendingTransition（int，int）}
     * 这应该用于代替{@link Activity＃overridePendingTransition（int，int）}以获取适当的pop动画。
     * @param activity An activity started from the {@link ActivityNavigator}.
     * @see #addPopAnimationsToIntent(Intent, NavOptions)
     * @see #getPopEnterAnim()
     * @see #getPopExitAnim()
     */
    public static void applyPopAnimationsToPendingTransition(Activity activity) {
        Intent intent = activity.getIntent();
        if (intent == null) {
            return;
        }
        Bundle bundle = intent.getBundleExtra(KEY_NAV_OPTIONS);
        if (bundle != null) {
            NavOptions navOptions = NavOptions.fromBundle(bundle);
            int popEnterAnim = navOptions.getPopEnterAnim();
            int popExitAnim = navOptions.getPopExitAnim();
            if (popEnterAnim != -1 || popExitAnim != -1) {
                popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
                popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
                //调用了activity的过度动画
                activity.overridePendingTransition(popEnterAnim, popExitAnim);
            }
        }
    }

    private int mLaunchMode;
    @IdRes
    private int mPopUpTo;
    private boolean mPopUpToInclusive;
    @AnimRes
    @AnimatorRes
    private int mEnterAnim;
    @AnimRes
    @AnimatorRes
    private int mExitAnim;
    @AnimRes
    @AnimatorRes
    private int mPopEnterAnim;
    @AnimRes
    @AnimatorRes
    private int mPopExitAnim;

    NavOptions( int launchMode,
                @IdRes int popUpTo,
                boolean popUpToInclusive,
                @AnimRes @AnimatorRes int enterAnim,
                @AnimRes @AnimatorRes int exitAnim,
                @AnimRes @AnimatorRes int popEnterAnim,
                @AnimRes @AnimatorRes int popExitAnim) {
        mLaunchMode = launchMode;
        mPopUpTo = popUpTo;
        mPopUpToInclusive = popUpToInclusive;
        mEnterAnim = enterAnim;
        mExitAnim = exitAnim;
        mPopEnterAnim = popEnterAnim;
        mPopExitAnim = popExitAnim;
    }


    public boolean shouldLaunchSingleTop() {
        return (mLaunchMode & LAUNCH_SINGLE_TOP) != 0;
    }
    public boolean shouldLaunchDocument() {
        return (mLaunchMode & LAUNCH_DOCUMENT) != 0;
    }
    public boolean shouldClearTask() {
        return (mLaunchMode & LAUNCH_CLEAR_TASK) != 0;
    }
    @IdRes
    public int getPopUpTo() {
        return mPopUpTo;
    }
    public boolean isPopUpToInclusive() {
        return mPopUpToInclusive;
    }
    @AnimRes @AnimatorRes
    public int getEnterAnim() {
        return mEnterAnim;
    }
    @AnimRes @AnimatorRes
    public int getExitAnim() {
        return mExitAnim;
    }
    @AnimRes @AnimatorRes
    public int getPopEnterAnim() {
        return mPopEnterAnim;
    }
    @AnimRes @AnimatorRes
    public int getPopExitAnim() {
        return mPopExitAnim;
    }


    /**
     * NavOptions转成Bundle
     */
    private Bundle toBundle() {
        Bundle b = new Bundle();
        b.putInt(KEY_LAUNCH_MODE, mLaunchMode);
        b.putInt(KEY_POP_UP_TO, mPopUpTo);
        b.putBoolean(KEY_POP_UP_TO_INCLUSIVE, mPopUpToInclusive);
        b.putInt(KEY_ENTER_ANIM, mEnterAnim);
        b.putInt(KEY_EXIT_ANIM, mExitAnim);
        b.putInt(KEY_POP_ENTER_ANIM, mPopEnterAnim);
        b.putInt(KEY_POP_EXIT_ANIM, mPopExitAnim);
        return b;
    }
    /**
     * 从Bundle中获取NavOptions
     */
    private static NavOptions fromBundle(Bundle b) {
        return new NavOptions(b.getInt(KEY_LAUNCH_MODE, 0),
                b.getInt(KEY_POP_UP_TO, 0), b.getBoolean(KEY_POP_UP_TO_INCLUSIVE, false),
                b.getInt(KEY_ENTER_ANIM, -1), b.getInt(KEY_EXIT_ANIM, -1),
                b.getInt(KEY_POP_ENTER_ANIM, -1), b.getInt(KEY_POP_EXIT_ANIM, -1));
    }

    /**
     * Builder用于构造NavOptions实例
     */
    public static class Builder {
        int mLaunchMode;
        int mPopUpTo;
        boolean mPopUpToInclusive;
        @AnimRes @AnimatorRes
        int mEnterAnim = -1;
        @AnimRes @AnimatorRes
        int mExitAnim = -1;
        @AnimRes @AnimatorRes
        int mPopEnterAnim = -1;
        @AnimRes @AnimatorRes
        int mPopExitAnim = -1;

        public Builder() { }

        /**
         * 如果要在同一目标的实例（例如，关于类似数据项的详细信息页面）之间进行横向导航而不应保留历史记录，则将导航目标启动为单顶。
         * @param singleTop true作为singleTop
         */
        public Builder setLaunchSingleTop(boolean singleTop) {
            if (singleTop) {
                mLaunchMode |= LAUNCH_SINGLE_TOP;
            } else {
                mLaunchMode &= ~LAUNCH_SINGLE_TOP;
            }
            return this;
        }

        /**
         * 如果希望在系统“概述”屏幕中将导航目标显示为其自己的条目，请将其作为文档启动。
         * 如果多次启动同一文档，则不会创建新任务，它会将现有文档任务放在前面。
         *
         * 如果用户从新文档任务中按下系统返回键，他们将登陆他们之前的任务。
         * 如果用户从系统概述屏幕到达文档任务，则它们将被带到其主屏幕。
         *
         * @param launchDocument true启动一个新文档任务
         */
        public Builder setLaunchDocument(boolean launchDocument) {
            if (launchDocument) {
                mLaunchMode |= LAUNCH_DOCUMENT;
            } else {
                mLaunchMode &= ~LAUNCH_DOCUMENT;
            }
            return this;
        }
        /**
         * 在启动此目标之前清除整个任务。
         * 如果要作为{@link #setLaunchDocument（boolean）document}启动，这将清除文档任务。否则它将清除当前任务。
         */
        public Builder setClearTask(boolean clearTask) {
            if (clearTask) {
                mLaunchMode |= LAUNCH_CLEAR_TASK;
            } else {
                mLaunchMode &= ~LAUNCH_CLEAR_TASK;
            }
            return this;
        }

        /**
         * 在导航之前弹出到指定目的地。 这将从后向堆栈弹出所有不匹配的目标，直到找到此目标。
         *
         * @param destinationId 弹出的目的地，清除所有干预目的地。
         * @param inclusive true也会从回退栈中弹出指定的目的地
         * @return this Builder
         * @see NavOptions#getPopUpTo
         * @see NavOptions#isPopUpToInclusive
         */
        public Builder setPopUpTo(int destinationId, boolean inclusive) {
            mPopUpTo = destinationId;
            mPopUpToInclusive = inclusive;
            return this;
        }

        /**
         * 为进入动画设置动画资源
         * 注意:动画资源不支持导航到一个新的activity
         * @param enterAnim Custom animation to run
         * @return this Builder
         * @see NavOptions#getEnterAnim()
         */
        public Builder setEnterAnim(@AnimRes @AnimatorRes int enterAnim) {
            mEnterAnim = enterAnim;
            return this;
        }

        /**
         * 为退出动画设置动画资源
         * 注意:动画资源不支持导航到一个新的activity
         * @param exitAnim Custom animation to run
         * @return this Builder
         * @see NavOptions#getExitAnim()
         */
        public Builder setExitAnim(@AnimRes @AnimatorRes int exitAnim) {
            mExitAnim = exitAnim;
            return this;
        }

        /**
         * 弹出后台堆栈时，为输入动画设置自定义动画或动画制作器资源。
         * 注意:动画资源不支持导航到一个新的activity
         * @param popEnterAnim Custom animation to run
         * @return this Builder
         * @see NavOptions#getPopEnterAnim()
         */
        public Builder setPopEnterAnim(@AnimRes @AnimatorRes int popEnterAnim) {
            mPopEnterAnim = popEnterAnim;
            return this;
        }

        /**
         * 弹出后台堆栈时，为退出动画设置自定义动画或动画制作器资源。
         * 注意:动画资源不支持导航到一个新的activity
         * @param popExitAnim Custom animation to run
         * @return this Builder
         * @see NavOptions#getPopExitAnim()
         */
        public Builder setPopExitAnim(@AnimRes @AnimatorRes int popExitAnim) {
            mPopExitAnim = popExitAnim;
            return this;
        }

        /**
         * @return 构造一个NavOptions
         */
        public NavOptions build() {
            return new NavOptions(mLaunchMode, mPopUpTo, mPopUpToInclusive,
                    mEnterAnim, mExitAnim, mPopEnterAnim, mPopExitAnim);
        }
    }
}
