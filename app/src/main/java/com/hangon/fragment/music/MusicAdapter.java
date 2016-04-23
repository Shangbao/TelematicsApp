package com.hangon.fragment.music;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fd.ourapplication.R;
import com.hangon.bean.music.Music;
import com.hangon.common.MusicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/10.
 */
public class MusicAdapter extends BaseAdapter {
    private List<Music> list = new ArrayList<Music>();
    private LayoutInflater mInflater;
    private int currIndex;

    public MusicAdapter(List<Music> list, Context context, int currIndex) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.currIndex = currIndex;
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
        ViewHold viewHold = null;
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView = mInflater.inflate(R.layout.item_music, null);

            viewHold.title = (TextView) convertView
                    .findViewById(R.id.item_title);
            viewHold.time = (TextView) convertView.findViewById(R.id.item_time);
            viewHold.singer = (TextView) convertView
                    .findViewById(R.id.item_singer);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.title.setText(list.get(position).getTitle());
        viewHold.singer.setText(list.get(position).getSinger());
        viewHold.time.setText(MusicUtil.toTime((int) list.get(position)
                .getTime()));

        if (position == currIndex) {
            viewHold.title.setTextColor(Color.BLUE);
            viewHold.singer.setTextColor(Color.BLUE);
            viewHold.time.setTextColor(Color.BLUE);
        } else {
            viewHold.title.setTextColor(Color.BLACK);
            viewHold.singer.setTextColor(Color.GRAY);
            viewHold.time.setTextColor(Color.GRAY);
        }
        return convertView;
    }

    class ViewHold {
        TextView title;
        TextView singer;
        TextView time;
    }

    public int getCurrIndex() {
        return currIndex;
    }

    public void setCurrIndex(int currIndex) {
        this.currIndex = currIndex;
    }
}