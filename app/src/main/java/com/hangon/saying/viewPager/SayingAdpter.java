package com.hangon.saying.viewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.fd.ourapplication.R;
import com.hangon.bean.carInfo.CarMessageVO;
import com.hangon.bean.saying.SayingVO;
import com.hangon.common.Constants;
import com.hangon.common.MyApplication;
import com.hangon.saying.layout.ViewPagerTestActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SayingAdpter extends BaseAdapter {
    private List<SayingVO> list = new ArrayList<SayingVO>();
    private Context context;

    public SayingAdpter(Context context,List<SayingVO> list) {
        this.list = list;
        this.context = context;
    }

    btnClickListener listener;

    public interface btnClickListener {
        public void btnDeleteClick(int position);
    }

    public void setBtnClickListener(btnClickListener btnListener) {
        this.listener = btnListener;
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


    ViewHorder vh;

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            vh = new ViewHorder();
            convertView = LayoutInflater.from(context).inflate(R.layout.carlife_list_layout, null);
            vh.saying_userimg = (ImageView) convertView.findViewById(R.id.saying_userimg);
            vh.saying_nickname = (TextView) convertView.findViewById(R.id.saying_nickname);
            vh.saying_time = (TextView) convertView.findViewById(R.id.saying_time);
            vh.saying_delete = (Button) convertView.findViewById(R.id.saying_delete);
            vh.saying_content = (TextView) convertView.findViewById(R.id.saying_content);
            vh.gridView = (GridView) convertView.findViewById(R.id.saying_gridview);
            vh.saying_address = (TextView) convertView.findViewById(R.id.saying_address);
            convertView.setTag(vh);
        } else {
            vh = (ViewHorder) convertView.getTag();
        }
       getImg(list.get(position).getUserIconUrl().trim().toString());
        vh.saying_nickname.setText(list.get(position).toString());
        vh.saying_nickname.setText(list.get(position).getNickName().trim().toString());
       // Date date = list.get(position).getPostTime();
        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String postTime = format.format(date);
        vh.saying_time.setText(postTime);
        vh.saying_address.setText(list.get(position).getPostAddress());
        vh.saying_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnDeleteClick(position);
            }
        });
        List<String> mList = new ArrayList<String>();
        if (list.get(position).getImg1() != null && !list.get(position).getImg1().equals("")) {
            mList.add(list.get(position).getImg1());
            Log.e("getImg1",list.get(position).getImg1());
        }
        if (list.get(position).getImg2() != null && !list.get(position).getImg2().equals("")) {
            mList.add(list.get(position).getImg2());
            Log.e("getImg2", list.get(position).getImg2());
        }

        if (list.get(position).getImg3() != null && !list.get(position).getImg3().equals("")) {
            mList.add(list.get(position).getImg3());
            Log.e("getImg3", list.get(position).getImg3());
        }

        if (list.get(position).getImg4() != null && !list.get(position).getImg4().equals("")) {
            mList.add(list.get(position).getImg4());
            Log.e("getImg4", list.get(position).getImg4());
        }

        if (list.get(position).getImg5() != null && !list.get(position).getImg5().equals("")) {
            mList.add(list.get(position).getImg5());
        }

        if (list.get(position).getImg6() != null && !list.get(position).getImg6().equals("")) {
            mList.add(list.get(position).getImg6());
        }
        if (mList != null && mList.size() != 0) {
            vh.gridView.setAdapter(new GradAdapter(context, mList));
            vh.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    Intent intent = new Intent(context,
                            ViewPagerTestActivity.class);
                    intent.putExtra("ID", arg2);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    class ViewHorder {
        ImageView saying_userimg;
        TextView saying_nickname;
        TextView saying_time;
        Button saying_delete;
        TextView saying_content;
        TextView saying_gridview;
        TextView saying_address;
        GridView gridView;
    }

    public void getImg(String path) {
        String url = Constants.LOAD_USER_ICON_URL + "/" + path;
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                vh.saying_userimg.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getHttpQueues().add(request);
    }

}
