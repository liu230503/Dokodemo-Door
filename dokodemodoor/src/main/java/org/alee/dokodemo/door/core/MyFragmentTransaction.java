package org.alee.dokodemo.door.core;

import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: 自定义 {@link Fragment} 事务管理器
 *
 *********************************************************/
final class MyFragmentTransaction implements IFragmentTransaction {
    /**
     * {@link FragmentManager}
     */
    private FragmentManager mFragmentManager;
    /**
     * {@link IProxy}
     */
    private IProxy mProxy;
    /**
     * 头
     */
    private Transaction mHead;

    /**
     * 尾
     */
    private Transaction mTail;
    /**
     * 未处理事务的条数
     */
    private int mOperatingNumber;
    /**
     * 入场动画
     */
    @AnimRes
    private int mEnterAnim;
    /**
     * 出场动画
     */
    @AnimRes
    private int mExitAnim;

    /**
     * 事务链表
     */
    private LinkedList<Transaction> mTransactionLinkedList;
    /**
     * 被添加的Fragment
     */
    private final Map<String, WeakReference<Fragment>> mAddedFragment;

    MyFragmentTransaction(IProxy proxy, FragmentManager mFragmentManager) {
        this.mFragmentManager = mFragmentManager;
        this.mProxy = proxy;
        mAddedFragment = new HashMap<>();
    }

    /**
     * 添加一条待处理事务
     *
     * @param transaction {@link Transaction}
     */
    private void addTransaction(Transaction transaction) {
        if (null == mHead) {
            mHead = mTail = transaction;
        } else {
            transaction.setPrevious(mTail);
            mTail.setNext(transaction);
            mTail = transaction;
        }
        transaction.setEnterAnim(mEnterAnim);
        transaction.setExitAnim(mExitAnim);
        mOperatingNumber++;
    }

    @Override
    public Fragment find(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return null;
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (null != fragment) {
            return fragment;
        }
        if (mAddedFragment.containsKey(tag)) {
            fragment = mAddedFragment.get(tag).get();
            if (null == fragment) {
                mAddedFragment.remove(tag);
            }
        }
        return fragment;
    }

    @Override
    public IFragmentTransaction add(int containerViewId, Fragment fragment, @NonNull String tag) {
        addTransaction(new Transaction(Transaction.OPERATING_ADD, fragment, tag, containerViewId));
        if (!mAddedFragment.containsKey(tag)) {
            mAddedFragment.put(tag, new WeakReference<>(fragment));
        }
        return this;
    }

    @Override
    public IFragmentTransaction remove(String... tags) {
        if (null == tags) {
            return this;
        }

        if (0 >= tags.length) {
            return this;
        }
        for (String tag : tags) {
            addTransaction(new Transaction(Transaction.OPERATING_REMOVE, tag));
        }
        return this;
    }

    @Override
    public IFragmentTransaction removeAll() {
        if (mAddedFragment.isEmpty()) {
            return this;
        }
        for (String s : mAddedFragment.keySet()) {
            remove(s);
        }
        return this;
    }

    @Override
    public IFragmentTransaction show(String... tags) {
        if (null == tags) {
            return this;
        }
        if (0 >= tags.length) {
            return this;
        }

        for (String tag : tags) {
            addTransaction(new Transaction(Transaction.OPERATING_SHOW, tag));
        }
        return this;
    }

    @Override
    public IFragmentTransaction hide(String... tags) {
        if (null == tags) {
            return this;
        }
        if (0 >= tags.length) {
            return this;
        }
        for (String tag : tags) {
            addTransaction(new Transaction(Transaction.OPERATING_HIDE, tag));
        }
        return this;
    }

    @Override
    public void commit() {
        if (null == mTransactionLinkedList) {
            mTransactionLinkedList = new LinkedList<>();
        }
        if (null != mHead) {
            mTransactionLinkedList.add(new Transaction().copy(mHead));
        }
        mHead = mTail = null;
        mOperatingNumber = 0;
        executePendingTransaction();
    }

