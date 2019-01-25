package com.enjoy.zero.viewpagerdemo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.List;

@SuppressLint("ValidFragment")
public class LazyFragmentDelegater extends FragmentDelegater {

    boolean isViewCreated = false;
    boolean isFirstVisible = true;
    boolean currentVisibleState = false;

    @SuppressLint("ValidFragment")
    public LazyFragmentDelegater(Fragment fragment) {
        super(fragment);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;

        if (!isHidden() && getUserVisibleHint()) {
            dispatchUserVisibleHint(true);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isViewCreated) {
            if (isVisibleToUser && !currentVisibleState) {
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint(false);
            }
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstVisible) {
            if (!isHidden() && !currentVisibleState && getUserVisibleHint()) {
                dispatchUserVisibleHint(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (currentVisibleState && getUserVisibleHint()) {
            dispatchUserVisibleHint(false);
        }
    }


    /**
     * 统一处理 显示隐藏
     *
     * @param visible
     */
    private void dispatchUserVisibleHint(boolean visible) {
        if (visible && isParentInvisible()) return;
        if (currentVisibleState == visible) {
            return;
        }
        currentVisibleState = visible;
        if (visible) {
            if (isFirstVisible) {
                isFirstVisible = false;
                if (mLazyLoad != null) {
                    mLazyLoad.onLazyLoad(true, true);
                }
            } else {
                if (mLazyLoad != null) {
                    mLazyLoad.onLazyLoad(true, false);
                }
            }
            dispatchChildVisibleState(true);
        } else {
            dispatchChildVisibleState(false);
            if (mLazyLoad != null) {
                mLazyLoad.onLazyLoad(false, false);
            }
        }
    }


    private boolean isParentInvisible() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof LazyFragmentDelegater) {
            LazyFragmentDelegater fragment = (LazyFragmentDelegater) parentFragment;
            return !fragment.isSupportVisible();
        } else {
            return false;
        }
    }

    private boolean isSupportVisible() {
        return currentVisibleState;
    }


    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment child : fragments) {
                if (child instanceof LazyFragmentDelegater && !child.isHidden() && child.getUserVisibleHint()) {
                    ((LazyFragmentDelegater) child).dispatchUserVisibleHint(visible);
                }
            }
        }
    }

    private LazyLoad mLazyLoad = null;

    public void setLazyLoad(LazyLoad lazyLoad) {
        this.mLazyLoad = lazyLoad;
    }

    public static interface LazyLoad {
        void onLazyLoad(boolean visible, boolean isFisrtVisiable);
    }


}
