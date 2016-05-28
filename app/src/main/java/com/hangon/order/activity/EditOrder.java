package com.hangon.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.common.Constants;
import com.hangon.common.Topbar;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.util.Bean;
import com.hangon.order.util.GasOrderAdapter;
import com.hangon.order.util.OnItemclick;
import com.hangon.order.util.OrderData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by fd on 2016/5/8.
 */
public class EditOrder extends Activity {
    EditOrderAdapter adapter;
    List<OrderData> orderList;
    int judge = 1;
    List<Bean> list;
    private ArrayList<HashMap<String, String>> arrayList;
    //订单编辑
    TextView deleteEdit;
    CheckBox allOrderSelected;
    Topbar editTopBar;
    ListView editOrderList;
    ViewHolder viewHolder;
    //记录选中的条目数量
    private int checkNum;
    //显示选中数目
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_order);
        arrayList = new ArrayList<>();
        initFindViewById();
    }

    private void initFindViewById() {
        deleteEdit = (TextView) findViewById(R.id.delete_order_edit);
        allOrderSelected = (CheckBox) findViewById(R.id.all_selected);
        editOrderList = (ListView) findViewById(R.id.edit_order);
        editTopBar = (Topbar) findViewById(R.id.edittopbar);
        editTopBar.setLeftIsVisible(false);
        editTopBar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {
                finish();
            }
        });
        getData();
        //全选按钮
        allOrderSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allOrderSelected.isChecked()) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        arrayList.get(i).put("flag", "true");
                    }
                    checkNum = arrayList.size();

                }
                if (!allOrderSelected.isChecked()) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).get("flag").equals("true")) {
                            arrayList.get(i).put("flag", "false");
                            checkNum--;//数量减一
                        }
                    }
                    checkNum = arrayList.size();
                    judge = 1;
                }
                judge = 0;
                DataChange();
            }
        });
        //删除数据
        deleteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList list = new ArrayList<Bean>();
                Toast.makeText(getApplicationContext(), arrayList.size() + "", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).get("flag").equals("true")) {
                        Bean bean = new Bean("id" + i, orderList.get(i).getOrderId() + "");
                        list.add(bean);
                    }
                }
                PostVolley(list);
                Iterator<HashMap<String, String>> iterator = arrayList.iterator();
                while (iterator.hasNext()) {
                    int i = 0;
                    HashMap<String, String> temp = iterator.next();
                    if (temp.get("flag").equals("true")) {
                        iterator.remove();
                    }
                }
                checkNum = 0;
                DataChange();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
        editOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取得ViewHorder对象，
                ViewHolder holder = (ViewHolder) view.getTag();

                holder.item_cb.toggle();
                if (holder.item_cb.isChecked() == true) {
                    arrayList.get(position).put("flag", "true");
                    checkNum++;
                } else {
                    arrayList.get(position).put("flag", "false");
                    checkNum--;
                }
                //用TextView 显示
            }
        });
    }

    //装载数据
    public void getData() {
        Toast.makeText(getApplicationContext(), "ALLPRDERGETGETDATA", Toast.LENGTH_SHORT).show();
        String url = Constants.GET_ORDER_INFOS_URL;
        VolleyRequest.RequestGet(getApplicationContext(), url, "getData", new VolleyInterface(getApplicationContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Gson gson = new Gson();
                Log.e("aaaa", result);
                Toast.makeText(getApplicationContext(), "ALLPRDERGETDATA", Toast.LENGTH_SHORT).show();
                orderList = gson.fromJson(result, new TypeToken<List<OrderData>>() {
                }.getType());
                List<OrderData> orderList1 = gson.fromJson(result, new TypeToken<List<OrderData>>() {
                }.getType());
                InidData(orderList1);
                adapter = new EditOrderAdapter(arrayList);
                editOrderList.setAdapter(adapter);
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(getApplicationContext(), "网络错误 ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void InidData(List<OrderData> orderList) {
        Toast.makeText(getApplicationContext(), "ssssssss", Toast.LENGTH_SHORT).show();
        if (orderList == null || orderList.size() == 0) {
            return;
        } else {
            for (int i = 0; i < orderList.size(); i++) {
                Toast.makeText(getApplicationContext(), "bbbbbb" + orderList.get(0).getOrderState(), Toast.LENGTH_SHORT).show();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("flag", "false");
                map.put("gasname", orderList.get(i).getGasStationName());
                map.put("gasaddress", orderList.get(i).getGasStationAddress());
                map.put("gastype", orderList.get(i).getGasType());
                map.put("gasordertime", orderList.get(i).getStrTime());
                map.put("gasLitre", orderList.get(i).getGasLitre());
                map.put("gassumprice", orderList.get(i).getGasSumPrice());
                if (orderList.get(i).getOrderState() == 1) {
                    map.put("gasstate", "已支付未加油");
                } else if (orderList.get(i).getOrderState() == 0) {
                    map.put("gasstate", "未支付");
                } else if (orderList.get(i).getOrderState() == 2) {
                    map.put("gasstate", "已加油");
                }
                arrayList.add(map);
            }
        }

    }

    private void DataChange() {
        adapter.notifyDataSetChanged();
    }

    public class EditOrderAdapter extends BaseAdapter {
        ArrayList<HashMap<String, String>> list;

        public EditOrderAdapter(ArrayList<HashMap<String, String>> list1) {
            this.list = list1;
            Toast.makeText(getApplicationContext(), "aaaaaaa", Toast.LENGTH_SHORT).show();
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
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.orderlist_edit4, null);
                viewHolder.list_gasname = (TextView) convertView.findViewById(R.id.list_editorder_gasname);
                viewHolder.list_gaslitre = (TextView) convertView.findViewById(R.id.list_gaslitre_edit);
                viewHolder.list_gasorder_status = (TextView) convertView.findViewById(R.id.list_editorder_status);
                viewHolder.list_gassumprice = (TextView) convertView.findViewById(R.id.list_gassumprice_edit);
                viewHolder.list_gastype = (TextView) convertView.findViewById(R.id.list_gastype_edit);
                viewHolder.list_ordertime = (TextView) convertView.findViewById(R.id.list_ordertime_edit);
                viewHolder.item_cb = (CheckBox) convertView.findViewById(R.id.item_cb);
                viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.liner_edit);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.list_gasname.setText(list.get(position).get("gasname").toString());
            viewHolder.list_gastype.setText(list.get(position).get("gastype").toString());
            viewHolder.list_ordertime.setText(list.get(position).get("gasordertime").toString());
            viewHolder.list_gaslitre.setText(list.get(position).get("gasLitre").toString());
            viewHolder.list_gasorder_status.setText(list.get(position).get("gasstate").toString());
            viewHolder.list_gassumprice.setText(list.get(position).get("gassumprice").toString());
            viewHolder.item_cb.setVisibility(View.VISIBLE);
            viewHolder.item_cb.setChecked(list.get(position).get("flag").equals("true"));
            if (viewHolder.item_cb.isChecked()) {
                viewHolder.linearLayout.setBackgroundColor(Color.BLACK);
                notifyDataSetChanged();
            }
            if (!viewHolder.item_cb.isChecked()) {
                viewHolder.linearLayout.setBackgroundColor(Color.WHITE);
                notifyDataSetChanged();
            }
            return convertView;
        }
    }

    private void PostVolley(List<Bean> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        String url = Constants.DELETE_ORDER_INFOS_URL;
        Map map = new HashMap();
        map.put("json", json);
        VolleyRequest.RequestPost(getApplicationContext(), url, "PostVolley", map, new VolleyInterface(getApplicationContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Intent confirm = new Intent();
                confirm.setClass(getApplicationContext(), Success.class);
                //confirm.putExtra("orderdata",bundle);
                //startActivity(confirm);
                //  finish();
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
            }
        });
    }

    class ViewHolder {
        //关于订单列表字段
        /**
         * 加油站名称
         */
        TextView list_gasname;
        /**
         * 支付状态
         */
        TextView list_gasorder_status;
        /**
         * 订单时间
         */
        TextView list_ordertime;
        /**
         * 加油类型
         */
        TextView list_gastype;
        /**
         * 加油升数
         */
        TextView list_gaslitre;
        /**
         * 总金额
         */
        TextView list_gassumprice;
        /**
         * 复选框
         */
        CheckBox item_cb;
        LinearLayout linearLayout;
    }
}
