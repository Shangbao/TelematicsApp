package com.hangon.order.util;

import java.util.ArrayList;
import java.util.List;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class FragmentAdapter extends FragmentStatePagerAdapter {

	List<Fragment> fragmentList = new ArrayList<Fragment>();
	FragmentManager fm;

	public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
		this.fm = fm;
	}

	public void setFragmentLists(ArrayList<Fragment> fragmentList) {
		if (this.fragmentList != null) {
			android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : this.fragmentList) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();
		}
		this.fragmentList = fragmentList;
		notifyDataSetChanged();
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
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return super.isViewFromObject(view, object);
	}

}
