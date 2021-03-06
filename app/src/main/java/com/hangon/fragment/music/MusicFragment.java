package com.hangon.fragment.music;

import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.hangon.bean.music.Mp3;
import com.hangon.bean.music.Music;
import com.hangon.common.Constants;
import com.hangon.common.MusicUtil;
import com.hangon.common.Topbar;
import com.hangon.home.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/4/4.
 */
public class MusicFragment extends Fragment implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener{

    View musicView;
    ListView songList;//歌曲列表List
    List<Music> list;//装载歌曲数据

    //List<Mp3> mp3List;
    SeekBar musicSeekbar;//音乐播放进度条
    RelativeLayout btnPlayModel;
    ImageButton btnPrevious, btnPause, btnNext;
    ImageView playModelImg;//播放模式的图片
    TextView playModelTxt;//播放模式的文字
    MusicAdapter musicAdapter;//音乐列表适配器
    ImageView singerPicture;

   // Mp3Adapter mp3Adapter;
    TextView selectedSong, selectedSinger;
    MusicService.MyBinder myBinder;

    ImageView topbarLeft, topbarRight;
    TextView topbarTitle;
    TextView currTime, totalTime;

    private int currentPosition;//当前音乐播放位置
    private int currentMax;//播放条的最大位置

    Intent intent;

    private ProgressReceiver progressReceiver;

    private boolean isPlaying = false;//播放状态
    private int currIndex = 0;//当前播放的索引

    int playMode = Constants.SEQUENCE_MODEL;//控制播放模式
    int mStartItem;//音乐播放列表窗口可见的第一项
    int mEndItem;//音乐播放列表窗口可见的最后项

