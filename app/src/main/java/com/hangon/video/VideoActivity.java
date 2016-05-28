package com.hangon.video;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;

/**
 * Created by Chuan on 2016/5/28.
 */
public class VideoActivity extends Activity implements View.OnClickListener {

    ImageButton record, stop;
    //系统的视频文件
    File videoFile;
    MediaRecorder mRecorder;
    //显示视频预览的SurfaceView
    SurfaceView sView;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {

    }
}
