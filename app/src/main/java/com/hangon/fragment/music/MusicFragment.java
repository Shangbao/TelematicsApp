package com.hangon.fragment.music;

import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
        SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener {

    View musicView;
    ListView songList;//歌曲列表List
    List<Music> list;//装载歌曲数据
    SeekBar musicSeekbar;//音乐播放进度条
    Button btnPlayModel,btnPrevious,btnPause,btnNext,btnStop;
    MusicAdapter musicAdapter;//音乐列表适配器
    TextView selectedSong,selectedSinger;
    MusicImage musicImage;
    MusicService.MyBinder myBinder;

    private int currentPosition;
    private int currentMax;

    Intent intent;

    private ProgressReceiver progressReceiver;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(getActivity(), "音乐播放器打开失败！", Toast.LENGTH_SHORT).show();
        }
    };

    private boolean isPlaying=true;//播放状态
    private static int currIndex = 0;//当前播放的索引

    int playMode=Constants.SEQUENCE_MODEL;//控制播放模式

    private int state= Constants.IDLE;//播放状态

    private boolean flag=true;//标志

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        musicView=inflater.inflate(R.layout.fragment_music,container,false);
        init();
        intent = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(intent, conn, Service.BIND_AUTO_CREATE);
        setMusicAdapter();
        registerReceiver();
        return musicView;
    }

    //初始化组件
    private void init(){
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
        songList.setOnItemClickListener(this);
        musicImage= (MusicImage) musicView.findViewById(R.id.personIcon);
        Topbar topbar= (Topbar) musicView.findViewById(R.id.topbar);
        topbar.setBtnIsVisible(false);
        Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.music_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        musicImage.startAnimation(operatingAnim);
    }

    private void registerReceiver(){
        progressReceiver = new ProgressReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.ACTION_UPDATE_PROGRESS);
        intentFilter.addAction(MusicService.ACTION_UPDATE_DURATION);
        intentFilter.addAction(MusicService.ACTION_UPDATE_CURRENT_MUSIC);
        getActivity().registerReceiver(progressReceiver, intentFilter);
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
                myBinder.setPlayMode(playMode);
                break;
            case R.id.btnPrevious:
                myBinder.toPrevious();
                btnPause.setText("暂停");
                isPlaying = false;
                break;
            case R.id.btnPause:
                if (isPlaying == true) {
                    myBinder.startPlay(currIndex);
                    btnPause.setText("暂停");
                    isPlaying = false;
                } else if (isPlaying == false) {
                    btnPause.setText("播放");
                    isPlaying = true;
                    myBinder.toPause();
                }
                break;
            case R.id.btnNext:
                btnPause.setText("暂停");
                isPlaying = false;
                myBinder.toNext();
                break;
            case R.id.btnStop:
                myBinder.stopPlay();
                btnPause.setText("播放");
                isPlaying = true;
                break;
        }
    }

    @Override
    //音频  释放音乐歌曲列表选项
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currIndex = (int)id;
        myBinder.toStart(currIndex);
    }

    //歌曲进度条发生改变时触发
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            myBinder.changeProgress(progress);
        }
    }

    @Override
    //触摸进度条触发
    public void onStartTrackingTouch(SeekBar seekBar) {
        myBinder.toPause();
    }

    @Override
    //离开进度条触发
    public void onStopTrackingTouch(SeekBar seekBar) {
        myBinder.startPlay(currIndex);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(conn);
        getActivity().stopService(intent);
        flag=false;
    }

    class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(MusicService.ACTION_UPDATE_PROGRESS.equals(action)){
                int progress = intent.getIntExtra(MusicService.ACTION_UPDATE_PROGRESS, 0);
                if(progress > 0){
                    currentPosition = progress; // Remember the current position
                    musicSeekbar.setProgress(progress / 1000);
                }
            }else if(MusicService.ACTION_UPDATE_CURRENT_MUSIC.equals(action)){
                //Retrive the current music and get the title to show on top of the screen.
                currIndex = intent.getIntExtra(MusicService.ACTION_UPDATE_CURRENT_MUSIC, 0);
                selectedSong.setText(list.get(currIndex).getTitle());
                selectedSinger.setText(list.get(currIndex).getSinger());
                musicAdapter.setCurrIndex(currIndex);
                musicAdapter.notifyDataSetChanged();

            }else if(MusicService.ACTION_UPDATE_DURATION.equals(action)){
                //Receive the duration and show under the progress bar
                //Why do this ? because from the ContentResolver, the duration is zero.
                currentMax = intent.getIntExtra(MusicService.ACTION_UPDATE_DURATION, 0);
                int max = currentMax / 1000;
                musicSeekbar.setMax(currentMax / 1000);
            }
        }
    }
}
