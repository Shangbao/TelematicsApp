package com.hangon.carInfoManage.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.hangon.bean.carInfo.CarInfoVO;
import com.hangon.bean.carInfo.CarMessageVO;
import com.hangon.bean.music.Music;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class SetCarInfoAdapter extends BaseAdapter {
    private List<CarMessageVO> list = new ArrayList<CarMessageVO>();
    private LayoutInflater mInflater;

    public SetCarInfoAdapter(List<CarMessageVO> list, Context context) {
        this.list=list;
        this.mInflater=LayoutInflater.from(context);
    }
    btnClickListener listener;

    public interface btnClickListener{
        public void btnEditeClick(int position);
        public void btnDeleteClick(int position);
        public void btnScanClick(int position);
    }

    public void setBtnClickListener(btnClickListener btnListener){
        this.listener=btnListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;

        if(convertView==null){
            viewHold=new ViewHold();
            convertView=mInflater.inflate(R.layout.item_set_carinfo,null);
            viewHold.plateNum= (TextView) convertView.findViewById(R.id.plateNum);
            viewHold.name= (TextView) convertView.findViewById(R.id.name);
            viewHold.phoneNum= (TextView) convertView.findViewById(R.id.phoneNum);
            viewHold.defaultAddress= (TextView) convertView.findViewById(R.id.defaultAddress);
            viewHold.btnEdite= (Button) convertView.findViewById(R.id.btnEdite);
            viewHold.btnDelete= (Button) convertView.findViewById(R.id.btnDelete);
            viewHold.btnScan= (Button) convertView.findViewById(R.id.btnScan);
            convertView.setTag(viewHold);
        }else {
            viewHold= (ViewHold) convertView.getTag();
        }

        viewHold.name.setText(list.get(position).getName());
        viewHold.phoneNum.setText(list.get(position).getPhoneNum());
        viewHold.plateNum.setText(Constants.PROVINCE_VALUE.charAt(list.get(position).getProvinceIndex())+list.get(position).getCarLicenceTail());
        if(list.get(position).getState()==1){
            viewHold.defaultAddress.setVisibility(View.VISIBLE);
        }else {
            viewHold.defaultAddress.setVisibility(View.GONE);
        }

        viewHold.btnEdite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnEditeClick(position);
            }
        });

        viewHold.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnDeleteClick(position);
            }
        });

        viewHold.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnScanClick(position);
            }
        });

        return convertView;
    }

    class ViewHold{
        TextView plateNum;
        TextView name;
        TextView phoneNum;
        TextView defaultAddress;
        Button btnEdite;
        Button btnDelete;
        Button btnScan;
    }
}
