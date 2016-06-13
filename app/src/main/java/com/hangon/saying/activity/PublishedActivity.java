package com.hangon.saying.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.VolleyError;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.ImageUtil;
import com.hangon.common.UserUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import com.hangon.saying.util.Location;
import com.hangon.saying.viewPager.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PublishedActivity extends Activity {
    private LocationClient mLocClient;
    private String address;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private TextView activity_selectimg_send;
    private TextView cancel_selectimg;
    private TextView location_address_publish;
    private TextView publishContent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carlife_selectimg);
        Init();
        mLocClient = ((Location) getApplication()).mLocationClient;
        ((Location) getApplication()).mTv = location_address_publish;
        setLocationOption();
        mLocClient.start();
    }

    public void Init() {
        location_address_publish = (TextView) findViewById(R.id.location_address_publish);
        publishContent = (TextView) findViewById(R.id.publish_content);
        cancel_selectimg = (TextView) findViewById(R.id.cancel_selectimg);
        cancel_selectimg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bimp.drr.clear();
                Bimp.bmp.clear();
                Bimp.max=0;
                Intent intent=new Intent();
                intent.setClass(PublishedActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    new PopupWindows(PublishedActivity.this, noScrollgridview);
                } else {
                    Intent intent = new Intent(PublishedActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
        activity_selectimg_send.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

            if(publishContent.getText().toString().trim().equals("")&&Bimp.bmp == null){
                Toast.makeText(PublishedActivity.this,"请输入发表内容",Toast.LENGTH_SHORT).show();
                return;
            }else{
                PostSaying();
            }}
        });
        if(Constants.SAYING_TYPE!=null&&!Constants.SAYING_TYPE.equals("")){
            if(Constants.SAYING_TYPE.equals("2")){
                publishContent.setHint("发送消息向车友圈求助.");
            }
        }
    }

    //网络请求发表说说
    private void PostSaying() {
        Map map = new HashMap();
        UserUtil.instance(PublishedActivity.this);
        String userId = UserUtil.getInstance().getIntegerConfig("userId") + "";
//        Log.e("userId", userId);
        map.put("userId", userId);
        map.put("postAddress", location_address_publish.getText().toString().trim());
        map.put("sayingContent", publishContent.getText().toString().trim());
        map.put("sayingType", Constants.SAYING_TYPE);
        if (Bimp.bmp != null && Bimp.bmp.size() != 0) {
            for (int i = 0; i < Bimp.bmp.size(); i++) {
                Bitmap bitmap = Bimp.bmp.get(i);
                byte[] bytes;
                bytes = ImageUtil.getBitmapByte(bitmap);
                String myImg = "img" + (i + 1);
                String imgTemp = ImageUtil.getStringFromByte(bytes);
                Log.e("PostSaying", myImg + "----" + imgTemp);
                map.put(myImg, imgTemp);
            }
        }
        String url = Constants.ADD_SAYING;
        VolleyRequest.RequestPost(PublishedActivity.this, url, "PostSaying", map, new VolleyInterface(PublishedActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
                Bimp.bmp.clear();
                Bimp.max=0;
                Bimp.drr.clear();
                FileUtils.deleteDir();
//                Intent intent = new Intent();
//                intent.setClass(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                finish();
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(PublishedActivity.this, "网络异常，请重新发表", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            final int coord = position;
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.carlife_itempublished_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.ss_06));
                if (position == 6) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.bmp.get(position));
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            try {
                                String path = Bimp.drr.get(Bimp.max);
                                System.out.println(path);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    public class PopupWindows extends PopupWindow {
        public PopupWindows(Context mContext, View parent) {
            View view = View
                    .inflate(mContext, R.layout.carlife_item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();
            //相机按钮
            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);

            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);

            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(PublishedActivity.this,
                            TestPicActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    //相机功能
    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/myimage/", String.valueOf(System.currentTimeMillis())
                + ".jpg");
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.drr.size() < 6 && resultCode == -1) {
                    Bimp.drr.add(path);
                }
                break;
        }
    }

    //获取当前位置
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        //option.setPoiExtraInfo(true);
        if (true) {
            option.setAddrType("all");
        }
        // option.setScanSpan(3000);
        //  option.setPoiNumber(10);
        //option.disableCache(true);
        mLocClient.setLocOption(option);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocClient.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocClient.stop();
    }

    @Override
    public void onDestroy() {
        mLocClient.stop();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            Intent intent=new Intent();
            intent.setClass(PublishedActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
