package org.alee.dokodemo.door.core;

import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: {@link Fragment} 事务类封装
 *
 *********************************************************/
final class Transaction {
    /**
     * 无操作
     */
    static final int OPERATING_NULL = 0x1000;
    /**
     * 添加操作
     */
    static final int OPERATING_ADD = 0x1001;
    /**
     * 替换操作
     */
    static final int OPERATING_REPLACE = 0x1002;
    /**
     * 移除操作
     */
    static final int OPERATING_REMOVE = 0x1003;
    /**
     * 隐藏操作
     */
    static final int OPERATING_HIDE = 0x1004;
    /**
     * 显示操作
     */
    static final int OPERATING_SHOW = 0x1005;
    /**
     * 分离操作
     */
    static final int OPERATING_DETACH = 0x1006;
    /**
     * 连接操作
     */
    static final int OPERATING_ATTACH = 0x1007;
    /**
     * 上一个操作
     */
    private Transaction mPrevious;
    /**
     * 下一个操作
     */
    private Transaction mNext;
    /**
     * 指令
     */
    private int mCommand = OPERATING_NULL;
    /**
     * 要操作的{@link Fragment}
     */
    private Fragment mFragment;
    /**
     * 被操作{@link Fragment}的Tag
     */
    private String mFragmentTag;
    /**
     * 所处容器id
     */
    private int mContainerId;
    /**
     * 入场动画id
     */
    @AnimRes
    private int mEnterAnim;
    /**
     * 出场动画id
     */
    @AnimRes
    private int mExitAnim;

    public Transaction getPrevious() {
        return mPrevious;
    }

    public void setPrevious(Transaction previous) {
        mPrevious = previous;
    }

    public Transaction getNext() {
        return mNext;
    }

    public void setNext(Transaction next) {
        mNext = next;
    }

    public int getCommand() {
        return mCommand;
    }

    public void setCommand(int command) {
        mCommand = command;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public String getFragmentTag() {
        return null == mFragmentTag ? "" : mFragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        mFragmentTag = fragmentTag;
    }

    public int getContainerId() {
        return mContainerId;
    }

    public void setContainerId(int containerId) {
        mContainerId = containerId;
    }

    public int getEnterAnim() {
        return mEnterAnim;
    }

    public void setEnterAnim(int enterAnim) {
        mEnterAnim = enterAnim;
    }

    public int getExitAnim() {
        return mExitAnim;
    }

    public void setExitAnim(int exitAnim) {
        mExitAnim = exitAnim;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "mPrevious=" + mPrevious +
                ", mNext=" + mNext +
                ", mCommand=" + mCommand +
                ", mFragment=" + mFragment +
                ", mFragmentTag='" + mFragmentTag + '\'' +
                ", mContainerId=" + mContainerId +
                ", mEnterAnim=" + mEnterAnim +
                ", mExitAnim=" + mExitAnim +
                '}';
    }

    Transaction() {
    }

    Transaction(int command, Fragment fragment, String fragmentTag, int containerId) {
        mCommand = command;
        mFragment = fragment;
        mFragmentTag = fragmentTag;
        mContainerId = containerId;
    }

    Transaction(int command, String fragmentTag) {
        mCommand = command;
        mFragmentTag = fragmentTag;
    }

    /**
     * 拷贝
     *
     * @param transaction 数据来源
     * @return 拷贝后
     */
    public Transaction copy(Transaction transaction) {
        if (null == transaction) {
            return this;
        }
        this.setContainerId(transaction.getContainerId());
        this.setFragmentTag(transaction.getFragmentTag());
        this.setFragment(transaction.getFragment());
        this.setCommand(transaction.getCommand());
        this.setExitAnim(transaction.getExitAnim());
        this.setEnterAnim(transaction.getEnterAnim());
        this.setNext(transaction.getNext());
        this.setPrevious(transaction.getPrevious());
        return this;
    }
}
