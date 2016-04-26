package com.hangon.map.activity;

import java.util.ArrayList;
import java.util.List;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.location.an;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.platform.comapi.map.B;
import com.example.fd.ourapplication.R;

/**
 * 所在位置适配器
 *
 *
 */
public class PoiSearchAdapter extends BaseAdapter implements Comparable<PoiInfo>{
	Pattern pattern;
	Matcher matcher;
	private Context context;
	private List<PoiInfo> list;
	private ViewHolder holder;
	private String address;

	public PoiSearchAdapter(Context context,List<PoiInfo> appGroup) {
		this.context = context;
		this.list = appGroup;
		this.address=address;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int location) {
		return list.get(location);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void addObject(List<PoiInfo> mAppGroup) {
		this.list = mAppGroup;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_activity_poi_search, null);
			holder.mpoi_name = (TextView) convertView
					.findViewById(R.id.mpoiNameT);
			holder.mpoi_address = (TextView) convertView
					.findViewById(R.id.mpoiAddressT);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		pattern=Pattern.compile("[\u4e00-\u9fa5]");
		for(int i=0;i<list.size()-1;i++){
			if(list.get(i).address.length()>list.get(i+1).address.length()){
				String a=list.get(i).address;
			}
		}
		holder.mpoi_name.setText(list.get(position).name);
		holder.mpoi_address.setText(list.get(position).address);
		return convertView;
	}
	public class ViewHolder {
		public TextView mpoi_name;// 名称
		public TextView mpoi_address;// 地址
	}
	@Override
	public int compareTo(PoiInfo another) {
		return Integer.valueOf(this.address)-Integer.valueOf(another.address);
	}

}
