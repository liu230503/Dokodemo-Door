package org.alee.dokodemo.door.demo.main;

import android.os.Bundle;

import org.alee.dokodemo.door.annotation.Node;
import org.alee.dokodemo.door.core.DokodemoDoor;
import org.alee.dokodemo.door.demo.R;
import org.alee.dokodemo.door.demo.base.BaseFragmentActivity;

import static org.alee.dokodemo.door.demo.main.MainFragment.KEY_BUNDLE_TAB;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: xxxx
 *
 *********************************************************/
@Node(containerViewId = R.id.contentFrame)
public class MainActivity extends BaseFragmentActivity {
    private static final String[] TAB = new String[]{"TabFragment", "LazyLoad"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getViews() {

    }

    @Override
    protected void setViewsValue() {
        Bundle bundle = new Bundle();
        bundle.putStringArray(KEY_BUNDLE_TAB, TAB);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        DokodemoDoor.getNodeProxy(this).startFragment(fragment);
    }

    @Override
    protected void setListeners() {

    }
}
