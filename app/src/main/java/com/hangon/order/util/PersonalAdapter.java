package com.hangon.order.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.fd.ourapplication.R;
import com.hangon.map.util.JudgeNet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
public class PersonalAdapter extends BaseAdapter {
 private List list=new ArrayList<>();
 private Context context;
 int resource;
 JudgeNet judge;
 public PersonalAdapter(List list,Context context,int resource ) {
   this.list=list;
   this.context=context;
   this.resource=resource;
 }
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}
	@Override
	public long getItemId(int position) {
		return  position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=new ViewHolder();
		View v=LayoutInflater.from(context).inflate(resource, null);
		judge = new JudgeNet();
		holder.cusname=(TextView)v.findViewById(R.id.personal_cus_name);
		holder.cusphone=(TextView)v.findViewById(R.id.personal_cus_phone);
		holder.cusplate=(TextView)v.findViewById(R.id.personal_cus_plate);
		holder.personal_ll=(LinearLayout)v.findViewById(R.id.personal_LL);
        HashMap map=(HashMap)list.get(position);
        holder.cusname.setText(map.get("personal_name").toString());
        holder.cusphone.setText(map.get("personal_phone").toString()); 
        holder.cusplate.setText(map.get("personal_plate").toString());
		return v;
	}
	class ViewHolder {
		TextView cusname;
		TextView cusphone;
		TextView cusplate;
		LinearLayout personal_ll;
	}
	
}
