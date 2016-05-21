package com.hangon.map.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.bean.map.Gastprice;
import com.hangon.carInfoManage.activity.SetCarInfoActivity;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.map.util.GasInfoUtil;
import com.hangon.map.util.JudgeNet;
import com.hangon.order.activity.AppointmentOrder;
import com.hangon.order.activity.PersonalInformationData;
import com.hangon.order.activity.Success;
import com.hangon.order.util.OnItemclick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/4/24.
 */
public class GasSiteDetailsActivity extends Activity {
    //加油列表
    ListView gasTypeList;
    //加油站名称
    private TextView mGasNameDetails;
    //加油站类型
    private TextView mGasAreanameDetails;
    //加油站地址
    private TextView mGasAddressDetails;
    //距离
    private TextView mGasDistanceDetails;
    //进行线路查询
    private ImageView mGasStartRoute;
    //预约加油
    private Button mAppointAddGas;
    //获取加油站在列表中的位置
    private int position1;
    //用于进行状态判断
    JudgeNet judge;
    //存取加油站数据
    GasInfoUtil datainfo;
    Context context;
    //获取加油升数
    //升数
    private EditText mGasLitre;
    private EditText mGasmoney;
    private TextView appointGastype;
    private TextView appointGasSingleprice;
    ViewHorder viewHorder;
    View alertView;

