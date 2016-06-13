package com.hangon.fragment.music;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fd.ourapplication.R;
import com.hangon.bean.music.Mp3;
import com.hangon.bean.music.Music;
import com.hangon.common.CircleView;

import java.util.List;

/**
 * Created by Administrator on 2016/6/13.
 */
public class Mp3Adapter extends BaseAdapter {
    private List<Mp3> list;
    private LayoutInflater mInflater;
    private int currIndex;

    public Mp3Adapter(List<Mp3> list, Context context, int currIndex) {
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
            viewHold.number = (CircleView) convertView.findViewById(R.id.item_num);
            viewHold.title = (TextView) convertView
                    .findViewById(R.id.item_title);
            viewHold.singer = (TextView) convertView
                    .findViewById(R.id.item_singer);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.number.setText(list.get(position).getNumber());
        viewHold.title.setText(list.get(position).getName());
        viewHold.singer.setText(list.get(position).getSingerName());

        if (list.size() > 2) {
            if (position % 2 == 1) {
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.item_music));
            }
        }

        if (position == currIndex) {
            viewHold.title.setTextColor(convertView.getResources().getColor(R.color.item_music_selected));
            viewHold.singer.setTextColor(convertView.getResources().getColor(R.color.item_music_selected));
            viewHold.number.setTextColor(convertView.getResources().getColor(R.color.item_music_selected));
            // viewHold.number.setText(""+currIndex);
        } else {
            viewHold.number.setTextColor(Color.BLACK);
            viewHold.title.setTextColor(Color.BLACK);
            viewHold.singer.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    class ViewHold {
        TextView title;
        TextView singer;
        CircleView number;
    }

    public int getCurrIndex() {
        return currIndex;
    }

    public void setCurrIndex(int currIndex) {
        this.currIndex = currIndex;
    }
}
