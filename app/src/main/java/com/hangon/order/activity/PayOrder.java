package com.hangon.order.activity;

import java.util.List;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.util.BaseFragmentPagerAdapter;
import com.hangon.order.util.DialogTool;
import com.hangon.order.util.GasOrderAdapter;
import com.hangon.order.util.Judge;
import com.hangon.order.util.OnItemclick;
import com.hangon.order.util.OrderData;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PayOrder extends Fragment implements BaseFragmentPagerAdapter.UpdateAble {
    /**
     * 获取ListView
     */
    ListView mOrderPayList;
    /**
     * 接收数据来自 GetOrderData
     */
    /**
     * 获取数据类
     */
    Payadapter adapter;
    View payorder;
    ViewHolder vh;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        payorder = inflater.inflate(R.layout.pay_order, container, false);
        mOrderPayList = (ListView) payorder.findViewById(R.id.pay_order_list);
        context = getActivity();
        Toast.makeText(getActivity(), Judge.getJudge() + "", Toast.LENGTH_SHORT);
        getData();
        return payorder;
    }

    //装载数据
    public void getData() {
        String url = Constants.GET_YZF_ORDER_INFOS_URL;
        VolleyRequest.RequestGet(getActivity(), url, "getData", new VolleyInterface(getActivity(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Gson gson = new Gson();
                Log.e("PayOrder", result);
                List<OrderData> list = gson.fromJson(result, new TypeToken<List<OrderData>>() {
                }.getType());
                mOrderPayList.setOnItemClickListener(new OnItemclick(getActivity(), list));
                adapter = new Payadapter(list);
                mOrderPayList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }

    @Override
    public void update() {
        getData();
    }

    public class Payadapter extends BaseAdapter {
        List<OrderData> payOrderList;

        Payadapter(List<OrderData> list) {
            this.payOrderList = list;
        }

        @Override
        public int getCount() {
            return payOrderList.size();
        }

        @Override
        public Object getItem(int position) {
            return payOrderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.orderlist, null);
                vh.list_gasname = (TextView) convertView.findViewById(R.id.list_order_gasname);
                vh.list_gassumprice = (TextView) convertView.findViewById(R.id.list_gassumprice);
                vh.list_gaslitre = (TextView) convertView.findViewById(R.id.list_gaslitre);
                vh.list_gastype = (TextView) convertView.findViewById(R.id.list_gastype);
                vh.list_ordertime = (TextView) convertView.findViewById(R.id.list_ordertime);
                vh.list_gasorder_status = (TextView) convertView.findViewById(R.id.list_gasorder_status);
                vh.gaslist_cancel_order = (TextView) convertView.findViewById(R.id.gaslist_cancel_order);
                vh.gaslist_payment_order = (TextView) convertView.findViewById(R.id.gaslist_payment_order);
                vh.qrSweep = (TextView) convertView.findViewById(R.id.list_sweep_code);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            PayOnclickListener listener = new PayOnclickListener(position);
            vh.gaslist_cancel_order.setOnClickListener(listener);
            vh.gaslist_payment_order.setOnClickListener(listener);
            vh.qrSweep.setOnClickListener(listener);
            vh.list_gasname.setText(payOrderList.get(position).getGasStationName());
            vh.list_gassumprice.setText(payOrderList.get(position).getGasSumPrice());
            vh.list_gaslitre.setText(payOrderList.get(position).getGasLitre());
            vh.list_ordertime.setText(payOrderList.get(position).getStrTime());
            vh.list_gastype.setText(payOrderList.get(position).getGasType());

            if ((payOrderList.get(position).getOrderState() == 2)) {
                vh.list_gasorder_status.setText("已加油");
                vh.gaslist_cancel_order.setVisibility(View.GONE);
                vh.gaslist_payment_order.setText("删除订单");
                vh.qrSweep.setVisibility(View.GONE);
            }
            if ((payOrderList.get(position).getOrderState() == 1)) {
                vh.list_gasorder_status.setText("已支付未加油");
                vh.gaslist_payment_order.setText("扫码加油");
                vh.gaslist_cancel_order.setVisibility(View.GONE);
                vh.gaslist_payment_order.setVisibility(View.VISIBLE);
                vh.qrSweep.setVisibility(View.GONE);
            }

            notifyDataSetChanged();
            return convertView;
        }

        public class PayOnclickListener implements View.OnClickListener {
            int position;

            public PayOnclickListener(int position) {
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.gaslist_payment_order:
                        if (payOrderList.get(position).getOrderState() == 2) {
                            DialogTool.createNormalDialog(context, "删除订单", "确定删除吗？", "取消", "确定", null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int orderId = payOrderList.get(position).getOrderId();

                                    String url = Constants.DELETE_ORDER_INFO_URL + "?orderId=" + orderId + "";
                                    VolleyRequest.RequestGet(context, url, "aaa", new VolleyInterface(context, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                                        @Override
                                        public void onMySuccess(String result) {
                                            payOrderList.remove(position);
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onMyError(VolleyError error) {
                                            Toast.makeText(context, "网络错误", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).show();
                        }
                        if (payOrderList.get(position).getOrderState() == 1) {
                            String URL = "htttp://" + Constants.HOST_IP + ":8080/wind/UserLogin?orderId=" + payOrderList.get(position).getOrderId() + "&orderState=" + payOrderList.get(position).getOrderState();
                            Bitmap QRcode = EncodingUtils.createQRCode(URL, 500, 500, null);
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.qrcode, null);
                            TextView cusname = (TextView) view.findViewById(R.id.qr_cusname);
                            TextView gastype = (TextView) view.findViewById(R.id.qr_gastype);
                            TextView gasSumPrice = (TextView) view.findViewById(R.id.qr_gassumprice);
                            TextView gasState = (TextView) view.findViewById(R.id.qr_state);
                            cusname.setText(payOrderList.get(position).getCusName());
                            gastype.setText(payOrderList.get(position).getGasType());
                            gasSumPrice.setText(payOrderList.get(position).getGasSumPrice());
                            ImageView QR = (ImageView) view.findViewById(R.id.qrcode_img);

                            String tradState = "";
                            if (payOrderList.get(position).getOrderState() == 2) {
                                tradState = "已加油";
                            } else if (payOrderList.get(position).getOrderState() == 1) {
                                tradState = "已支付未加油";
                            } else if (payOrderList.get(position).getOrderState() == 0) {
                                tradState = "未支付";
                            }
                            gasState.setText(tradState);

                            QR.setImageBitmap(QRcode);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(view);
                            builder.create().show();
                        }
                        break;

                    case R.id.list_sweep_code:
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.qrcode, null);
                        TextView cusname = (TextView) view.findViewById(R.id.qr_cusname);
                        TextView gastype = (TextView) view.findViewById(R.id.qr_gastype);
                        TextView gasSumPrice = (TextView) view.findViewById(R.id.qr_gassumprice);
                        cusname.setText(payOrderList.get(position).getCusName());
                        gastype.setText(payOrderList.get(position).getGasType());
                        gasSumPrice.setText(payOrderList.get(position).getGasSumPrice());
                        ImageView QR = (ImageView) view.findViewById(R.id.qrcode_img);
                        QR.setImageResource(R.drawable.ic_launcher);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setView(view);
                        builder.create().show();
                        break;
                }
            }
        }
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
         * 取消订单
         */
        TextView gaslist_cancel_order;
        /**
         * 付款项（当已经完结时，会将其改为删除按钮）
         */
        TextView gaslist_payment_order;
        //扫码加油
        TextView qrSweep;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "PAYORDER", Toast.LENGTH_SHORT).show();
    }
}
