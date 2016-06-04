package com.hangon.saying.viewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.bean.saying.SayingVO;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.util.BaseFragmentPagerAdapter;
import com.hangon.saying.view.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class HelpFrage extends Fragment implements BaseFragmentPagerAdapter.UpdateAble, XListView.IXListViewListener {
    View view;
    List<SayingVO> sayingList;
    private XListView mListView;
    private SayingAdpter adapter;
    int image[] = {R.drawable.a, R.drawable.c, R.drawable.a, R.drawable.c, R.drawable.a, R.drawable.c};
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.carlife_help_fra, null);
        init();
        return view;
    }

    //初始化
    private void init() {
        mHandler = new Handler();
        mListView = (XListView) view.findViewById(R.id.zhoubian_list);
        mListView.setPullLoadEnable(true);
        getQzDatas(0);
        mListView.setXListViewListener(this);
    }

    private void delete(final int position) {
        String url = Constants.DELETE_SAYING + "?sayingId=" + sayingList.get(position).getSayingId();

        VolleyRequest.RequestGet(getActivity(), url, "deleteSaying", new VolleyInterface(getActivity(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                sayingList.remove(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(getActivity(), "网络异常，删除失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getQzDatas(final int totalItems) {
        String url = Constants.GET_QZ_SAYINGS + "?totalItemCount=" + totalItems;
        VolleyRequest.RequestGet(getActivity(), url, "getCzwDatas", new VolleyInterface(getActivity(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.e("getCzwDatas", result);
                if (result.equals("no_saying")) {
                    Toast.makeText(getActivity(), "车友圈无人发表说说", Toast.LENGTH_SHORT).show();
                } else if (result.equals("load_all")) {
                    Toast.makeText(getActivity(), "已加载所有的说说", Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    if (totalItems == 0) {
                        sayingList = new ArrayList<SayingVO>();
                        sayingList = gson.fromJson(result, new TypeToken<List<SayingVO>>() {
                        }.getType());
                        adapter = new SayingAdpter(getActivity(), sayingList);
                        adapter.setBtnClickListener(new SayingAdpter.btnClickListener() {
                            @Override
                            public void btnDeleteClick(final int position) {
                                DialogTool.createNormalDialog(getActivity(), "删除说说", "真的要删除吗?", "确认", "取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        delete(position);
                                    }
                                }, null).show();
                            }
                        });
                        mListView.setAdapter(adapter);
                    } else if (totalItems != 0) {
                        List<SayingVO> tempList = new ArrayList<SayingVO>();
                        tempList = gson.fromJson(result, new TypeToken<List<SayingVO>>() {
                        }.getType());
                        for (int i = 0; i < tempList.size(); i++) {
                            sayingList.add(tempList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }


    @Override
    public void update() {
        init();
    }

    @Override
    public void update(int totalItems) {

    }

    //请求数据
    private void geneItems() {
        Toast.makeText(getActivity(), "geneItem", Toast.LENGTH_LONG).show();
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str=simpleDateFormat.format(date);
        mListView.setRefreshTime(str);
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getQzDatas(sayingList.size());
                onLoad();
            }
        }, 2000);
    }
}
