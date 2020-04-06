package org.alee.dokodemo.door.core;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: {@link Fragment}栈 管理器
 *
 *********************************************************/
final class FragmentStackManager implements Cloneable, Serializable {
    /**
     * key  {@link Fragment} 栈
     */
    private static final String BUNDLE_KEY_FRAGMENT_STACK = "key_fragment_stack";

    /**
     * key {@link Fragment} tag
     * value 宿主的containerViewId
     */
    private final HashMap<String, Integer> mFragmentContainerMap;

    /**
     * 存储 已经添加到宿主中的{@link Fragment} 的 tag
     */
    private final Stack<String> mFragmentTagStack;

    public FragmentStackManager() {
        mFragmentContainerMap = new HashMap<>();
        mFragmentTagStack = new Stack<>();
    }


    public Stack<String> getFragmentTagStack() {
        return mFragmentTagStack;
    }

    /**
     * 恢复{@link Fragment} 栈管理器
     *
     * @param savedInstanceState {@link Bundle} 宿主恢复时提供
     * @return {@link  FragmentStackManager}
     */
    public static FragmentStackManager restoreStack(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            return null;
        }
        return (FragmentStackManager) savedInstanceState.getSerializable(BUNDLE_KEY_FRAGMENT_STACK);
    }

    /**
     * 在宿主被杀死之前调用，保存当前的{@link Fragment} 栈
     *
     * @param savedInstanceState {@link Bundle} 宿主被杀死前提供
     */
    public void saveInstanceState(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            return;
        }
        savedInstanceState.putSerializable(BUNDLE_KEY_FRAGMENT_STACK, this);
    }

    /**
     * 入栈（最后一个进入的在栈顶）
     *
     * @param tag             {@link Fragment} 的tag
     * @param containerViewId 宿主提供的加载{@link Fragment}的布局id
     * @return 是否入栈成功
     */
    public boolean push(String tag, @IdRes int containerViewId) {
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        if (mFragmentTagStack.contains(tag)) {
            return false;
        }
        mFragmentTagStack.push(tag);
        mFragmentContainerMap.put(tag, containerViewId);
        return true;
    }

    /**
     * 添加一个{@link Fragment} 到 mFragmentContainerMap中
     *
     * @param tag             {@link Fragment} 的tag
     * @param containerViewId 宿主提供的加载{@link Fragment}的布局id
     * @return 是否入栈成功
     */
    public boolean add(String tag, @IdRes int containerViewId) {
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        if (contain(tag)) {
            return false;
        }
        mFragmentContainerMap.put(tag, containerViewId);
        return true;
    }

    /**
     * 返回 {@link Fragment} 是否已经在 mFragmentContainerMap中
     *
     * @param tag {@link Fragment} 的tag
     * @return 结果
     */
    public boolean contain(String tag) {
        return (mFragmentContainerMap != null) && mFragmentContainerMap.containsKey(tag);
    }

    /**
     * 返回栈顶部的{@link Fragment} 的tag并将其删除。
     *
     * @return {@link Fragment} 的tag
     */
    public String pop() {
        if (mFragmentTagStack.isEmpty()) {
            return null;
        }
        String tag = mFragmentTagStack.pop();
        mFragmentContainerMap.remove(tag);
        return tag;
    }

    /**
     * 返回栈顶部的{@link Fragment} 的tag。
     *
     * @return {@link Fragment} 的tag
     */
    public String peek() {
        if (mFragmentTagStack.isEmpty()) {
            return null;
        }
        return mFragmentTagStack.peek();
    }

    /**
     * 返回装载 tag 所属{@link Fragment} 的容器 的布局id
     *
     * @param tag {@link Fragment} 的tag
     * @return 容器 的布局id
     */
    public int getContainer(String tag) {
        if (!contain(tag)) {
            return 0;
        }
        return mFragmentContainerMap.get(tag);
    }

    /**
     * 将指定的{@link Fragment} tag 从栈中移除
     *
     * @param tag {@link Fragment}的tag
     * @return 结果
     */
    public boolean remove(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        if (!contain(tag)) {
            return false;
        }
        mFragmentTagStack.remove(tag);
        mFragmentContainerMap.remove(tag);
        return true;
    }

    /**
     * 根据给定的容器布局id 移除{@link Fragment} tag
     *
     * @param containerViewId 容器 的布局id
     * @return 结果
     */
    public boolean remove(@IdRes int containerViewId) {
        Iterator<Map.Entry<String, Integer>> iterator = mFragmentContainerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            if (entry.getValue() == containerViewId) {
                mFragmentTagStack.remove(entry.getKey());
                iterator.remove();
            }
        }
        return true;
    }

    /**
     * 获取不在栈中的{@link Fragment}的tag
     *
     * @return 结果
     */
    @NonNull
    public String[] getFragmentsWithoutStack() {
        List<String> fragmentTags = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : mFragmentContainerMap.entrySet()) {
            if (mFragmentTagStack.contains(entry.getKey())) {
                continue;
            }
            fragmentTags.add(entry.getKey());
        }
        return fragmentTags.toArray(new String[fragmentTags.size()]);
    }

    /**
     * 根据指定的容器布局id 获取{@link Fragment}的tag
     *
     * @param containerViewId 容器 的布局id
     * @return 结果
     */
    @NonNull
    public String[] getFragmentTags(@IdRes int containerViewId) {
        List<String> fragmentTags = new ArrayList<>();
        for (Map.Entry<String, Integer> item : mFragmentContainerMap.entrySet()) {
            if (containerViewId == item.getValue()) {
                fragmentTags.add(item.getKey());
            }
        }
        return fragmentTags.toArray(new String[fragmentTags.size()]);
    }

    /**
     * 判断{@link Fragment} 是否已经入栈
     *
     * @param tag {@link Fragment}的tag
     * @return 结果
     */
    public boolean isFragmentIntoStack(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return false;
        }
        return mFragmentTagStack.contains(tag);
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        // TODO
    }

    /**
     * 清空栈
     */
    public void clear() {
        mFragmentTagStack.clear();
        mFragmentContainerMap.clear();
    }
}
