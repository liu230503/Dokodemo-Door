package org.alee.dokodemo.door.demo.lazyLoad;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.alee.dokodemo.door.annotation.EnumLoadModel;
import org.alee.dokodemo.door.annotation.LoadModel;
import org.alee.dokodemo.door.demo.R;
import org.alee.dokodemo.door.demo.base.BaseFragment;
import org.lmy.open.utillibrary.LogUtil;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: xxxx
 *
 *********************************************************/
@LoadModel(loadModel = EnumLoadModel.LAZY_LOAD)
public class ItemFragment extends BaseFragment {
    public static ItemFragment newInstance(String content) {

        Bundle args = new Bundle();
        args.putString("content", content);
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView mContentTv;

    @Override
    protected void setViewsValue() {
        mContentTv.setText(getArguments().getString("content"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_item;
    }

    @Override
    protected void getViews() {
        mContentTv = findView(R.id.tv_content);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void processClick(View v) {

    }

    @Override
    public void onLazyLoadViewCreated(Bundle savedInstanceState) {
        super.onLazyLoadViewCreated(savedInstanceState);
        LogUtil.d("liumy===", "onLazyLoadViewCreated " + mContentTv.getText());
        LogUtil.d("liumy===", this + "");
    }
}
