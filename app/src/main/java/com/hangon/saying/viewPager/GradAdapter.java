package com.hangon.saying.viewPager;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.MyApplication;
import com.hangon.common.ViewHolder;
import com.hangon.common.VolleyBitmapCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public class GradAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    Context context;
    List list = new ArrayList();
    private static ImageLoader mImageLoader; // imageLoader对象，用来初始化NetworkImageView
    /**
     * 记录每个子项的高度。
     */
    private int mItemHeight = 0;

    GradAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
        mImageLoader = new ImageLoader(MyApplication.queues, new VolleyBitmapCache()); // 初始化一个loader对象，可以进行自定义配置

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

        NetworkImageView networkImageView = (NetworkImageView) gradHolder.img;
        // 设置默认的图片
        networkImageView.setDefaultImageResId(R.drawable.default_photo);
        // 设置图片加载失败后显示的图片
        networkImageView.setErrorImageResId(R.drawable.error_photo);


        if (list.get(position) != null && !list.get(position).equals("")) {
            //getImag(list.get(position).toString());
            // 开始加载网络图片
            networkImageView.setImageUrl(Constants.LOAD_SAYING_IMG_URL + list.get(position), mImageLoader);
        }

        return convertView;
    }


    class ViewGradHolder {
        ImageView img;
    }

    private void getImag(String path) {
        String url = Constants.LOAD_SAYING_IMG_URL + path;
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                gradHolder.img.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "说说图片加载失败", Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getHttpQueues().add(request);
    }


    /**
     * 设置item子项的高度。
     */
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        notifyDataSetChanged();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
// 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
        if (scrollState == SCROLL_STATE_IDLE) {
            // loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
        } else {
            // cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}

