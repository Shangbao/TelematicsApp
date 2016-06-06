package com.hangon.video;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chuan on 2016/5/28.
 */
public class VideoService extends Service {

    //系统的视频文件
    File videoFile;
    MediaRecorder mRecorder;
    SurfaceView sView;

    VideoBinder binder;

    private boolean isRecording = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new VideoBinder();
        return binder;
    }

    public void recordVideo(){
        if(!isRecording){
            try{
                //设置日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                //获取当前时间
                Date date = new Date();
                //创建保存录制视频的视频文件,并以录制时间命名
                videoFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/"+ df.format(date) + ".mp4");
                //创建MediaRecorder对象
                mRecorder = new MediaRecorder();
                mRecorder.reset();
                if (!videoFile.exists())
                    videoFile.createNewFile();
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
                mRecorder.setOutputFile(videoFile.getAbsolutePath());
                mRecorder.setPreviewDisplay(sView.getHolder().getSurface());
                mRecorder.prepare();
                // 开始录制
                mRecorder.start();
                isRecording = true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void stopVideo(){
        if(isRecording){
            //停止录制
            mRecorder.stop();
            //释放资源
            mRecorder.release();
            mRecorder = null;

            isRecording = false;
        }
    }

    public class VideoBinder extends Binder{
        public void record(){
            recordVideo();
        }

        public void stop(){
            stopVideo();
        }
    }
}
