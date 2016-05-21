package com.hangon.order.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.common.CharacterSetECI;
import com.hangon.order.util.FragmentAdapter;
import com.hangon.order.util.GasOrderAdapter;
import com.hangon.order.util.Judge;
import com.mob.tools.gui.ViewPagerAdapter;

public class OrderMain extends FragmentActivity{
	private ViewPager mViewPagerOrder;
	private ArrayList<Fragment> mFragmentList;
	private FragmentAdapter mFragmentAdapter;
	//编辑
	TextView editText;
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
	//ViewPagerAdapter
	private ViewPagerAdapter viewPagerAdapter;
	//判断是否加载数据
	Judge judge;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		judge=new Judge();
		setContentView(R.layout.main);
		InitFindViewById();
		init();
		initTabLineWidth();
	}
	// 获取ID
	private void InitFindViewById() {
		tAllOrder = (TextView) findViewById(R.id.all_order);
		tAppointOrder = (TextView) findViewById(R.id.appointment_order);
		mViewPagerOrder = (ViewPager) findViewById(R.id.viewpager);
		tPayOrder = (TextView) findViewById(R.id.pay_order);
		mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
		mAllOrdertext = (LinearLayout) findViewById(R.id.all_order_text);
		mPaytext = (LinearLayout) findViewById(R.id.pay_order_text);
		mAppointtext = (LinearLayout) findViewById(R.id.appointment_order_text);
		//订单编辑
		editText = (TextView) findViewById(R.id.edit);
		editText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(OrderMain.this,EditOrder.class);
				startActivity(intent);
			}
		});
		//////////
		mAllOrdertext.setOnClickListener(new OrderStatus(0));
		mAppointtext.setOnClickListener(new OrderStatus(1));
		mPaytext.setOnClickListener(new OrderStatus(2));
		mFragmentList = new ArrayList<>();
		mAllOrder = new AllOrder();
		mNotPay = new NotPay();
		mPayOrder = new PayOrder();
	}

	private void init() {
		mFragmentList.add(mAllOrder);
		mFragmentList.add(mNotPay);
		mFragmentList.add(mPayOrder);
		mFragmentAdapter = new FragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mViewPagerOrder.setAdapter(mFragmentAdapter);

		mViewPagerOrder.setCurrentItem(0);
		mViewPagerOrder.setOffscreenPageLimit(1);
		mAllOrder.setUserVisibleHint(true);

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
                         Judge.setJudge(0);
						editText.setVisibility(View.VISIBLE);
						tAllOrder.setTextColor(Color.BLUE);
						break;
					case 1:
                       Judge.setJudge(1);
						editText.setVisibility(View.GONE);
						tAppointOrder.setTextColor(Color.BLUE);
						break;
					case 2:
						Judge.setJudge(2);
						editText.setVisibility(View.GONE);
						tPayOrder.setTextColor(Color.BLUE);
						break;
				}
				currentIndex = position;
			}

		});
		viewPagerAdapter = new ViewPagerAdapter() {
			@Override
			public int getCount() {
				return 0;
			}
			@Override
			public View getView(int i, View view, ViewGroup viewGroup) {
				return null;
			}
		};
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

	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onStop() {
		super.onStop();

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


}}