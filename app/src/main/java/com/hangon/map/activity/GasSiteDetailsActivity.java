package com.hangon.map.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangon.carInfoManage.activity.SetCarInfoActivity;
import com.hangon.common.Constants;
import com.hangon.common.DialogTool;
import com.hangon.common.UserUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.map.util.GasInfoUtil;
import com.hangon.map.util.JudgeNet;
import com.hangon.order.activity.PersonalInformationData;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/4/24.
 */
public class GasSiteDetailsActivity extends Activity {

    //topbar
    private ImageView topbarLeft;
    private ImageView topbarRight;
    private TextView topbarTittle;
    //加油列表
    private ListView gasTypeList;
    //加油站名称
    private TextView mGasNameDetails;
    //加油站类型
    private TextView mGasAreanameDetails;
    //加油站地址
    private TextView mGasAddressDetails;
    //距离
    private TextView mGasDistanceDetails;
    //进行线路查询

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
    private  ImageView mGasStartRoute;
    String cusName;
    String cusPhone;
    String cusPlatecar;

    //预约加油
    Dialog dialog ;
    private Dialog dialog1;
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
                mGasLitre.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                        //消除小数点后超过两位的字符​
                        if (s.toString().contains(".")) {
                            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                                s = s.toString().subSequence(0,
                                        s.toString().indexOf(".") + 3);
                                mGasLitre.setText(s);
                                mGasLitre.setSelection(s.length());
                            }
                        }
                        //输入的第一个字符为小数点时，自动在小数点前面不一个零
                        if (s.toString().trim().substring(0).equals(".")) {
                            s = "0" + s;
                            mGasLitre.setText(s);
                            mGasLitre.setSelection(2);
                        }
                        //如果输入的第一个和第二个字符都为0，则消除第二个0
                        if (s.toString().startsWith("0")
                                && s.toString().trim().length() > 1) {
                            if (!s.toString().substring(1, 2).equals(".")) {
                                mGasLitre.setText(s.subSequence(0, 1));
                                mGasLitre.setSelection(1);
                                return;
                            }
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }

                });
                mGasmoney.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                        //消除小数点后超过两位的字符​
                        if (s.toString().contains(".")) {
                            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                                s = s.toString().subSequence(0,
                                        s.toString().indexOf(".") + 3);
                                mGasmoney.setText(s);
                                mGasmoney.setSelection(s.length());
                            }
                        }
                        //输入的第一个字符为小数点时，自动在小数点前面不一个零
                        if (s.toString().trim().substring(0).equals(".")) {
                            s = "0" + s;
                            mGasmoney.setText(s);
                            mGasmoney.setSelection(2);
                        }
                        //如果输入的第一个和第二个字符都为0，则消除第二个0
                        if (s.toString().startsWith("0")
                                && s.toString().trim().length() > 1) {
                            if (!s.toString().substring(1, 2).equals(".")) {
                                mGasmoney.setText(s.subSequence(0, 1));
                                mGasmoney.setSelection(1);
                                return;
                            }
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                });


                //Yes or NO
                ImageView orderYes = (ImageView) alertView.findViewById(R.id.appoint_order_yes);
                ImageView orderNo = (ImageView) alertView.findViewById(R.id.appoint_order_no);
                appointGasSingleprice = (TextView) alertView.findViewById(R.id.appoint_gassingleprice);
                appointGasSingleprice.setText(GasInfoUtil.getGasinfo().get(position1).getGastprice().get(position).getPrice());
                appointGastype.setText(GasInfoUtil.getGasinfo().get(position1).getGastprice().get(position).getName());
                if (personalInformationList == null || personalInformationList.size() == 0) {
                    return;
                }
                dialog = new Dialog(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(alertView);
                dialog = builder.create();
                dialog.show();
                orderNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                orderYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mGasLitre.getText().toString().trim().equals("") || mGasmoney.getText().toString().trim().equals("")) {
                            Toast.makeText(GasSiteDetailsActivity.this, "请输入钱数或者升数", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (mGasLitre.getText().toString().trim().equals("0") || mGasmoney.getText().toString().trim().equals("0")) {
                            Toast.makeText(GasSiteDetailsActivity.this, "钱数或者升数不能为0", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            PostVolley();
                            dialog.dismiss();
                        }
                    }
                });

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
                                BigDecimal bigDecimal=new BigDecimal(Double.parseDouble((mGasLitre.getText() + "").toString())
                                        * (Double.parseDouble(appointGasSingleprice.getText().toString() + "")) );
                                double value=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                                mGasmoney.setText(value+ "");
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
                                BigDecimal bigDecimal=new BigDecimal((Double.parseDouble((mGasmoney.getText() + "").toString().trim())
                                        / (Double.parseDouble(appointGasSingleprice.getText().toString() + ""))) );
                                String value=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()+"".trim();
                                mGasLitre.setText(value+"");
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
        double b = Double.valueOf(appointGasSingleprice.getText().toString());
        String url = Constants.ADD_ORDER_INFO_URL;
        final Map map = new HashMap<>();
        UserUtil.instance(GasSiteDetailsActivity.this);
        int userId = UserUtil.getInstance().getIntegerConfig("userId");
        map.put("userId", userId + "");
        map.put("gasStationAddress", mGasAddressDetails.getText().toString());
        map.put("gasStationName", mGasNameDetails.getText().toString());
        map.put("cusName", cusName.toString());
        map.put("cusPhoneNum", cusPhone.toString());
        map.put("gasSinglePrice", appointGasSingleprice.getText().toString());
        map.put("cusPlateNum", cusPlatecar.toString());
        map.put("gasType", appointGastype.getText().toString());
        //BigDecimal bigDecimal=new BigDecimal(a*b);
        //double value=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        map.put("gasSumPrice",mGasmoney.getText().toString().trim()+"");
        map.put("gasLitre", mGasLitre.getText().toString().trim()+"");
        VolleyRequest.RequestPost(GasSiteDetailsActivity.this, url, "PostVolley", map, new VolleyInterface(GasSiteDetailsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                dialog1=new Dialog(GasSiteDetailsActivity.this);
                AlertDialog.Builder builder=new AlertDialog.Builder(GasSiteDetailsActivity.this);
                builder.setView(LayoutInflater.from(GasSiteDetailsActivity.this).inflate(R.layout.appointorder_success_alert,null));
                dialog1=builder.create();
                dialog1.show();
                Timer timer=new Timer();
                timer.schedule(new wait(), 2000);

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
        String userId=Constants.USER_ID+"";
        String url = Constants.GET_CAR_INFO_URL+"?userId="+userId;
        VolleyRequest.RequestGet(GasSiteDetailsActivity.this, url, "getData", new VolleyInterface(GasSiteDetailsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Gson gson = new Gson();
                List<PersonalInformationData> mPersonalList = gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {
                }.getType());
                personalInformationList = gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {
                }.getType());
            }

            @Override
            public void onMyError(VolleyError error) {


            }
        });
        //topbarID
        topbarLeft = (ImageView) findViewById(R.id.topbar_left);
        topbarRight = (ImageView) findViewById(R.id.topbar_right);
        topbarTittle = (TextView) findViewById(R.id.topbar_title);
        topbarTittle.setText("加油站详情");
        topbarRight.setVisibility(View.GONE);
        topbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        judge = new JudgeNet();
        mGasStartRoute = (ImageView) findViewById(R.id.GasStartRoute);
        mGasAddressDetails = (TextView) findViewById(R.id.gasAddressDetails);
        mGasNameDetails = (TextView) findViewById(R.id.gasNameDetails);
        mGasDistanceDetails = (TextView) findViewById(R.id.gasDistanceDetails);
        mGasAreanameDetails = (TextView) findViewById(R.id.gasAreanameDetails);
        gasTypeList = (ListView) findViewById(R.id.gas_type_list);
        Onclick mOnclick = new Onclick();
        mGasStartRoute.setOnClickListener(mOnclick);
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
                            GasInfoUtil.getGasinfo().get(position1).getLat();
                            bundle.putDouble("lon", GasInfoUtil.getGasinfo().get(position1).getLon());
                            bundle.putDouble("lat", GasInfoUtil.getGasinfo().get(position1).getLat());
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
        String userId=Constants.USER_ID+"";
        String url = Constants.GET_CAR_INFO_URL+"?userId="+userId;
        VolleyRequest.RequestGet(GasSiteDetailsActivity.this, url, "getData", new VolleyInterface(GasSiteDetailsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Gson gson = new Gson();
                Log.e("aaaa", result);
                List<PersonalInformationData> mPersonalList = gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {
                }.getType());
                personalInformationList = gson.fromJson(result, new TypeToken<List<PersonalInformationData>>() {
                }.getType());
                Personal(mPersonalList);
            }

            @Override
            public void onMyError(VolleyError error) {


            }
        });
    }
    class wait extends TimerTask {

        @Override
        public void run() {
            dialog1.dismiss();

        }
    }
}
