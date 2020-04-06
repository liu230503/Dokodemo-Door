package org.alee.dokodemo.door.core;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: {@link Fragment} 事务处理器
 *
 *********************************************************/
interface IFragmentTransaction {
    /**
     * 根据tag 查找一个{@link Fragment}
     *
     * @param tag tag
     * @return {@link Fragment}
     */
    Fragment find(String tag);

    /**
     * 添加一个{@link Fragment}
     *
     * @param containerViewId 容器id
     * @param fragment        {@link Fragment}
     * @param tag             tag
     * @return {@link IFragmentTransaction}
     */
    IFragmentTransaction add(@IdRes int containerViewId, Fragment fragment, @NonNull String tag);

    /**
     * 移除指定节点
     *
     * @param tags 要移除的节点 tag
     * @return {@link IFragmentTransaction}
     */
    IFragmentTransaction remove(String... tags);

    /**
     * 移除当前容器内所有节点
     *
     * @return {@link IFragmentTransaction}
     */
    IFragmentTransaction removeAll();

    /**
     * 显示以前隐藏的节点
     *
     * @param tags 要显示的节点 tag
     * @return {@link IFragmentTransaction}
     */
    IFragmentTransaction show(String... tags);

    /**
     * 隐藏已经存在的节点
     *
     * @param tags 要隐藏的节点的tag
     * @return {@link IFragmentTransaction}
     */
    IFragmentTransaction hide(String... tags);

    /**
     * 提交处理操作
     */
    void commit();

    /**
     * 设置自定义转场动画
     *
     * @param enter 入场动画 id
     * @param exit  出场动画 id
     */
    void setCustomAnimations(@AnimRes int enter, @AnimRes int exit);

    /**
     * 返回当前节点的是否还有剩余未处理事务
     *
     * @return 结果
     */
    boolean isEmpty();
}
