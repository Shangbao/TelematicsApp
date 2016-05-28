package com.hangon.order.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import java.util.List;

public class FragmentViewPagerAdapter extends BaseFragmentPagerAdapter  {
    private List<Fragment> mFragments;

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


}

