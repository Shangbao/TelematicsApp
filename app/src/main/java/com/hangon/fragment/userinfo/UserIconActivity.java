package com.hangon.fragment.userinfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.ImageUtil;
import com.hangon.common.Topbar;
import com.hangon.common.UserUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.home.activity.HomeActivity;

import javax.security.auth.login.LoginException;


public class UserIconActivity extends Activity implements OnClickListener {
    private final int START_ALBUM_REQUESTCODE = 1;
    private final int CAMERA_WITH_DATA = 2;
    private final int CROP_RESULT_CODE = 3;
    public static final String TMP_PATH = "clip_temp.jpg";
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_icon);


        findViewById(R.id.albumBtn).setOnClickListener(this);
        findViewById(R.id.captureBtn).setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);


        Topbar topbar= (Topbar) findViewById(R.id.userIconTopbar);
       topbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
           @Override
           public void leftClick() {

               Intent intent = new Intent();
               intent.putExtra("id", 4);
               intent.setClass(UserIconActivity.this, HomeActivity.class);
               startActivity(intent);
               UserIconActivity.this.finish();
           }

           @Override
           public void rightClick() {

           }
       });
        topbar.setRightIsVisible(false);
        getUserIconFromCookies();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.albumBtn) {
            startAlbum();
        } else if (v.getId() == R.id.captureBtn) {
            startCapture();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // String result = null;
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CROP_RESULT_CODE:
                String path = data.getStringExtra(ClipImageActivity.RESULT_PATH);
                Log.e("xxxx",path);
                Bitmap photo = BitmapFactory.decodeFile(path);
                if(photo!=null){
                    saveIconToCookies(photo);
                    postIconBytes(photo);
                }

                 getUserIconFromCookies();
                break;
            case START_ALBUM_REQUESTCODE:
                startCropImageActivity(getFilePath(data.getData()));
                break;
            case CAMERA_WITH_DATA:
                startCropImageActivity(Environment.getExternalStorageDirectory()
                        + "/" + TMP_PATH);
                break;
        }
    }

    //获取内存里面的图片信息
    private void getUserIconFromCookies(){
        UserUtil.instance(UserIconActivity.this);
        String s=UserUtil.getInstance().getStringConfig("userIconContent");
        if(s==null||s.equals("")){
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
            Bitmap bitmap1=ImageUtil.getRoundedCornerBitmap(bitmap,100);
            imageView.setImageBitmap(bitmap1);
        }else {
            byte[] bytes=ImageUtil.getStringByte(s);
            Bitmap bitmap=ImageUtil.getBitmapFromByte(bytes);
            Bitmap bitmap1=ImageUtil.getRoundedCornerBitmap(bitmap,100);
            imageView.setImageBitmap(bitmap);
        }
    }

    //把获取的图片保存到cookies里面
    private void saveIconToCookies(Bitmap bitmap){
        byte[] bytes;
        bytes=ImageUtil.getBitmapByte(bitmap);
    String userIconConten= ImageUtil.getStringFromByte(bytes);
        UserUtil.instance(UserIconActivity.this);
        UserUtil.getInstance().saveStringConfig("userIconContent",userIconConten);
    }

    //发送图片的二进制码
    private void postIconBytes(Bitmap bitmap){
       String url= Constants.ADD_USER_ICON_URL;
        Map<String,Object> map=new HashMap<String,Object>();
        UserUtil.instance(UserIconActivity.this);
       String userName= UserUtil.getInstance().getStringConfig("userName");
        map.put("userName",userName);
        byte[] bytes;
        bytes=ImageUtil.getBitmapByte(bitmap);
        String userIconContent=ImageUtil.getStringFromByte(bytes);
        map.put("userIconContent",userIconContent);

        VolleyRequest.RequestPost(UserIconActivity.this, url, "potsUserIcon", map, new VolleyInterface(UserIconActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {

            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }


    // 裁剪图片的Activity
    private void startCropImageActivity(String path) {
        ClipImageActivity.startActivity(this, path, CROP_RESULT_CODE);
    }

    private void startAlbum() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image/*");
            startActivityForResult(intent, START_ALBUM_REQUESTCODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, START_ALBUM_REQUESTCODE);
            } catch (Exception e2) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }


    private void startCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), TMP_PATH)));
        startActivityForResult(intent, CAMERA_WITH_DATA);
    }

    /**
     *
     *
     * @param mUri
     * @return
     */
    public String getFilePath(Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return mUri.getPath();
            } else {
                return getFilePathByUri(mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * 通过uri获取文件路径
     *
     * @param mUri
     * @return
     */
    private String getFilePathByUri(Uri mUri) throws FileNotFoundException {
        Cursor cursor = getContentResolver()
                .query(mUri, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

}
