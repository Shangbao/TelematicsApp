package com.hangon.order.util;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class FragmentAdapter extends FragmentPagerAdapter {

	List<Fragment> fragmentList = new ArrayList<Fragment>();
	FragmentManager fm;
	public FragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
		this.fm=fm;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}
	@Override
	public int getCount() {
		return fragmentList.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		return super.instantiateItem(container, position);

	}
}