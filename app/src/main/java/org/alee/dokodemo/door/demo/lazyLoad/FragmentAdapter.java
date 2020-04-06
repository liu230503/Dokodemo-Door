package org.alee.dokodemo.door.demo.lazyLoad;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: xxxx
 *
 *********************************************************/
public class FragmentAdapter extends FragmentPagerAdapter {
    /**
     * Fragment列表
     */
    private List<Fragment> mFragments;
    /**
     * Title列表
     */
    private List<String> mTitles;

    public FragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titles) {
        super(fragmentManager);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments == null) {
            return null;
        }
        if (mFragments.size() <= position) {
            return null;
        }
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        if (mFragments == null) {
            return 0;
        }
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles == null) {
            return null;
        }
        if (mTitles.size() <= 0) {
            return null;
        }
        return mTitles.get(position % mTitles.size());
    }
}
