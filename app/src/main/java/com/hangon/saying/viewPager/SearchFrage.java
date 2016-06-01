package com.hangon.saying.viewPager;

import java.util.ArrayList;
import java.util.List;


import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.bean.saying.SayingVO;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.util.BaseFragmentPagerAdapter;
import com.hangon.saying.layout.ViewPagerTestActivity;
import com.hangon.saying.view.XListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchFrage extends Fragment implements BaseFragmentPagerAdapter.UpdateAble, XListView.IXListViewListener {
    ViewHorder vh;
    List list = new ArrayList();
    View view;
    List<SayingVO> sayingList;
    private XListView mListView;
    private Handler mHandler;
    private SayingAdpter adapter;
    int image[] = {R.drawable.a, R.drawable.c, R.drawable.a, R.drawable.c, R.drawable.a, R.drawable.c};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.carlife_search_fra,null);
        mListView = (XListView) view.findViewById(R.id.zhoubian_list);
        mListView.setPullLoadEnable(true);
        getCzwDatas();
        mListView.setXListViewListener(this);
        geneItems();
        return view;
    }



    private void getCzwDatas() {
        String url = Constants.GET_CZW_SAYINGS;
        VolleyRequest.RequestGet(getActivity(), url, "getCzwDatas", new VolleyInterface(getActivity(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.e("getCzwDatas", result);
                Gson gson = new Gson();
                sayingList = new ArrayList<SayingVO>();
                sayingList=gson.fromJson(result,new TypeToken<List<SayingVO>>(){}.getType());
                adapter=new SayingAdpter(getActivity(),sayingList);
                mListView.setAdapter(adapter);
            }
            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }

    public class Adapter extends BaseAdapter {
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

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.carlife_list_layout, null);
                vh = new ViewHorder();
                vh.username = (TextView) convertView.findViewById(R.id.saying_nickname);
                vh.gridView = (GridView) convertView.findViewById(R.id.saying_gridview);
                convertView.setTag(vh);
            } else {
                vh = (ViewHorder) convertView.getTag();
            }
            vh.username.setText(list.get(position).toString());
            vh.gridView.setAdapter(new GradAdapter());
            vh.gridView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    Intent intent = new Intent(getActivity(),
                            ViewPagerTestActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

    class GradAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return image.length;
        }

        @Override
        public Object getItem(int position) {
            return image[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGradHorder gradHorder;
            if (convertView == null) {
                gradHorder = new ViewGradHorder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.carlife_grade_content, null);
                gradHorder.img = (ImageView) convertView.findViewById(R.id.item_grida_image);
                convertView.setTag(gradHorder);
            } else {
                gradHorder = (ViewGradHorder) convertView.getTag();
            }
            gradHorder.img.setImageResource(image[position]);
            return convertView;
        }
    }

    class ViewGradHorder {
        ImageView img;
    }

    class ViewHorder {
        TextView username;
        GridView gridView;
    }

    @Override
    public void update() {

    }

    //请求数据
    private void geneItems() {
        Toast.makeText(getActivity(), "geneItem", Toast.LENGTH_LONG).show();
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        geneItems();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems();
                mListView.setAdapter(adapter);
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        geneItems();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems();
                adapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }
}
