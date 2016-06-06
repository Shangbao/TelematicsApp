package com.hangon.video;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.xys.libzxing.zxing.camera.CameraManager;

import java.io.File;

/**
 * Created by Chuan on 2016/5/28.
 */
public class VideoActivity extends Activity implements View.OnClickListener {

    ImageButton stop;
    MediaRecorder mRecorder;
    SurfaceView sView;

    public static final String ACTION_STOP = "com.hangon.fragment.userinfo.UserFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_main);
        stop = (ImageButton) findViewById(R.id.stop);
        sView = (SurfaceView) findViewById(R.id.sView);
        stop.setOnClickListener(this);
        // 设置Surface不需要维护自己的缓冲区
        sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置分辨率
        sView.getHolder().setFixedSize(1024, 600);
        // 设置该组件不会让屏幕自动关闭
        sView.getHolder().setKeepScreenOn(true);
        mRecorder = new MediaRecorder();
        // 设置从麦克风采集声音
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置从摄像头采集图像
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // 设置视频、音频的输出格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        // 设置音频的编码格式、
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        // 设置图像编码格式
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        //mRecorder.setVideoSize(320, 280);
        // mRecorder.setVideoFrameRate(5);
        // 指定SurfaceView来预览视频
        mRecorder.setPreviewDisplay(sView.getHolder().getSurface());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stop:
                Intent intent = new Intent();
                intent.setAction(ACTION_STOP);
                intent.putExtra(ACTION_STOP, "stop");
                sendBroadcast(intent);
                finish();
                break;
        }
    }
}
