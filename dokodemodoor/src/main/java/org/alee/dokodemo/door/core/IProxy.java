package org.alee.dokodemo.door.core;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import org.alee.dokodemo.door.annotation.EnumLoadModel;
import org.alee.dokodemo.door.annotation.LoadModel;
import org.alee.dokodemo.door.annotation.Node;

import java.util.Stack;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: 定义支持的操作
 *
 *********************************************************/
public interface IProxy {
    /**
     * 返回当前{@link Fragment} 的宿主
     *
     * @return 当前 {@link Fragment} 的宿主（Activity/Fragment）
     */
    @NonNull
    Object getHost();

    /**
     * 回退
     */
    void onBackPressed();

    /**
     * 根据 tag 获取 {@link Fragment}
     *
     * @param tag {@link Fragment}的 tag
     * @return {@link Fragment}
     */
    Fragment findFragmentByTag(String tag);

    /**
     * 添加 {@link Fragment} 到 宿主中 但这些{@link Fragment} 不会被加到栈中
     * 调用此方法后可以针对这些{@link Fragment}进行{@link #showFragment}或{@link #replaceFragment}
     *
     * @param containerViewId 宿主加载{@link Fragment} 的布局id 如果<=0 则会从从宿主向上寻找新的宿主
     * @param fragments       要被添加的{@link Fragment}
     */
    void addFragment(@IdRes int containerViewId, Fragment... fragments);

    /**
     * 显示一个 {@link Fragment} 并隐藏该 {@link Fragment} 所在的宿主的布局中其他的 {@link Fragment}
     *
     * @param fragment 要显示的 {@link Fragment}
     */
    void startFragment(@NonNull Fragment fragment);

    /**
     * 添加并显示一个 {@link Fragment} ,该{@link Fragment} 可以调用{@link #setResult}返回一个结果给打开它的{@link Fragment}
     *
     * @param receive     接收结果的对象
     * @param fragment    要显示的{@link Fragment}
     * @param requestCode 如果> = 0，则当 {@link Fragment} 退出时，此cod将在onFragmentResult()方法中返回。
     */
    void startFragmentForResult(Object receive, @NonNull Fragment fragment, int requestCode);

    /**
     * 添加并显示一个 {@link Fragment}，当此{@link Fragment}关闭时 你会接到onFragmentResult()回调。
     * 可能调用此方法的 {@link Fragment}不会接收到onFragmentResult()回调，而在调用此此方法的节点的容器中调用onFragmentResult()
     *
     * @param fragment    要显示的{@link Fragment}
     * @param requestCode 如果> = 0，则当 {@link Fragment} 退出时，此cod将在onFragmentResult()方法中返回。
     */
    void startFragmentForResult(@NonNull Fragment fragment, int requestCode);

    /**
     * 显示堆栈中弹出的最后一个{@link Fragment}，并隐藏其他{@link Fragment}。
     */
    void startPopFragment();

    /**
     * 显示一个{@link Fragment}并隐藏其所属宿主中包含的其他{@link Fragment}。如果未在堆栈中添加该{@link Fragment}
     * 则先添加。
     *
     * @param fragment        {@link Fragment}
     * @param containerViewId 宿主要加载 {@link Fragment}的布局id
     */
    void showFragment(@NonNull Fragment fragment, @IdRes int containerViewId);

    /**
     * 显示一个{@link Fragment}并隐藏其所属宿主中包含的其他{@link Fragment}
     * 如果未在堆栈中添加该{@link Fragment}则先添加。
     * 该方法添加的{@link Fragment}不会被压入堆栈。如果调用方法{@link #onBackPressed()}，则该片段没有任何操作。
     *
     * @param fragment        {@link Fragment}
     * @param containerViewId 宿主要加载 {@link Fragment}的布局id
     * @param showRepeatAnim  如果{@link Fragment}已经显示，则触发以显示{@link Fragment}的动画,
     *                        {@link #showFragment(Fragment, int)}默认值为false。
     */
    void showFragment(@NonNull Fragment fragment, @IdRes int containerViewId, boolean showRepeatAnim);

    /**
     * 显示一个已经被添加的{@link Fragment}隐藏其所属宿主中包含的其他{@link Fragment}
     * 如果该片段已经显示，则什么也不做
     *
     * @param tag 要显示的 {@link Fragment}的tag
     */
    void showFragment(@NonNull String tag);

