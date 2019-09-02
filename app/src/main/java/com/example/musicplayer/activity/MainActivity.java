package com.example.musicplayer.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.MusicAdapter;
import com.example.musicplayer.bean.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : PengLiang
 * Time : 2019/8/26
 * Description : 音乐列表显示的活动，主活动
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MusicAdapter adapter;
    private List<Music> list_music = new ArrayList<>();
    private Toolbar toolbar;
    private TextView mSongSizeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置状态栏为透明，且背景与状态栏融合
//        if (Build.VERSION.SDK_INT >= 21) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
        // 设置状态栏底色白色, 设置状态栏字体黑色
        setLightMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.main_toolbar);
        recyclerView = findViewById(R.id.rv_main_rcy);
        list_music = Music.getMusicList(MainActivity.this);
        adapter = new MusicAdapter(list_music);
        mSongSizeTv = findViewById(R.id.tv_main_song_size);
        mSongSizeTv.setText("(共" + list_music.size() + "首)");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // 设置菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    // 菜单的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.main_search:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_scan_music:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_select_range:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_get_music_word:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_grade_music:
                Toast.makeText(this, "此功能暂未开发", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    /**
     * Android 6.0 以上设置状态栏颜色
     */
    private void setLightMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 设置状态栏底色白色
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.WHITE);

            // 设置状态栏字体黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }
}
