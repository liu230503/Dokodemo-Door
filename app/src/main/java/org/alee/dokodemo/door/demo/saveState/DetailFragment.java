package org.alee.dokodemo.door.demo.saveState;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.alee.dokodemo.door.core.DokodemoDoor;
import org.alee.dokodemo.door.demo.R;
import org.alee.dokodemo.door.demo.base.BaseFragment;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: xxxx
 *
 *********************************************************/
public class DetailFragment extends BaseFragment {

    public static DetailFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("content", content);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView mContentTv;

    private Button mBackBtn;

    @Override
    protected void setViewsValue() {
        mContentTv.setText(getArguments().getString("content"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void getViews() {
        mContentTv = findView(R.id.tv_content);
        mBackBtn = findView(R.id.btn_back);
    }

    @Override
    protected void setListeners() {
        setClick(mBackBtn);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void processClick(View v) {
        DokodemoDoor.getNodeProxy(this).close();
    }

}
