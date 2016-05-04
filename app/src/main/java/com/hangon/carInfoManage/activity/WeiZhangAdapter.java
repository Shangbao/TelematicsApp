package com.hangon.carInfoManage.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fd.ourapplication.R;
import com.hangon.bean.carInfo.WeiZhangInfoVO;
import com.hangon.common.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/2.
 */
public class WeiZhangAdapter extends BaseAdapter{
    List<WeiZhangInfoVO> list=new ArrayList<WeiZhangInfoVO>();
    private LayoutInflater mInflater;

    public WeiZhangAdapter(List<WeiZhangInfoVO> list, Context context) {
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold=null;
        if(viewHold==null){
            viewHold=new ViewHold();
            convertView=mInflater.inflate(R.layout.item_weizhang,null);
            viewHold.weizhang_item_name= (TextView) convertView.findViewById(R.id.weizhang_item_name);
            viewHold.weizhang_item_cph= (TextView) convertView.findViewById(R.id.weizhang_item_cph);
            viewHold.weizhang_item_engineNum= (TextView) convertView.findViewById(R.id.weizhang_item_engineNum);
            viewHold.weizhang_item_cjh= (TextView) convertView.findViewById(R.id.weizhang_item_cjh);
            convertView.setTag(viewHold);
        }else {
            viewHold= (ViewHold) convertView.getTag();
        }

        viewHold.weizhang_item_name.setText(list.get(position).getName());
        viewHold.weizhang_item_cph.setText(Constants.PROVINCE_VALUE.charAt(list.get(position).getProvinceIndex())+list.get(position).getCarLicenceTail());
        viewHold.weizhang_item_engineNum.setText(list.get(position).getEngineNum());
        viewHold.weizhang_item_cjh.setText(list.get(position).getChassisNum());
        return convertView;
    }

    class ViewHold{
        TextView weizhang_item_name;
        TextView weizhang_item_engineNum;
        TextView weizhang_item_cjh;//车架号
        TextView weizhang_item_cph;//车牌号

    }
}
