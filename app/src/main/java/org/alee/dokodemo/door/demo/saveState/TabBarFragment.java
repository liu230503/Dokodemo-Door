package org.alee.dokodemo.door.demo.saveState;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.alee.dokodemo.door.core.DokodemoDoor;
import org.alee.dokodemo.door.demo.R;
import org.alee.dokodemo.door.demo.base.BaseFragment;
import org.alee.dokodemo.door.demo.base.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: xxxx
 *
 *********************************************************/
public class TabBarFragment extends BaseFragment implements OnItemClickListener {

    private TabLayout mTabLayout;

    private RecyclerView mListView;

    private Adapter mAdapter;

    @Override
    protected void setViewsValue() {
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        for (int i = 0; i < 4; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText("Tab" + i));
        }
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_bar;
    }

    @Override
    protected void getViews() {
        mTabLayout = findView(R.id.tb_title);
        mListView = findView(R.id.rv_list);
    }

    @Override
    protected void setListeners() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    list.add(tab.getText() + "__" + i);
                }
                mAdapter.notifyData(list);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mAdapter.notifyData(new ArrayList<>());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void initData() {
        mAdapter = new Adapter(mContext, this);
    }

    @Override
    protected void processClick(View v) {

    }

    @Override
    public void onItemClick(View view, int position) {
        DokodemoDoor.getNodeProxy(this).startFragment(DetailFragment.newInstance(mAdapter.getItem(position)));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
