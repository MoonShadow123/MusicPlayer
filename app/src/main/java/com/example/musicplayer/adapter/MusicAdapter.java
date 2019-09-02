package com.example.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.activity.MusicPlayActivity;
import com.example.musicplayer.bean.Music;

import java.util.List;

/**
 * @author : PengLiang
 * Time : 2019/8/26
 * Description : 音乐列表界面RecyclerView的适配器
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private Context mContext;
    private List<Music> list_music;

    public MusicAdapter(List<Music> list) {
        list_music = list;
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView singer;
        private TextView album;
        private View ll_1;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.music_name);
            singer = itemView.findViewById(R.id.music_singer);
            album = itemView.findViewById(R.id.music_album);
            ll_1 = itemView.findViewById(R.id.ll_1);
        }
    }

    @NonNull
    @Override
    public MusicAdapter.MusicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_music, viewGroup, false);
        MusicViewHolder holder = new MusicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicAdapter.MusicViewHolder holder, final int i) {
        Music music = list_music.get(i);
        holder.name.setText(music.getTitle());
        holder.singer.setText(music.getArtist());
        holder.album.setText(music.getAlbum());
        holder.ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MusicPlayActivity.class);
                intent.putExtra("index", holder.getAdapterPosition());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_music.size();
    }
}
