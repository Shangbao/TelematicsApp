package com.hangon.fragment.music;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.fd.ourapplication.R;
import com.hangon.bean.music.Music;
import com.hangon.common.Constants;
import com.hangon.common.MusicUtil;
import com.hangon.common.Topbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/4/4.
 */
public class MusicFragment extends Fragment implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        AdapterView.OnItemClickListener {

    View musicView;
    ListView songList;//歌曲列表List
    List<Music> list;//装载歌曲数据
    SeekBar musicSeekbar;//音乐播放进度条
    Button btnPlayModel,btnPrevious,btnPause,btnNext,btnStop;
    MusicAdapter musicAdapter;//音乐列表适配器
    MediaPlayer mediaPlayer;//音乐播放器
    TextView selectedSong,selectedSinger;

    private boolean isPlaying=true;//播放状态
    private static int currIndex=0;//当前播放的索引

    int playMode=Constants.SEQUENCE_MODEL;//控制播放模式

    private int state= Constants.IDLE;//播放状态

    private boolean flag=true;//标志

    ExecutorService es= Executors.newSingleThreadExecutor();// 单线程的执行器

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        musicView=inflater.inflate(R.layout.fragment_music,container,false);
        init();
        setMusicAdapter();
        start();
        return musicView;
    }

    //初始化组件
    private void init(){
        mediaPlayer=new MediaPlayer();
        songList= (ListView) musicView.findViewById(R.id.songList);
        btnPlayModel= (Button) musicView.findViewById(R.id.btnPlayModel);
        btnPrevious= (Button) musicView.findViewById(R.id.btnPrevious);
        btnPause= (Button) musicView.findViewById(R.id.btnPause);
        btnNext= (Button) musicView.findViewById(R.id.btnNext);
        btnStop= (Button) musicView.findViewById(R.id.btnStop);
        musicSeekbar= (SeekBar) musicView.findViewById(R.id.musicSeekbar);
        selectedSinger= (TextView) musicView.findViewById(R.id.selectedSinger);
        selectedSong= (TextView) musicView.findViewById(R.id.selectedSong);
        btnPlayModel.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        musicSeekbar.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        songList.setOnItemClickListener(this);
        Topbar topbar= (Topbar) musicView.findViewById(R.id.topbar);
        topbar.setBtnIsVisible(false);
    }

    //获取音乐歌曲列表并添加适配器
    private  void setMusicAdapter(){
        list=new ArrayList<Music>();
        list= MusicUtil.getMusicData(getActivity());
        musicAdapter=new MusicAdapter(list,getActivity(),currIndex);
        songList.setAdapter(musicAdapter);
    }

    @Override
    //按钮点击事件
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPlayModel:
                if (playMode==Constants.SEQUENCE_MODEL){
                    btnPlayModel.setText("随机");
                    playMode=Constants.RANDOM_MODEL;
                }else if(playMode==Constants.RANDOM_MODEL){
                    btnPlayModel.setText("循环");
                    playMode=Constants.CIRCULATION_MODEL;
                }else if (playMode==Constants.CIRCULATION_MODEL){
                    btnPlayModel.setText("顺序");
                    playMode=Constants.SEQUENCE_MODEL;
                }
                break;
            case R.id.btnPrevious:
                playPrevious();
                btnPause.setText("暂停");
                isPlaying = false;
                break;
            case R.id.btnPause:
                if (isPlaying == true) {
                    play();
                    btnPause.setText("暂停");
                    isPlaying = false;
                } else if (isPlaying == false) {
                    btnPause.setText("播放");
                    isPlaying = true;
                    pause();
                }
                break;
            case R.id.btnNext:
                btnPause.setText("暂停");
                isPlaying = false;
                playNext();
                break;
            case R.id.btnStop:
                stop();
                btnPause.setText("播放");
                isPlaying = true;
                break;
        }
    }

    //播放上一曲
    // 播放上一曲
    private void playPrevious() {
        if(playMode==Constants.SEQUENCE_MODEL){
            if ((currIndex - 1) >= 0) {
                currIndex--;
            } else {
                currIndex = list.size() - 1;
            }
            start();
        }else if (playMode==Constants.RANDOM_MODEL){
            currIndex=(int)(Math.random()*list.size());
            start();
        }else if (playMode==Constants.CIRCULATION_MODEL){
            start();
        }
    }

    // 播放下一曲
    private void playNext() {
        if (playMode==Constants.SEQUENCE_MODEL){
            if ((currIndex + 1) < list.size()) {
                currIndex++;
            } else {
                currIndex = 0;
            }
            start();
        }else if (playMode==Constants.RANDOM_MODEL){
            currIndex=(int)(Math.random()*list.size());
            start();
        }else if (playMode==Constants.CIRCULATION_MODEL){
            start();
        }
    }

    // 播放与暂停
    // ---从上一次状态开始播放
    // 从上一次的播放状态开始播放
    private void play() {
        if (state == Constants.PAUSE) {
            mediaPlayer.start();
        } else {
            start();
        }
        state = Constants.PLAY;
    }

    // 停止播放
    private void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            state = Constants.STOP;
            flag = flag;
        }
    }

    private void pause() {
        mediaPlayer.pause();
        state = Constants.PAUSE;
    }

    private void start() {
        if (currIndex < list.size()) {
            Music m = list.get(currIndex);
            selectedSinger.setText(m.getSinger());
            selectedSong.setText(m.getTitle());
            try {
                mediaPlayer.reset();// 让播放器回到空闲
                mediaPlayer.setDataSource(m.getUrl());// 设置文件播放的路径
                mediaPlayer.prepare();
                mediaPlayer.start();// 开始播放
                initSeekBar();
                es.execute(new seekBar());
                musicAdapter.setCurrIndex(currIndex);
                musicAdapter.notifyDataSetChanged();
            } catch (Exception e) {
            }
        }
    }

    // 初始化进度条
    private void initSeekBar() {
        musicSeekbar.setMax(mediaPlayer.getDuration());// 设置进度条的总进度
        musicSeekbar.setProgress(0);
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
                    musicSeekbar.setProgress(mediaPlayer.getCurrentPosition());
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

    @Override
    // 监听网络媒体播放结束
    public void onCompletion(MediaPlayer mp) {
        playNext();
    }

    @Override
    //音频播放错误的时候
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mediaPlayer.reset();
        return false;
    }

    @Override
    //音频  释放音乐歌曲列表选项
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currIndex = (int) id;
        play();
    }

    @Override

    //歌曲进度条发生改变时触发
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    //触摸进度条触发
    public void onStartTrackingTouch(SeekBar seekBar) {
        pause();
    }

    @Override
    //离开进度条触发
    public void onStopTrackingTouch(SeekBar seekBar) {
        play();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        flag=false;
    }
}
