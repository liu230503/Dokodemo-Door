package org.alee.dokodemo.door.demo.lazyLoad;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.alee.dokodemo.door.annotation.Node;
import org.alee.dokodemo.door.demo.R;
import org.alee.dokodemo.door.demo.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: xxxx
 *
 *********************************************************/
public class LazyLoadFragment extends BaseFragment {
    private TabLayout mTabBar;

    private ViewPager mPagerView;

    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void setViewsValue() {
        mPagerView.setAdapter(mFragmentAdapter);
        mTabBar.setupWithViewPager(mPagerView);
        mPagerView.setOffscreenPageLimit(4);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lazy_load;
    }

    @Override
    protected void getViews() {
        mTabBar = findView(R.id.tb_title);
        mPagerView = findView(R.id.vp_pager);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void initData() {
        List<String> title = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();
        for (int i=0;i<4;i++){
            title.add("Title"+i+1);
            fragmentList.add(ItemFragment.newInstance(title.get(i)));
        }
        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager(),fragmentList,title);
    }

    @Override
    protected void processClick(View v) {

    }
}