    @Override
    public void setCustomAnimations(int enter, int exit) {
        mEnterAnim = enter;
        mExitAnim = exit;
    }

    @Override
    public boolean isEmpty() {
        return 0 == mOperatingNumber;
    }

    /**
     * 执行待处理事务
     */
    private void executePendingTransaction() {
        if (!mProxy.isResumed()) {
            printLog("当前节点还未恢复, 提交将被延迟处理");
            return;
        }

        Transaction transaction = mTransactionLinkedList.poll();
        if (null == transaction) {
            return;
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        while (null != transaction) {
            handleTransaction(transaction, fragmentTransaction);
            transaction = transaction.getNext();
        }
        fragmentTransaction.commit();
        executePendingTransaction();
    }

    /**
     * 处理一条事务
     *
     * @param transaction         待处理事务
     * @param fragmentTransaction {@link FragmentTransaction}
     */
    private void handleTransaction(Transaction transaction, FragmentTransaction fragmentTransaction) {
        if (null == transaction) {
            return;
        }
        if (null == fragmentTransaction) {
            return;
        }
        switch (transaction.getCommand()) {
            case Transaction.OPERATING_ADD:
                fragmentTransaction.setCustomAnimations(transaction.getEnterAnim(), transaction.getExitAnim());
                fragmentTransaction.add(transaction.getContainerId(), transaction.getFragment(), transaction.getFragmentTag());
                break;
            case Transaction.OPERATING_ATTACH:
                Fragment attachFragment = find(transaction.getFragmentTag());
                if (null == attachFragment) {
                    printLog("处理添加任务时未能找到 [" + transaction.getFragmentTag() + "] 对应的Fragment");
                } else {
                    fragmentTransaction.attach(attachFragment);
                }
                break;
            case Transaction.OPERATING_DETACH:
                Fragment detachFragment = find(transaction.getFragmentTag());
                if (null == detachFragment) {
                    printLog("处理分离任务时未能找到 [" + transaction.getFragmentTag() + "] 对应的 Fragment");
                } else {
                    fragmentTransaction.detach(detachFragment);
                }
                break;
            case Transaction.OPERATING_HIDE:
                fragmentTransaction.setCustomAnimations(transaction.getEnterAnim(), transaction.getExitAnim());
                Fragment hideFragment = find(transaction.getFragmentTag());
                if (null == hideFragment) {
                    printLog("处理隐藏任务时未能找到 [" + transaction.getFragmentTag() + "] 对应的 Fragment");
                } else {
                    fragmentTransaction.hide(hideFragment);
                }
                break;
            case Transaction.OPERATING_REMOVE:
                Fragment removeFragment = find(transaction.getFragmentTag());
                if (null == removeFragment) {
                    printLog("处理移除任务时未能找到 [" + transaction.getFragmentTag() + "] 对应的 Fragment");
                } else {
                    fragmentTransaction.remove(removeFragment);
                    mAddedFragment.remove(transaction.getFragmentTag());
                }
                break;
            case Transaction.OPERATING_REPLACE:
                fragmentTransaction.setCustomAnimations(transaction.getEnterAnim(), transaction.getExitAnim());
                fragmentTransaction.replace(transaction.getContainerId(), transaction.getFragment(), transaction.getFragmentTag());
                break;
            case Transaction.OPERATING_SHOW:
                fragmentTransaction.setCustomAnimations(transaction.getEnterAnim(), transaction.getExitAnim());
                Fragment showFragment = find(transaction.getFragmentTag());
                if (null == showFragment) {
                    printLog("处理显示任务时未能找到 [" + transaction.getFragmentTag() + "] 对应的 Fragment");
                } else {
                    fragmentTransaction.show(showFragment);
                }
                break;
            default:
                break;
        }
    }

    private void printLog(String message) {
        if (DokodemoDoor.sUseDebugModel) {
            DokodemoDoor.sLogger.debug(this.getClass().getSimpleName(), message);
        }
    }
}
