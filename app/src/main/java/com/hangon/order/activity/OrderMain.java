package com.hangon.order.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.hangon.order.util.FragmentAdapter;
import com.hangon.order.util.GetOrderData;

public class OrderMain extends FragmentActivity {
	private ViewPager mViewPagerOrder;
	private List mFragmentList;
	private FragmentAdapter mFragmentAdapter;
	/**
	 * Tab显示内容TextView
	 */
	private TextView tAllOrder;
	private TextView tAppointOrder;
	private TextView tPayOrder;
	private LinearLayout mAllOrdertext;
	private LinearLayout mAppointtext;
	private LinearLayout mPaytext;
	/**
	 * Tab的那个引导线
	 */
	private ImageView mTabLineIv;
	/**
	 * 获取数据类
	 */
	GetOrderData mGetOrderData;
	AllOrder mAllOrder;
	NotPay mNotPay;
	PayOrder mPayOrder;
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitFindViewVyId();
		init();
		initTabLineWidth();
	}

	// 获取ID
	private void InitFindViewVyId() {
		// //////
		tAllOrder = (TextView) findViewById(R.id.all_order);
		tAppointOrder = (TextView) findViewById(R.id.appointment_order);
		mViewPagerOrder = (ViewPager) findViewById(R.id.viewpager);
		tPayOrder = (TextView) findViewById(R.id.pay_order);
		mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
		mAllOrdertext=(LinearLayout)findViewById(R.id.all_order_text);
		mPaytext=(LinearLayout)findViewById(R.id.pay_order_text);
		mAppointtext=(LinearLayout)findViewById(R.id.appointment_order_text);
		mAllOrdertext.setOnClickListener(new OrderStatus(0));
		mAppointtext.setOnClickListener(new OrderStatus(1));
		mPaytext.setOnClickListener(new OrderStatus(2));
		mFragmentList = new ArrayList<>();
	}

	private void init() {
		mAllOrder = new AllOrder();
		mNotPay = new NotPay();
		mPayOrder = new PayOrder();
		mFragmentList.add(mAllOrder);
		mFragmentList.add(mNotPay);
		mFragmentList.add(mPayOrder);
		mFragmentAdapter = new FragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mViewPagerOrder.setAdapter(mFragmentAdapter);
		mViewPagerOrder.setCurrentItem(0);
		mViewPagerOrder.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {

			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
									   int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();

				Log.e("offset:", offset + "");
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景：
				 * 记3个页面,
				 * 从左到右分别为0,1,2 
				 * 0->1; 1->2; 2->1; 1->0
				 */

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));

				} else if (currentIndex == 1 && position == 1) // 1->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));
				} else if (currentIndex == 2 && position == 1) // 2->1
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));
				}
				mTabLineIv.setLayoutParams(lp);
			}

			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
					case 0:
						tAllOrder.setTextColor(Color.BLUE);
						break;
					case 1:
						tAppointOrder.setTextColor(Color.BLUE);
						break;
					case 2:
						tPayOrder.setTextColor(Color.BLUE);
						break;
				}
				currentIndex = position;
			}
		});

	}
	/**
	 * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 3;
		mTabLineIv.setLayoutParams(lp);
	}
	private void resetTextView() {
		tAllOrder.setTextColor(Color.BLACK);
		tAppointOrder.setTextColor(Color.BLACK);
		tPayOrder.setTextColor(Color.BLACK);
	}

//	/**
//	 * 头标点击监听 3
//	 */
	public class OrderStatus implements OnClickListener {
		private int index;

		public OrderStatus(int index) {
			this.index = index;
		}
		@Override
		public void onClick(View v) {
			mViewPagerOrder.setCurrentItem(index);
		}
	}

	@Override
	public void onAttachFragment(android.app.Fragment fragment) {
		super.onAttachFragment(fragment);
	}
}