    BackClickListener listener;
    //音乐通信接口
    public interface BackClickListener{
        public void backClick(int position);
    };

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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        musicView = inflater.inflate(R.layout.fragment_music, container, false);
        init();
        intent = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(intent, conn, Service.BIND_AUTO_CREATE);
        setMusicAdapter();
        registerReceiver();
        return musicView;
    }

    //初始化组件
    private void init() {

        songList = (ListView) musicView.findViewById(R.id.songList);
        btnPlayModel = (RelativeLayout) musicView.findViewById(R.id.btnPlayModel);
        btnPrevious = (ImageButton) musicView.findViewById(R.id.btnPrevious);
        btnPause = (ImageButton) musicView.findViewById(R.id.btnPause);
        btnNext = (ImageButton) musicView.findViewById(R.id.btnNext);
        singerPicture= (ImageView) musicView.findViewById(R.id.img2);


        playModelImg = (ImageView) musicView.findViewById(R.id.playModelImg);
        playModelTxt = (TextView) musicView.findViewById(R.id.playModelTxt);
        currTime = (TextView) musicView.findViewById(R.id.currTime);
        totalTime = (TextView) musicView.findViewById(R.id.totleTime);

        musicSeekbar = (SeekBar) musicView.findViewById(R.id.musicSeekbar);
        selectedSinger = (TextView) musicView.findViewById(R.id.selectedSinger);
        selectedSong = (TextView) musicView.findViewById(R.id.selectedSong);
        btnPlayModel.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        musicSeekbar.setOnSeekBarChangeListener(this);
        songList.setOnItemClickListener(this);

        topbarLeft = (ImageView) musicView.findViewById(R.id.topbar_left);

        topbarRight = (ImageView) musicView.findViewById(R.id.topbar_right);
        topbarTitle = (TextView) musicView.findViewById(R.id.topbar_title);
        topbarRight.setVisibility(View.GONE);
        topbarTitle.setText("我的音乐");

        mListItemScroll();//设置list的滚动

        topbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener= (BackClickListener) getActivity();
                listener.backClick(HomeActivity.FragmentIndex);
            }
        });
    }



    private void registerReceiver() {
        progressReceiver = new ProgressReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.ACTION_UPDATE_PROGRESS);
        intentFilter.addAction(MusicService.ACTION_UPDATE_DURATION);
        intentFilter.addAction(MusicService.ACTION_UPDATE_CURRENT_MUSIC);
        getActivity().registerReceiver(progressReceiver, intentFilter);
    }

    //获取音乐歌曲列表并添加适配器
    private void setMusicAdapter() {
        list = new ArrayList<Music>();
       // list = MusicUtil.getMusicData(getActivity());
        //musicAdapter = new MusicAdapter(list, getActivity(), currIndex);
        //songList.setAdapter(musicAdapter);
        list=MusicUtil.getMusicData(getActivity());
         musicAdapter=new MusicAdapter(list,getActivity(),currIndex);
        songList.setAdapter(musicAdapter);
    }

    @Override
    //按钮点击事件
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlayModel:
                if (playMode == Constants.SEQUENCE_MODEL) {
                    playModelImg.setImageResource(R.drawable.lj_yy_002);
                    playModelTxt.setText("随机播放");
                    playMode = Constants.RANDOM_MODEL;
                } else if (playMode == Constants.RANDOM_MODEL) {
                    playModelImg.setImageResource(R.drawable.lj_yy_003);
                    playModelTxt.setText("循环播放");
                    playMode = Constants.CIRCULATION_MODEL;
                    playMode = Constants.CIRCULATION_MODEL;
                } else if (playMode == Constants.CIRCULATION_MODEL) {
                    playModelTxt.setText("顺序播放");
                    playModelImg.setImageResource(R.drawable.lj_yy_001);
                    playMode = Constants.SEQUENCE_MODEL;
                }
                myBinder.setPlayMode(playMode);
                break;
            case R.id.btnPrevious:
                myBinder.toPrevious();
                btnPause.setBackgroundResource(R.drawable.lj_yy_005);
                isPlaying = false;
                break;
            case R.id.btnPause:
                if (isPlaying == true) {
                    myBinder.startPlay(currIndex);
                    btnPause.setBackgroundResource(R.drawable.lj_yy_005);
                    isPlaying = false;
                } else if (isPlaying == false) {
                    btnPause.setBackgroundResource(R.drawable.wdyy_34);
                    isPlaying = true;
                    myBinder.toPause();
                }
                break;
            case R.id.btnNext:
                btnPause.setBackgroundResource(R.drawable.lj_yy_005);
                isPlaying = false;
                myBinder.toNext();
                break;

        }
    }

    @Override
    //音频  释放音乐歌曲列表选项
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // currIndex = (int)id;
        myBinder.toStart((int) id);
        btnPause.setBackgroundResource(R.drawable.lj_yy_005);
        isPlaying = false;
    }

    //跳转
    private void skipSelected(int position) {
        if (position <= mStartItem) {
            songList.setSelection(position);
            //songList.smoothScrollBy(3,0);
            //songList.smoothScrollToPosition(position);
        } else if (position >= mEndItem - 1) {
            songList.setSelection(position);
            // songList.smoothScrollToPosition(5,0);
            //songList.smoothScrollToPosition(position);
        }
    }

    //歌曲进度条发生改变时触发
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            myBinder.changeProgress(progress);
            currTime.setText("");
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

    //歌曲列表滚动的时候，触发
    private void mListItemScroll() {
        songList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mStartItem = firstVisibleItem;
                mEndItem = firstVisibleItem + visibleItemCount;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(progressReceiver);
        getActivity().unbindService(conn);
    }



    class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MusicService.ACTION_UPDATE_PROGRESS.equals(action)) {
                int progress = intent.getIntExtra(MusicService.ACTION_UPDATE_PROGRESS, 0);
                currTime.setText(MusicUtil.toTime(progress));
                if (progress > 0) {
                    currentPosition = progress; // Remember the current position
                    musicSeekbar.setProgress(progress / 1000);
                }
            } else if (MusicService.ACTION_UPDATE_CURRENT_MUSIC.equals(action)) {
                //Retrive the current music and get the title to show on top of the screen.
                currIndex = intent.getIntExtra(MusicService.ACTION_UPDATE_CURRENT_MUSIC, 0);
                selectedSong.setText(list.get(currIndex).getName().replace(".mp3",""));
                selectedSinger.setText(list.get(currIndex).getSinger());
                Bitmap singerPic=list.get(currIndex).getPicture();
                if(null!=singerPic){
                    singerPicture.setImageBitmap(list.get(currIndex).getPicture());
                }
                totalTime.setText(MusicUtil.toTime((int) list.get(currIndex).getTime()));
                musicAdapter.setCurrIndex(currIndex);
                skipSelected(currIndex);
                musicAdapter.notifyDataSetChanged();

            } else if (MusicService.ACTION_UPDATE_DURATION.equals(action)) {
                //Receive the duration and show under the progress bar
                //Why do this ? because from the ContentResolver, the duration is zero.
                currentMax = intent.getIntExtra(MusicService.ACTION_UPDATE_DURATION, 0);
                totalTime.setText(MusicUtil.toTime(currentMax));
                int max = currentMax / 1000;
                musicSeekbar.setMax(currentMax / 1000);
            }
        }
    }

}
