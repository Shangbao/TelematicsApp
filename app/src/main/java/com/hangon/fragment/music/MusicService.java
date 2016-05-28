package com.hangon.fragment.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.SeekBar;

import com.hangon.bean.music.Music;
import com.hangon.common.Constants;
import com.hangon.common.MusicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Chuan on 2016/5/14.
 */
public class MusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private MediaPlayer mediaPlayer = new MediaPlayer();//音乐播放器

    private int playMode = Constants.SEQUENCE_MODEL;//控制播放模式
    private int currIndex = 0;//当前播放的索引
    private List<Music> list;
    private int state = Constants.IDLE;

    private MyBinder myBinder = new MyBinder();

    private int currentPosition;

    private boolean flag;

    ExecutorService es = Executors.newSingleThreadExecutor();// 单线程的执行器


    private static final int updateProgress = 1;
    private static final int updateCurrentMusic = 2;
    private static final int updateDuration = 3;

    public static final String ACTION_UPDATE_PROGRESS = "com.hangon.fragment.music.UPDATE_PROGRESS";
    public static final String ACTION_UPDATE_DURATION = "com.hangon.fragment.music.UPDATE_DURATION";
    public static final String ACTION_UPDATE_CURRENT_MUSIC = "com.hangon.fragment.music.UPDATE_CURRENT_MUSIC";

    //向MusicFragment传递消息
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                //更新进度
                case updateProgress:
                    toUpdateProgress();
                    break;
                //更新歌曲长度
                case updateDuration:
                    toUpdateDuration();
                    break;
                //更新歌曲
                case updateCurrentMusic:
                    toUpdateCurrentMusic();
                    break;
            }
        }
    };

    // 播放上一曲
    private void playPrevious() {
        if (playMode == Constants.SEQUENCE_MODEL) {
            if ((currIndex - 1) >= 0) {
                currIndex--;
            } else {
                currIndex = list.size() - 1;
            }
            start(currIndex);
        } else if (playMode == Constants.RANDOM_MODEL) {
            currIndex = (int) (Math.random() * list.size());
            start(currIndex);
        } else if (playMode == Constants.CIRCULATION_MODEL) {
            start(currIndex);
        }
    }

    // 播放下一曲
    private void playNext() {
        if (playMode == Constants.SEQUENCE_MODEL) {
            if ((currIndex + 1) < list.size()) {
                currIndex++;
            } else {
                currIndex = 0;
            }
            start(currIndex);
        } else if (playMode == Constants.RANDOM_MODEL) {
            currIndex = (int) (Math.random() * list.size());
            start(currIndex);
        } else if (playMode == Constants.CIRCULATION_MODEL) {
            start(currIndex);
        }
    }

    // 播放与暂停
    // 从上一次的播放状态开始播放
    private void play(int currIndex) {
        if (state == Constants.PAUSE) {
            mediaPlayer.start();
        } else {
            start(currIndex);
        }
        state = Constants.PLAY;
    }

    // 停止播放
    private void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            state = Constants.STOP;
        }
    }

    //暂停播放
    private void pause() {
        mediaPlayer.pause();
        state = Constants.PAUSE;
    }

    //开始播放
    private void start(int currIndex) {
        if (currIndex < list.size()) {
            Music m = list.get(currIndex);
            try {
                mediaPlayer.reset();// 让播放器回到空闲
                mediaPlayer.setDataSource(m.getUrl());// 设置文件播放的路径
                mediaPlayer.prepare();
                mediaPlayer.start();// 开始播放
                initSeekBar();
                handler.sendEmptyMessage(updateCurrentMusic);
                handler.sendEmptyMessage(updateDuration);
                es.execute(new seekBar());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 实现播放进度
    class seekBar implements Runnable {
        public seekBar() {
            flag = true;
        }

        @Override
        public void run() {
            while (flag) {
                if (mediaPlayer.getCurrentPosition() < mediaPlayer
                        .getDuration()) {
                    handler.sendEmptyMessage(updateProgress);
                } else {
                    flag = false;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initSeekBar() {
        currentPosition = 0;
    }

    private void toUpdateProgress() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            int progress = mediaPlayer.getCurrentPosition();
            Intent intent = new Intent();
            intent.setAction(ACTION_UPDATE_PROGRESS);
            intent.putExtra(ACTION_UPDATE_PROGRESS, progress);
            sendBroadcast(intent);
            handler.sendEmptyMessageDelayed(updateProgress, 1000);
        }
    }

    private void toUpdateDuration() {
        if (mediaPlayer != null) {
            int duration = mediaPlayer.getDuration();
            Intent intent = new Intent();
            intent.setAction(ACTION_UPDATE_DURATION);
            intent.putExtra(ACTION_UPDATE_DURATION, duration);
            sendBroadcast(intent);
        }
    }

    private void toUpdateCurrentMusic() {
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_CURRENT_MUSIC);
        intent.putExtra(ACTION_UPDATE_CURRENT_MUSIC, currIndex);
        sendBroadcast(intent);
    }

    private void outSetPlayMode(int playMode) {
        this.playMode = playMode;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        mediaPlayer.reset();
        return false;
    }

    class MyBinder extends Binder {
        public void startPlay(int currIndex) {
            play(currIndex);
        }

        public void stopPlay() {
            stop();
        }

        public void toNext() {
            playNext();
        }

        public void toPrevious() {
            playPrevious();
        }

        public void toPause() {
            pause();
        }

        public void toStart(int id) {
            currIndex = id;
            start(currIndex);
        }

        public void changeProgress(int progress) {
            if (mediaPlayer != null) {
                currentPosition = progress * 1000;
                mediaPlayer.seekTo(currentPosition);
            }
        }

        public void setPlayMode(int playMode) {
            outSetPlayMode(playMode);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        list = new ArrayList<Music>();
        list = MusicUtil.getMusicData(MusicService.this);
        start(currIndex);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }
}
