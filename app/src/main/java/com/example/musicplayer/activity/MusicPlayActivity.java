package com.example.musicplayer.activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.bean.Music;
import com.example.musicplayer.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : PengLiang
 * Time : 2019/8/26
 * Description : 音乐播放界面的活动
 */
public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSelectSongBtn;
    private Button mShareBtn;
    private Button mDownloadBtn;
    private Button mTianBtn;
    private Button mMsgBtn;
    private Button mLotBtn;
    private Button mBackBtn;
    private Button mControlMusicBtn;
    private Button mNextBtn;
    private Button mPreviousBtn;
    private Button mLoveBtn;
    private ImageButton mPlayBtn;
    private TextView mSongNameTv;
    private TextView mSongDurationTv;
    private TextView mSongCurrentTimeTv;
    private TextView mSingerTv;
    private SeekBar mSongSb;
    private MediaPlayer mediaPlayer;
    private List<Music> musicList = new ArrayList<>();
    private ExecutorService es = Executors.newSingleThreadExecutor(); //定义线程池（同时只能有一个线程运行）
    private LoadProgress loadProgress;
    private int sbCurrentProgress;
    private int sbSaveProgress = 0; // 进度条改变时，保存的进度
    private int index;
    private static final int UPDATE_PROGRESS = 1;
    // 爱心按钮的当前状态
    private static final int STATE_LOVE_NONE = 5;
    private static final int STATE_LOVE_CLICKED = 6;
    private int currLoveState = STATE_LOVE_NONE;
    // 定义当前播放器的状态
    private static final int IDLE = 0;
    private static final int PAUSE = 1;
    private static final int START = 2;
    private static final int CURR_TIME_VALUE = 1;
    private int currState = IDLE; // 当前播放器的状态
    // 控制音乐按钮状态
    private static final int MUSIC_SINGLE_WHILE = 10; // 单曲循环
    private static final int MUSIC_LIST_PLAY = 11;  // 列表循环
    private static final int MUSIC_RANDOM_PLAY = 12;// 随机播放
    private int currMusicControlState = MUSIC_LIST_PLAY;

    // 监听音乐按钮状态
    private static final int SINGLE_WHILE = 21; // 单曲循环
    private static final int LIST_PLAY = 22;  // 列表循环
    private static final int RANDOM_PLAY = 23;// 随机播放
    private int currControlState = LIST_PLAY;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    mSongSb.setProgress(sbCurrentProgress);
                    mSongCurrentTimeTv.setText(getMusicTime(sbCurrentProgress));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置状态栏为透明，且背景与状态栏融合
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_music_play);
        initViews();
        loadIntent();
        // 设置播放完当前歌曲，自动切换到下一首歌曲
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                switch (currControlState) {
                    case SINGLE_WHILE:  //单曲循环
                        play();
                        break;
                    case RANDOM_PLAY:  // 随机播放
                        Random random = new Random();
                        index = random.nextInt(musicList.size());
                        play();
                        break;
                    case LIST_PLAY:  // 列表循环
                        if (index + 1 < musicList.size()) {
                            next();
                        } else {
                            index = 0;
                            play();
                        }
                        break;
                }
            }
        });
    }

    //播放音乐
    public void play() {
        if (musicList.size() > 0 && index < musicList.size()) {
            mediaPlayer.reset();
            try {
                Music music = musicList.get(index);
                mediaPlayer.setDataSource(music.getUrl());   //设置播放音乐资源文件对象
                mediaPlayer.prepare();      // 准备
                mediaPlayer.start(); // 开始播放
                initSeekBar();  // 初始化SeekBar
                es.execute(loadProgress);   // 启动控制SeekBar的线程
                mSongNameTv.setText(music.getTitle()); // 设置歌曲名字
                mSingerTv.setText(music.getArtist());  // 设置歌曲的作者
                mSongDurationTv.setText(getMusicTime(music.getDuration())); // 设置歌曲的时长
                currState = PAUSE;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "播放列表为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void initSeekBar() {
        mSongSb.setMax(mediaPlayer.getDuration());
        mSongSb.setProgress(0);
    }

    // 将得到的毫秒时间转换成分秒形式的时间
    private String getMusicTime(long duration) {
        long data_time;
        String time = "00:00";
        if ((data_time = duration) > 0) {
            time = TimeUtils.getTime(data_time, TimeUtils.DEFAULT_MIN_FORMAT);
            return time;
        }
        return time;
    }

    // 加载上一个活动传过来的参数
    private void loadIntent() {
        Bundle bundle = getIntent().getExtras();
        index = 0;
        if (bundle != null) {
            index = bundle.getInt("index");
        }
        // 播放
        play();
    }

    private void initViews() {
        mSelectSongBtn = findViewById(R.id.btn_music_play_select_music);
        mShareBtn = findViewById(R.id.btn_music_play_share);
        mDownloadBtn = findViewById(R.id.btn_music_play_download);
        mTianBtn = findViewById(R.id.btn_music_play_tian);
        mMsgBtn = findViewById(R.id.btn_music_play_msg);
        mLotBtn = findViewById(R.id.btn_music_play_lot);
        mControlMusicBtn = findViewById(R.id.btn_music_play_control_music);
        mPreviousBtn = findViewById(R.id.btn_music_play_previous);
        mPlayBtn = findViewById(R.id.btn_music_play_play);
        mNextBtn = findViewById(R.id.btn_music_play_next);
        mBackBtn = findViewById(R.id.btn_music_play_back);
        mLoveBtn = findViewById(R.id.btn_music_play_love);
        mSingerTv = findViewById(R.id.tv_music_play_singer);
        mSongDurationTv = findViewById(R.id.tv_music_play_song_duration);
        mSongCurrentTimeTv = findViewById(R.id.tv_music_play_song_current_time);
        mSongNameTv = findViewById(R.id.tv_music_play_song_name);
        mSongSb = findViewById(R.id.sb_music_play_sb);
        // 设置监听
        mPlayBtn.setOnClickListener(this);
        mPreviousBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mControlMusicBtn.setOnClickListener(this);
        mSelectSongBtn.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);
        mLoveBtn.setOnClickListener(this);
        mDownloadBtn.setOnClickListener(this);
        mTianBtn.setOnClickListener(this);
        mMsgBtn.setOnClickListener(this);
        mLotBtn.setOnClickListener(this);

        musicList = Music.getMusicList(this);
        loadProgress = new LoadProgress();
        mediaPlayer = new MediaPlayer();
        //设置音乐无限循环播放
//        mediaPlayer.setLooping(true);

        mSongSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sbSaveProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 解决音乐暂停时拖动SeekBar无法刷新当前时间的问题
                mSongCurrentTimeTv.setText(getMusicTime(sbSaveProgress));
                seekBar.setProgress(sbSaveProgress);
                mediaPlayer.seekTo(sbSaveProgress);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_music_play_play:
                btnPlay();
                break;
            case R.id.btn_music_play_previous:
                previous();
                break;
            case R.id.btn_music_play_next:
                next();
                break;
            case R.id.btn_music_play_back:
                finish();
                break;
            case R.id.btn_music_play_control_music:
                controlMusic();
                break;
            case R.id.btn_music_play_select_music:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_music_play_share:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_music_play_love:
                clickLove();
                break;
            case R.id.btn_music_play_download:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_music_play_tian:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_music_play_msg:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_music_play_lot:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // 音乐控制按钮
    private void controlMusic() {
        switch (currMusicControlState) {
            case MUSIC_SINGLE_WHILE:
                mControlMusicBtn.setBackgroundResource(R.drawable.selector_music_play_btn_list);
                Toast.makeText(this, "列表播放", Toast.LENGTH_SHORT).show();
                currMusicControlState = MUSIC_LIST_PLAY;
                currControlState = LIST_PLAY;
                break;
            case MUSIC_LIST_PLAY:
                mControlMusicBtn.setBackgroundResource(R.drawable.ic_music_play_btn_random);
                Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
                currMusicControlState = MUSIC_RANDOM_PLAY;
                currControlState = RANDOM_PLAY;
                break;
            case MUSIC_RANDOM_PLAY:
                mControlMusicBtn.setBackgroundResource(R.drawable.ic_music_play_btn_single_while);
                Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
                currMusicControlState = MUSIC_SINGLE_WHILE;
                currControlState = SINGLE_WHILE;
                break;
        }
    }

    // 爱心按钮
    private void clickLove() {
        switch (currLoveState) {
            case STATE_LOVE_NONE:
                mLoveBtn.setBackgroundResource(R.drawable.ic_music_play_red_love);
                currLoveState = STATE_LOVE_CLICKED;
                break;
            case STATE_LOVE_CLICKED:
                mLoveBtn.setBackgroundResource(R.drawable.ic_music_play_love);
                currLoveState = STATE_LOVE_NONE;
                break;
        }
    }

    // 播放按钮
    private void btnPlay() {
        switch (currState) {
            case IDLE:
                play();
                break;
            case PAUSE:
                mediaPlayer.pause();
                currState = START;
                mPlayBtn.setBackgroundResource(R.drawable.ic_music_play_pause);
                break;
            case START:
                mediaPlayer.start();
                es.execute(loadProgress); // 开始播放时，恢复进度条和当前时间
                currState = PAUSE;
                mPlayBtn.setBackgroundResource(R.drawable.ic_music_play_play);
        }
    }

    // 播放下一首
    private void next() {
        if (index + 1 < musicList.size()) {
            mPlayBtn.setBackgroundResource(R.drawable.ic_music_play_play);
            index++;
            play();
        } else {
            Toast.makeText(this, "当前已经是最后一首歌了", Toast.LENGTH_SHORT).show();
        }
    }

    // 播放上一首
    private void previous() {
        if (index - 1 >= 0) {
            mPlayBtn.setBackgroundResource(R.drawable.ic_music_play_play);
            index--;
            play();
        } else {
            Toast.makeText(this, "当前已经是第一首歌了", Toast.LENGTH_SHORT).show();
        }
    }

    // 控制SeekBar进度的线程
    class LoadProgress implements Runnable {
        @Override
        public void run() {
            while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                sbCurrentProgress = mediaPlayer.getCurrentPosition();
                Message message = new Message();
                message.what = UPDATE_PROGRESS;
                handler.sendMessage(message);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        super.onDestroy();
    }
}
