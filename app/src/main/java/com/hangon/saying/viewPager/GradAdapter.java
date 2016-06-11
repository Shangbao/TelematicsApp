package com.hangon.saying.viewPager;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public class GradAdapter extends BaseAdapter {
    Context context;
    List<String> list=new ArrayList<String>();

    GradAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
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


    ViewGradHolder gradHolder;
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            gradHolder = new ViewGradHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.carlife_grade_content, null);
            gradHolder.img = (ImageView) convertView.findViewById(R.id.item_grida_image);
            convertView.setTag(gradHolder);
        } else {
            gradHolder = (ViewGradHolder) convertView.getTag();
        }
        Log.e("list.size()",list.size()+"");
        if(list.get(position)!=null&&!list.get(position).equals("")){
           Log.e("GradAdapter",list.get(position));
            getImag(list.get(position));
        }

        return convertView;
    }

    class ViewGradHolder {
        ImageView img;
    }

    private void getImag(String path){
        String url= Constants.LOAD_SAYING_IMG_URL+path;
        ImageRequest request=new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                //gradHolder.img.setImageBitmap(bitmap);
                Drawable drawable=new BitmapDrawable(bitmap);
                gradHolder.img.setBackground(drawable);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"说说图片加载失败",Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getHttpQueues().add(request);
    }
}

