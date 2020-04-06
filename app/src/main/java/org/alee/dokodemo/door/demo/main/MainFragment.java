package org.alee.dokodemo.door.demo.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.alee.dokodemo.door.core.DokodemoDoor;
import org.alee.dokodemo.door.demo.R;
import org.alee.dokodemo.door.demo.base.BaseFragment;
import org.alee.dokodemo.door.demo.base.OnItemClickListener;
import org.alee.dokodemo.door.demo.lazyLoad.LazyLoadFragment;
import org.alee.dokodemo.door.demo.saveState.TabBarFragment;

import java.util.Arrays;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: xxxx
 *
 *********************************************************/
public class MainFragment extends BaseFragment implements OnItemClickListener {
    static final String KEY_BUNDLE_TAB = "tab";
    private MainAdapter mAdapter;
    private RecyclerView mListView;

    @Override
    protected void setViewsValue() {
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void getViews() {
        mListView = findView(R.id.rv_list);
    }

    @Override
    protected void setListeners() {
        mAdapter.notifyData(Arrays.asList(getArguments().getStringArray(KEY_BUNDLE_TAB)));
    }

    @Override
    protected void initData() {
        mAdapter = new MainAdapter(mContext, this);
    }

    @Override
    protected void processClick(View v) {

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (mAdapter.getItem(position)) {
            case "TabFragment":
                DokodemoDoor.getNodeProxy(this).startFragment(new TabBarFragment());
                break;
            case "LazyLoad":
                DokodemoDoor.getNodeProxy(this).startFragment(new LazyLoadFragment());
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
