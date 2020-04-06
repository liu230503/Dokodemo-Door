package org.alee.dokodemo.door.demo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import org.alee.dokodemo.door.annotation.Node;
import org.lmy.open.utillibrary.PreferenceUtil;

import java.util.List;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: FragmentActivity模板类
 *
 *********************************************************/
@Node
public abstract class BaseFragmentActivity extends FragmentActivity {
    /**
     * 数据存储器
     */
    protected PreferenceUtil mSpfUtil = null;
    /**
     * 上下文
     */
    protected Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpfUtil = PreferenceUtil.getInstance();
        mContext = BaseFragmentActivity.this;
        setContentView(getLayoutId());
        initData();
        getViews();
        setViewsValue();
        setListeners();
    }

    /**
     * 设置布局文件
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 关联控件
     */
    protected abstract void getViews();

    /**
     * 关联控件值
     */
    protected abstract void setViewsValue();

    /**
     * 关联控件点击事件
     */
    protected abstract void setListeners();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (null == fragment) {
                continue;
            }
            handleResult(fragment, requestCode, resultCode, data);
        }
    }

    private void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments();
        if (null == childFragment) {
            return;
        }
        for (Fragment child : childFragment) {
            if (null == child) {
                continue;
            }
            handleResult(child, requestCode, resultCode, data);
        }
    }

    public Integer getContainerViewId() {
        return 0;
    }
}
