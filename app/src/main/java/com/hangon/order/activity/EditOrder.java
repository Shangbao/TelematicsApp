package com.hangon.order.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.util.Bean;
import com.hangon.order.util.OrderData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fd on 2016/5/8.
 */
public class EditOrder extends Activity {
    //订单编辑标题栏
    private ImageButton topbarLeft;
    private ImageButton topbarRight;
    private TextView topbarTittle;
    EditOrderAdapter adapter;
    List<OrderData> orderList;
    int judge = 1;
    List<Bean> list;
    private ArrayList<HashMap<String, String>> arrayList;
    //订单编辑
    TextView deleteEdit;
    Dialog dialog,dialog1;
    ListView editOrderList;
    ViewHolder viewHolder;
    //记录选中的条目数量
    private int checkNum;
    //显示选中数目
    private TextView tv_show;
    CheckBox allOrderSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_order);
        arrayList = new ArrayList<>();
        initFindViewById();
    }

    private void initFindViewById() {
        getData();
        //topbarID
        topbarLeft=(ImageButton)findViewById(R.id.topbar_left);
        topbarRight=(ImageButton)findViewById(R.id.topbar_right);
        topbarTittle=(TextView)findViewById(R.id.topbar_title);
        topbarTittle.setText("编辑订单");
       topbarLeft.setVisibility(View.GONE);
        topbarRight.setBackgroundResource(R.drawable.grzx_041 );
        topbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(EditOrder.this,MainOrderActivity.class);
                startActivity(intent);
               finish();
            }
        });
        deleteEdit = (TextView) findViewById(R.id.delete_order_edit);
        allOrderSelected = (CheckBox) findViewById(R.id.all_selected);
        editOrderList = (ListView) findViewById(R.id.edit_order);
        //全选按钮
        allOrderSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList == null || arrayList.size() == 0) {
                    Toast.makeText(EditOrder.this, "可删除列表为空", Toast.LENGTH_SHORT).show();
                    return;
                }
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

               final  ArrayList list = new ArrayList<Bean>();
                if(arrayList==null||arrayList.size()==0){
                    Toast.makeText(EditOrder.this,"请选择要删除的订单",Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).get("flag").equals("true")) {
                        Bean bean = new Bean("id" + i, orderList.get(i).getOrderId() + "");
                        list.add(bean);
                    }
                }
                if(list!=null||list.size()!=0){
                    View cancelView1=LayoutInflater.from(EditOrder.this).inflate(R.layout.order_alert,null);
                    ImageView cancelYes1=(ImageView)cancelView1.findViewById(R.id.calcel_order_yes);
                    ImageView cancelNo1=(ImageView)cancelView1.findViewById(R.id.calcel_order_no);
                    TextView alertContent1=(TextView)cancelView1.findViewById(R.id.alert_content);
                    dialog=new Dialog(EditOrder.this);
                    AlertDialog.Builder builder=new AlertDialog.Builder(EditOrder.this);
                    builder.setView(cancelView1);
                    dialog=builder.create();
                    dialog.show();
                    alertContent1.setText("是否删除订单？");
                    cancelYes1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PostVolley(list);
                            dialog.dismiss();
                        }
                    });
                    cancelNo1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                Iterator<HashMap<String, String>> iterator = arrayList.iterator();
                while (iterator.hasNext()) {
                    int i = 0;
                    HashMap<String, String> temp = iterator.next();
                    if (temp.get("flag").equals("true")) {
                        iterator.remove();
                    }
                }

            }}
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
       String userId=Constants.USER_ID+"";
        String url = Constants.GET_ORDER_INFOS_URL+"?userId="+userId;
        VolleyRequest.RequestGet(getApplicationContext(), url, "getData", new VolleyInterface(getApplicationContext(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Gson gson = new Gson();
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
        if (orderList == null || orderList.size() == 0) {
            return;
        } else {
            for (int i = 0; i < orderList.size(); i++) {
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
                viewHolder.list_gastype = (TextView) convertView.findViewById(R.id.list_gastype_edit);
                viewHolder.list_ordertime = (TextView) convertView.findViewById(R.id.list_ordertime_edit);
                viewHolder.item_cb = (CheckBox) convertView.findViewById(R.id.item_cb);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.list_gasname.setText(list.get(position).get("gasname").toString());
            viewHolder.list_gastype.setText(list.get(position).get("gastype").toString());
            viewHolder.list_ordertime.setText(list.get(position).get("gasordertime").toString());

            viewHolder.item_cb.setVisibility(View.VISIBLE);
            viewHolder.item_cb.setChecked(list.get(position).get("flag").equals("true"));
            if (viewHolder.item_cb.isChecked()) {
                notifyDataSetChanged();
            }
            if (!viewHolder.item_cb.isChecked()) {
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
                dialog1=new Dialog(EditOrder.this);
                AlertDialog.Builder builder=new AlertDialog.Builder(EditOrder.this);
                builder.setView(LayoutInflater.from(EditOrder.this).inflate(R.layout.delete_alert_success, null));
                dialog1=builder.create();
                dialog1.show();
                Timer timer=new Timer();
                timer.schedule(new wait(), 2000);
                checkNum = 0;
                DataChange();

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
      //  TextView list_gasorder_status;
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
       // TextView list_gaslitre;
        /**
         * 总金额
         */
       // TextView list_gassumprice;
        /**
         * 复选框
         */
        CheckBox item_cb;
        LinearLayout linearLayout;
    }
    class wait extends TimerTask {

        @Override
        public void run() {
            dialog1.dismiss();
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainOrderActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
