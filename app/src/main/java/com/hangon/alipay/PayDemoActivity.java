package com.hangon.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.activity.MainOrderActivity;
import com.hangon.order.util.DialogTool;
import com.hangon.order.util.OrderData;

public class PayDemoActivity extends FragmentActivity {
    TextView product_subject;
    TextView product_price;
    TextView product_describe;
    String subjectValue = "";
    String describeValue = "";
    String priceValue = "";
    OrderData orderData;
    Dialog dialog;
    //topbar
    //topbar
    private ImageView topbarLeft;
    private ImageView topbarRight;
    private TextView topbarTittle;
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        paySuccess(orderData);
                        Toast.makeText(PayDemoActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayDemoActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayDemoActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alipay_pay_main);
        Intent intent = getIntent();
        init();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            orderData = (OrderData) bundle.getSerializable("selectedNoPay");
            Log.e("orderData", orderData.getCusName() + orderData.getCusPhoneNum());
            subjectValue = orderData.getGasType() + "型号汽油";
            describeValue = "共加油:" + orderData.getGasLitre() + "升" + "/" + orderData.getGasSinglePrice() + "元升";
            priceValue = orderData.getGasSumPrice();
            product_subject.setText(subjectValue);
            product_describe.setText(describeValue);
            product_price.setText(priceValue);
        }

    }

    /**
     * 初始化
     */
    private void init() {
        //topbarID
        topbarLeft = (ImageView) findViewById(R.id.topbar_left);
        topbarRight = (ImageView) findViewById(R.id.topbar_right);
        topbarTittle = (TextView) findViewById(R.id.topbar_title);
        topbarTittle.setText("订单支付");
        topbarRight.setVisibility(View.GONE);
        topbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(PayDemoActivity.this,MainOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
        product_subject = (TextView) findViewById(R.id.product_subject);
        product_price = (TextView) findViewById(R.id.product_price);
        product_describe = (TextView) findViewById(R.id.product_describe);
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(Keys.PARTNER) || TextUtils.isEmpty(Keys.RSA_PRIVATE)
                || TextUtils.isEmpty(Keys.SELLER)) {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    finish();
                                }
                            }).show();
            return;
        }
        if (orderData == null) {
            return;
        }
        // 订单
        String orderInfo = "";
        if (subjectValue.isEmpty() && describeValue.isEmpty() || priceValue.isEmpty()) {
            orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
        } else {
            orderInfo = getOrderInfo(subjectValue, describeValue, "0.01");
        }


        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayDemoActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(PayDemoActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Keys.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Keys.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, Keys.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * 支付成功以后的操作
     */
    private void paySuccess(OrderData orderData) {
        int orderId = orderData.getOrderId();
        String url = Constants.CHANGE_ORDER_INFO_URL + "?orderId=" + orderId + "";
        VolleyRequest.RequestGet(PayDemoActivity.this, url, "", new VolleyInterface(PayDemoActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
               dialog=new Dialog(PayDemoActivity.this);
                AlertDialog.Builder builder=new AlertDialog.Builder(PayDemoActivity.this);
                builder.setView(LayoutInflater.from(PayDemoActivity.this).inflate(R.layout.payorder_success_alert,null));
                dialog=builder.create();
                dialog.show();
                Timer timer=new Timer();
                timer.schedule(new wait(), 2000);

            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(PayDemoActivity.this, "网络异常，请重新支付", Toast.LENGTH_SHORT).show();
            }
        });
    }
    class wait extends TimerTask {

        @Override
        public void run() {
            dialog.dismiss();
            Intent intent=new Intent();
            intent.setClass(PayDemoActivity.this, MainOrderActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
