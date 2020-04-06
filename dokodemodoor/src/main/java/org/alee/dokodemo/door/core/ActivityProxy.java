package org.alee.dokodemo.door.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import org.alee.dokodemo.door.exception.UnSupportException;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: {@link FragmentActivity} 类的代理
 *
 *********************************************************/
final class ActivityProxy extends BaseProxy {
    /**
     * {@link FragmentActivity}
     */
    private FragmentActivity mFragmentActivity;
    /**
     * 标识是否已走过onResume 生命周期
     */
    private boolean mIsResumed = false;

    ActivityProxy(@NonNull FragmentActivity activity) {
        super(activity);
        this.mFragmentActivity = activity;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = mFragmentActivity.getApplicationContext();
        if (null == mFragmentTransaction) {
            mFragmentTransaction = new MyFragmentTransaction(this, mFragmentActivity.getSupportFragmentManager());
        }
        if (null == mFragmentStackManager) {
            mFragmentStackManager = new FragmentStackManager();
        }
    }

    @Override
    public void onResumeFragments() {
        mIsResumed = true;
        mFragmentTransaction.commit();
    }

    @Override
    void onResume() {
        mIsResumed = true;
    }

    @Override
    void onPause() {
        mIsResumed = false;
    }

    @Override
    void onSaveInstanceState(Bundle outState) {
        mFragmentStackManager.saveInstanceState(outState);
    }

    @Override
    void onDestroy() {
        mFragmentStackManager.onDestroy();
        if (mFragmentActivity.isFinishing()) {
            mFragmentStackManager.clear();
            if (null == mFragmentTransaction) {
                return;
            }
            mFragmentTransaction.removeAll();
        }
    }


    @Override
    public boolean isResumed() {
        return mIsResumed;
    }

    @Override
    public void close() {
        mFragmentActivity.finish();
    }

    @Override
    public void closeWithoutTransaction() {
        close();
        mFragmentActivity.overridePendingTransition(0, 0);
    }


    @Override
    public void setFragmentTag(@NonNull String tag) {
        printLog(mFragmentActivity.getClass().getCanonicalName() + "----- setFragmentTag(String tag) 只能在Fragment 中调用！");
    }

    @Override
    public String getFragmentTag() {
        printLog(mFragmentActivity.getClass().getCanonicalName() + "----- getFragmentTAG() 只能在Fragment 中调用！");
        return null;
    }


    @Override
    public void setResult(int resultCode, Bundle bundle) {
        throw new UnSupportException("setResult() 函数只支持在Fragment 中调用");
    }

}
