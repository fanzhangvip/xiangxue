package com.me94me.example_navigatioin_resource.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import androidx.collection.SparseArrayCompat;

/**
 * NavGraph是一个通过Id获取{@link NavDestination}节点的集合
 *
 * NavGraph充当“虚拟”目的地
 * 虽然NavGraph本身不会出现在后台堆栈上，但导航到该NavGraph将导致{@link #getStartDestination }起始目的地被添加到后台堆栈。
 */
public class NavGraph extends NavDestination implements Iterable<NavDestination> {
    //所有节点
    private final SparseArrayCompat<NavDestination> mNodes = new SparseArrayCompat<>();
    //起点节点
    private int mStartDestId;

    /**
     * 构造一个NavGraph
     * 只有NavGraph被调用了{@link #addDestination(NavDestination)}
     * 并且设置了starting destination 才合法
     *
     * @param navigatorProvider NavGraph关联的{@link NavController}
     */
    public NavGraph(NavigatorProvider navigatorProvider) {
        this(navigatorProvider.getNavigator(NavGraphNavigator.class));
    }

    /**
     * Construct a new NavGraph. This NavGraph is not valid until you
     * {@link #addDestination(NavDestination) add a destination} and
     * {@link #setStartDestination(int) set the starting destination}.
     *
     * @param navGraphNavigator The {@link NavGraphNavigator} which this destination
     *                          will be associated with. Generally retrieved via a
     *                          {@link NavController}'s
     *                          {@link NavigatorProvider#getNavigator(Class)} method.
     */
    public NavGraph( Navigator<? extends NavGraph> navGraphNavigator) {
        super(navGraphNavigator);
    }

    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs) {
        super.onInflate(context, attrs);
        TypedArray a = context.getResources().obtainAttributes(attrs,
                R.styleable.NavGraphNavigator);
        setStartDestination(
                a.getResourceId(R.styleable.NavGraphNavigator_startDestination, 0));
        a.recycle();
    }

    @Override
    @Nullable
    Pair<NavDestination, Bundle> matchDeepLink(@NonNull Uri uri) {
        // First search through any deep links directly added to this NavGraph
        Pair<NavDestination, Bundle> result = super.matchDeepLink(uri);
        if (result != null) {
            return result;
        }
        // Then search through all child destinations for a matching deep link
        for (NavDestination child : this) {
            Pair<NavDestination, Bundle> childResult = child.matchDeepLink(uri);
            if (childResult != null) {
                return childResult;
            }
        }
        return null;
    }


    /**
     * 添加节点到NavGraph
     * Adds a destination to this NavGraph. The destination must have an
     * {@link NavDestination#getId()} id} set.
     *
     * <p>The destination must not have a {@link NavDestination#getParent() parent} set. If
     * the destination is already part of a {@link NavGraph navigation graph}, call
     * {@link #remove(NavDestination)} before calling this method.</p>
     *
     * @param node destination to add
     */
    public void addDestination(NavDestination node) {
        if (node.getId() == 0) {
            throw new IllegalArgumentException("Destinations must have an id."
                    + " Call setId() or include an android:id in your navigation XML.");
        }
        NavDestination existingDestination = mNodes.get(node.getId());
        if (existingDestination == node) {
            return;
        }
        if (node.getParent() != null) {
            throw new IllegalStateException("Destination already has a parent set."
                    + " Call NavGraph.remove() to remove the previous parent.");
        }
        if (existingDestination != null) {
            existingDestination.setParent(null);
        }
        node.setParent(this);
        mNodes.put(node.getId(), node);
    }

    /**
     * Adds multiple destinations to this NavGraph. Each destination must have an
     * {@link NavDestination#getId()} id} set.
     *
     * <p> Each destination must not have a {@link NavDestination#getParent() parent} set. If
     * any destination is already part of a {@link NavGraph navigation graph}, call
     * {@link #remove(NavDestination)} before calling this method.</p>
     *
     * @param nodes destinations to add
     */
    public void addDestinations(@NonNull Collection<NavDestination> nodes) {
        for (NavDestination node : nodes) {
            if (node == null) {
                continue;
            }
            addDestination(node);
        }
    }

    /**
     * Adds multiple destinations to this NavGraph. Each destination must have an
     * {@link NavDestination#getId()} id} set.
     *
     * <p> Each destination must not have a {@link NavDestination#getParent() parent} set. If
     * any destination is already part of a {@link NavGraph navigation graph}, call
     * {@link #remove(NavDestination)} before calling this method.</p>
     *
     * @param nodes destinations to add
     */
    public void addDestinations(NavDestination... nodes) {
        for (NavDestination node : nodes) {
            if (node == null) {
                continue;
            }
            addDestination(node);
        }
    }

    /**
     * 在Graph通过resid找到NavDestination
     *
     * @param resid ID to locate
     * @return NavDestination节点
     */
    public NavDestination findNode(int resid) {
        return findNode(resid, true);
    }

    /**
     * 寻找NavDestination
     * @param resid 节点Id
     * @param searchParents 是否查找父级Graph
     * @return NavDestination
     */
    NavDestination findNode(int resid, boolean searchParents) {
        //从当前Graph的所有节点中获取
        NavDestination destination = mNodes.get(resid);
        //为null就从父级Graph从查找未找到返回null
        return destination != null
                ? destination
                : searchParents && getParent() != null ? getParent().findNode(resid) : null;
    }

    @Override
    public Iterator<NavDestination> iterator() {
        return new Iterator<NavDestination>() {
            private int mIndex = -1;
            private boolean mWentToNext = false;

            @Override
            public boolean hasNext() {
                return mIndex + 1 < mNodes.size();
            }

            @Override
            public NavDestination next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                mWentToNext = true;
                return mNodes.valueAt(++mIndex);
            }

            @Override
            public void remove() {
                if (!mWentToNext) {
                    throw new IllegalStateException(
                            "You must call next() before you can remove an element");
                }
                mNodes.valueAt(mIndex).setParent(null);
                mNodes.removeAt(mIndex);
                mIndex--;
                mWentToNext = false;
            }
        };
    }

    /**
     * Add all destinations from another collection to this one. As each destination has at most
     * one parent, the destinations will be removed from the given NavGraph.
     *
     * @param other collection of destinations to add. All destinations will be removed from this
     * graph after being added to this graph.
     */
    public void addAll(@NonNull NavGraph other) {
        Iterator<NavDestination> iterator = other.iterator();
        while (iterator.hasNext()) {
            NavDestination destination = iterator.next();
            iterator.remove();
            addDestination(destination);
        }
    }

    /**
     * 移除某个给定NavGraph的destination
     *
     * @param node the destination to remove.
     */
    public void remove(NavDestination node) {
        int index = mNodes.indexOfKey(node.getId());
        if (index >= 0) {
            mNodes.valueAt(index).setParent(null);
            mNodes.removeAt(index);
        }
    }

    /**
     * 清空graph的所有节点
     */
    public void clear() {
        Iterator<NavDestination> iterator = iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    /**
     * 返回Graph的起点，当导航到该Graph时该起点会被初始化显示
     * @return 起点的Id
     */
    public int getStartDestination() {
        return mStartDestId;
    }

    /**
     * 设置NavGraph的起点
     *
     * @param startDestId 当导航到该Graph时的DestinationId
     */
    public void setStartDestination(int startDestId) {
        mStartDestId = startDestId;
    }
}