    String cusName;
    String cusPhone;
    String cusPlatecar;
   private List<PersonalInformationData> personalInformationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_gas_details);
        context = GasSiteDetailsActivity.this;
        init();
        receive();
        datainfo = new GasInfoUtil();
    }
    //由于接受加油站的数据
    public void receive() {
        Bundle bundle = this.getIntent().getExtras().getBundle("mGasList");
        position1 = bundle.getInt("position");
        mGasAddressDetails.setText(GasInfoUtil.gasinfo.get(position1).getAddress());
        mGasDistanceDetails.setText(GasInfoUtil.gasinfo.get(position1).getDistance());
        mGasAreanameDetails.setText(GasInfoUtil.gasinfo.get(position1).getBrandname());
        mGasNameDetails.setText(GasInfoUtil.gasinfo.get(position1).getName());
       //加油站油号以及油价的列表
        final BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return GasInfoUtil.gasinfo.get(position1).getGastprice().size();
            }

            @Override
            public Object getItem(int position) {
                return GasInfoUtil.getGasinfo().get(position1).getGastprice().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    viewHorder = new ViewHorder();
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.gas_type_list, null);
                    viewHorder.gasType = (TextView) convertView.findViewById(R.id.gas_type);
                    viewHorder.gasSinglePrice = (TextView) convertView.findViewById(R.id.gas_type_singleprice);
                    convertView.setTag(viewHorder);
                } else {
                    viewHorder = (ViewHorder) convertView.getTag();
                }
                viewHorder.gasType.setText(GasInfoUtil.getGasinfo().get(position1).getGastprice().get(position).getName());
                viewHorder.gasSinglePrice.setText(GasInfoUtil.getGasinfo().get(position1).getGastprice().get(position).getPrice());
                return convertView;
            }
        };
        gasTypeList.setAdapter(adapter);

        gasTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                getData();
                //获取弹出框的View以及对应的控件
                LayoutInflater inflater = getLayoutInflater();
                alertView = inflater.inflate(R.layout.appointalert, null);
                appointGasSingleprice = (TextView) alertView.findViewById(R.id.appoint_gassingleprice);
                appointGastype = (TextView) alertView.findViewById(R.id.appoint_gastype);
                mGasLitre = (EditText) alertView.findViewById(R.id.alert_appoint_gaslitre);
                mGasmoney = (EditText) alertView.findViewById(R.id.alert_appoint_gasmoney);
                appointGastype = (TextView) alertView.findViewById(R.id.appoint_gastype);
                appointGasSingleprice = (TextView) alertView.findViewById(R.id.appoint_gassingleprice);
                appointGasSingleprice.setText(GasInfoUtil.getGasinfo().get(position1).getGastprice().get(position).getPrice());
                appointGastype.setText(GasInfoUtil.getGasinfo().get(position1).getGastprice().get(position).getName());
                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
                if (personalInformationList == null || personalInformationList.size() == 0) {
                    return;
                }
                final AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("预约加油");
                builder.setView(alertView);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (mGasLitre.getText().toString().trim().equals("") || mGasmoney.getText().toString().trim().equals("")) {
                            Toast.makeText(GasSiteDetailsActivity.this, "请输入钱数或者升数", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (mGasLitre.getText().toString().trim().equals("0") || mGasmoney.getText().toString().trim().equals("0")) {
                            Toast.makeText(GasSiteDetailsActivity.this, "钱数或者升数不能为0", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            PostVolley();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
//                DialogTool.createViewDialog(GasSiteDetailsActivity.this, "预约加油", alertView, "取消", "确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                }, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (mGasLitre.getText().toString().trim().equals("") || mGasmoney.getText().toString().trim().equals("")) {
//                            Toast.makeText(GasSiteDetailsActivity.this, "请输入钱数或者升数", Toast.LENGTH_SHORT).show();
//                            return;
//                        } else if (mGasLitre.getText().toString().trim().equals("0") || mGasmoney.getText().toString().trim().equals("0")) {
//                            Toast.makeText(GasSiteDetailsActivity.this, "钱数或者升数不能为0", Toast.LENGTH_SHORT).show();
//                            return;
//                        } else {
//                            PostVolley();
//                        }
//                    }
//                }).show();

//对加油升数的监听，当升数变时，钱数也变
                mGasLitre.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (mGasLitre.hasFocus() && mGasmoney.hasFocus() == false) {
                            if (mGasLitre.getText().toString().trim().equals("")) {
                                mGasmoney.setText("0");
                            } else {
                                mGasmoney.setText(Double.parseDouble((mGasLitre.getText() + " ").toString())
                                        * (Double.parseDouble(appointGasSingleprice.getText().toString() + " ")) + " ");
                            }
                        }
                    }
                });
                mGasmoney.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (mGasLitre.hasFocus() == false && mGasmoney.hasFocus()) {
                            if (mGasmoney.getText().toString().trim().equals("")) {
                                mGasLitre.setText("0");
                            } else {
                                mGasLitre.setText((Double.parseDouble((mGasmoney.getText() + " ").toString())
                                        / (Double.parseDouble(appointGasSingleprice.getText().toString() + " "))) + " ");
                            }
                        }
                    }
                });
            }
        });
    }

    //加油信息发送到数据库
    private void PostVolley() {
        double a = Double.valueOf(mGasLitre.getText().toString());
        double b = Double.valueOf(mGasmoney.getText().toString());
        String url = Constants.ADD_ORDER_INFO_URL;
        final Map map = new HashMap<>();
        map.put("gasStationAddress", mGasAddressDetails.getText().toString());
        map.put("gasStationName", mGasNameDetails.getText().toString());
        map.put("cusName", cusName.toString());
        map.put("cusPhoneNum", cusPhone.toString());
        map.put("gasSinglePrice", appointGasSingleprice.getText().toString());
        map.put("cusPlateNum", cusPlatecar.toString());
        map.put("gasType", appointGastype.getText().toString());
        map.put("gasSumPrice", a * b + "");
        map.put("gasLitre", mGasLitre.getText().toString());
        VolleyRequest.RequestPost(GasSiteDetailsActivity.this, url, "PostVolley", map, new VolleyInterface(GasSiteDetailsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Intent confirm = new Intent();
                confirm.setClass(GasSiteDetailsActivity.this, Success.class);
                startActivity(confirm);
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(GasSiteDetailsActivity.this, "网络错误", Toast.LENGTH_LONG).show();
            }
        });
    }

    //加油站列表对应的组件
    class ViewHorder {
        private TextView gasType;
        private TextView gasSinglePrice;
    }

    //获取页面各个控件以及监听事件的添加
    private void init() {
        judge = new JudgeNet();
        mGasStartRoute = (ImageView) findViewById(R.id.GasStartRoute);
        mGasAddressDetails = (TextView) findViewById(R.id.gasAddressDetails);
        mGasNameDetails = (TextView) findViewById(R.id.gasNameDetails);
        mGasDistanceDetails = (TextView) findViewById(R.id.gasDistanceDetails);
        mGasAreanameDetails = (TextView) findViewById(R.id.gasAreanameDetails);
        gasTypeList = (ListView) findViewById(R.id.gas_type_list);
        Onclick mOnclick = new Onclick();
        mGasStartRoute.setOnClickListener(mOnclick);
        String url = Constants.GET_CAR_INFO_URL;
        VolleyRequest.RequestGet(GasSiteDetailsActivity.this, url, "getData", new VolleyInterface(GasSiteDetailsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Gson gson = new Gson();
                Log.e("aaaa", result);
                List<PersonalInformationData> mPersonalList = gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {
                }.getType());
                personalInformationList = gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {
                }.getType());
            }
            @Override
            public void onMyError(VolleyError error) {


            }
        });
    }

    //设置监听事件
    class Onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.GasStartRoute:
                    //发送终点位置到最优路线,用来进行状态判断
                    judge.setStates(1);
                    judge.setAppointRoute(1);
                    Bundle bundle = new Bundle();
                    bundle.putString("endaddress", mGasAddressDetails.getText().toString());
                    Intent intent = new Intent();
                    intent.setClass(GasSiteDetailsActivity.this, MapMainActivity.class);
                    intent.putExtra("endaddress", bundle);
                    startActivity(intent);
                    break;

            }
        }
    }

    //个人信息
    private void Personal(List<PersonalInformationData> mPersonalList) {
        if (mPersonalList == null || mPersonalList.size() == 0) {

            DialogTool.createNormalDialog(GasSiteDetailsActivity.this, "绑定车辆信息", "对不起，检测不到你个车辆信息", "前去绑定", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(GasSiteDetailsActivity.this, SetCarInfoActivity.class);
                    startActivity(intent);
                    judge.setPersonalInformation(2);
                    finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    return;
                }
            }).show();
        } else {
            cusName = mPersonalList.get(0).getName();
            cusPhone = mPersonalList.get(0).getPhoneNum();
            cusPlatecar = (Constants.PROVINCE_VALUE.charAt(mPersonalList.get(0).getProvinceIndex()) + mPersonalList.get(0).getCarLicenceTail());
        }
    }
    //获取个人信息放入列表
    private void getData() {
        String url = Constants.GET_CAR_INFO_URL;
        VolleyRequest.RequestGet(GasSiteDetailsActivity.this, url, "getData", new VolleyInterface(GasSiteDetailsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Gson gson = new Gson();
                Log.e("aaaa", result);
                List<PersonalInformationData> mPersonalList = gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {}.getType());
                 personalInformationList=gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {}.getType());
                Personal(mPersonalList);
            }
            @Override
            public void onMyError(VolleyError error) {


            }
        });
    }

}