    /**
     * 显示一个已经被添加的{@link Fragment}隐藏其所属宿主中包含的其他{@link Fragment}
     *
     * @param tag            要显示的 {@link Fragment}的tag
     * @param showRepeatAnim 如果{@link Fragment}已经显示，则触发以显示{@link Fragment}的动画
     *                       {@link #showFragment(String)}默认值为false。
     */
    void showFragment(@NonNull String tag, boolean showRepeatAnim);

    /**
     * 隐藏一个已添加的 {@link Fragment}
     *
     * @param fragment 要隐藏的{@link Fragment}
     */
    void hideFragment(@NonNull Fragment fragment);

    /**
     * 根据tag 隐藏一个{@link Fragment} 并隐藏 该{@link Fragment}同级别的其他{@link Fragment}
     *
     * @param tag 要隐藏的{@link Fragment}的tag
     */
    void hideFragment(@NonNull String tag);

    /**
     * 显示一个{@link Fragment} 并移除该{@link Fragment}所属宿主所使用的的布局中所包含其他的{@link Fragment}
     * 如果该{@link Fragment} 已经显示了，则什么都不会做
     * <p>
     * 该方法添加的{@link Fragment}不会被压入堆栈。如果调用方法{@link #onBackPressed()}，则该片段没有任何操作。
     *
     * @param fragment        要显示的{@link Fragment}
     * @param containerViewId 宿主加载{@link Fragment} 的布局id
     */
    void replaceFragment(@NonNull Fragment fragment, @IdRes int containerViewId);

    /**
     * 显示一个{@link Fragment} 并移除该{@link Fragment}所属宿主所使用的的布局中所包含其他的{@link Fragment}
     * 如果该{@link Fragment} 已经显示了，则什么都不会做
     * 该方法添加的{@link Fragment}不会被压入堆栈。如果调用方法{@link #onBackPressed()}，则该片段没有任何操作。
     *
     * @param fragment 要显示的{@link Fragment}
     */
    void replaceFragment(@NonNull Fragment fragment);

    /**
     * 返回当前节点是否已经走过onResume生命周期
     *
     * @return 结果
     */
    boolean isResumed();

    /**
     * 关闭当前节点，如果当前节点是{@link Fragment}，那么会从父容器的堆栈中移除自己
     */
    void close();

    /**
     * 关闭当前节点，如果当前节点是{@link Fragment}，那么会从父容器的堆栈中移除自己
     * 此方法会禁用转场动画
     */
    void closeWithoutTransaction();

    /**
     * 关闭一个{@link Fragment}并将其从堆栈中删除。
     *
     * @param fragment 关闭一个{@link Fragment}，此{@link Fragment} 必须是当前节点的子{@link Fragment}
     */
    void close(@NonNull Fragment fragment);

    /**
     * 设置{@link Fragment} 的tag，此方法只在调用{@link #addFragment} 之前生效
     *
     * @param tag tag
     */
    void setFragmentTag(@NonNull String tag);

    /**
     * 返回当前{@link Fragment}的tag
     *
     * @return tag 如果不是{@link Fragment} 调用此方法 将会返回null
     */
    String getFragmentTag();

    /**
     * 返回此{@link Fragment}所在容器的布局id
     *
     * @return 结果
     */
    @IdRes
    int getContainerViewId();

    /**
     * 返回{@link Node#stickyStack()} 的值
     *
     * @return 结果
     */
    boolean isStickyStack();

    /**
     * 返回{@link LoadModel#loadModel()} 的值
     * <p>
     * 如果没有使用此注解标记 默认返回{@link EnumLoadModel#NORMAL_LOAD}
     *
     * @return 结果
     */
    EnumLoadModel getLoadModel();

    /**
     * 返回一个结果给调用此{@link Fragment}
     *
     * @param resultCode 请选择{@link DokodemoDoor#RESULT_OK} 或 {@link DokodemoDoor#RESULT_CANCELED}
     * @param bundle     要返回的数据
     */
    void setResult(int resultCode, Bundle bundle);

    /**
     * 返回当前{@link Fragment}的堆栈
     *
     * @return 结果
     */
    Stack<String> getFragmentTagStack();

    /**
     * 打印{@link Fragment}的堆栈
     */
    void printStack();
}
