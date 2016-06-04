package com.hangon.saying.util;

import java.util.List;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.example.fd.ourapplication.R;

public class MenuHelper {
    private PopupWindow popupWindow;
    private ListView listView;
    private List<String> data;
    private Context mContext;
    private View topView;
    private ListAdapter adapter;
    private int i = 0;
    private LinearLayout container;

    public MenuHelper(Context context, View topView, final OnMenuClick clickListener, List<String> data, LinearLayout containerView) {
        mContext = context;
        this.topView = topView;
        this.data = data;
        this.container = containerView;
        //container.getForeground().setAlpha(0);
        initListView(clickListener);
        initPopupWindow();
    }

    private void initListView(final OnMenuClick clickListener) {
        listView = new ListView(mContext);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setBackgroundColor(Color.WHITE);
        adapter = new ListAdapter(mContext);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                i = position;
                clickListener.onPopupMenuClick(position);
                popupWindow.dismiss();
            }
        });
    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(listView, 200, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (container != null) {
                    //container.getForeground().setAlpha(0);
                }
            }
        });
    }

    public void showMenu() {
        adapter.notifyDataSetChanged();
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            popupWindow.showAsDropDown(topView, 0, 0);
            if (container != null) {
                //container.getForeground().setAlpha(120);
            }
        }
    }

    private class ListAdapter extends ArrayAdapter<String> {
        public ListAdapter(Context context) {
            super(context, R.layout.callife_pull, data);
        }

        private Holder getHolder(final View view) {
            Holder holder = (Holder) view.getTag();
            if (holder == null) {
                holder = new Holder(view);
                view.setTag(holder);
            }
            return holder;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                rowView = inflater.inflate(R.layout.callife_pull, null);
            }
            final Holder holder = getHolder(rowView);

            holder.textview.setText(data.get(position));
            if (position == i) {
                holder.textview.setBackgroundColor(mContext.getResources().getColor(R.color.item_press));
            } else {
                holder.textview.setBackgroundColor(Color.TRANSPARENT);
            }
            return rowView;
        }

        private class Holder {
            public TextView textview;

            public Holder(View view) {
                textview = (TextView) view.findViewById(R.id.select_text);
            }
        }
    }
}
