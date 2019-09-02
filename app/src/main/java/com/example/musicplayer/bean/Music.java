package com.example.musicplayer.bean;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : PengLiang
 * Time : 2019/8/26
 * Description : 音乐数据类
 */
public class Music {
    private long id;
    private long album_id;
    // 歌名
    private String title;
    //歌手
    private String artist;
    private long size;
    // 歌曲绝对路径
    private String url;
    private int isMusic;
    // 歌曲时间
    private long duration;
    // 专辑
    private String album;

    public String getAlbum() {
        return album;
    }

    // 使用此方法前记得加上<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />这条权限
    // 此方法读取手机上的音频文件，并返回一个音频文件信息的List
    public static List<Music> getMusicList(Context context) {

        /**
         * Cursor 是每行的集合。使用 moveToFirst() 定位第一行。
         * 你必须知道每一列的名称。你必须知道每一列的数据类型。
         * Cursor 是一个随机的数据源。所有的数据都是通过下标取得
         */

        //得到一个内容提供器的实例
        ContentResolver contentResolver = context.getContentResolver();
        /*query()中的第一个参数是指定查询某个应用程序下的某一张表
         * 第二个参数是指定查询的列名
         * 第三个参数是指定where的约束条件
         * 第四个参数是为where中的占位符提供具体的值
         * 第五个参数是指定查询的结果的排序方式
         * */
        //查询完之后返回的是一个Cursor对象，这时我们就可以将数据从Cursor对象中逐个读取出来了
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        // 创建一个Music类的列表
        List<Music> musicList = new ArrayList<>();

        // moveToFirst() 定位第一行
        if (cursor.moveToFirst()) {
            // getCount()   总数据项数
            for (int i = 0; i < cursor.getCount(); i++) {
                // 实例化一个music类的对象m，music类是自己创建的一个类，用于保存音乐的信息
                Music m = new Music();
                // cursor.getColumnIndex(String columnName) 返回某列名对应的列索引值，如果不存在返回-1
                // cursor.getString(int columnIndex)   //返回当前行指定列的值
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                // 歌名
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                // 歌手
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                // 歌的时长
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                // 歌的大小
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                // 歌的绝对路径
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                // 专辑
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                int ismusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));

                // 如果歌曲符合指定要求，就添加到列表中去
                if (ismusic != 0 && duration / (500 * 60) >= 1) {
                    // 对m对象的元素进行赋值
                    m.setId(id);
                    m.setTitle(title);
                    m.setArtist(artist);
                    m.setDuration(duration);
                    m.setSize(size);
                    m.setUrl(url);
                    m.setAlbum(album);
                    m.setAlbum_id(album_id);
                    musicList.add(m);
                }

                //移动到下一行
                cursor.moveToNext();

            }

        }

        for (int i = 0; i < musicList.size(); i++) {
            Music music;
            String name = musicList.get(i).title;
            if (name.contains("人间白首")) {
                music = musicList.get(3);
                musicList.set(3, musicList.get(i));
                musicList.set(i, music);
            }
            if (name.contains("铭记")) {
                music = musicList.get(4);
                musicList.set(4, musicList.get(i));
                musicList.set(i, music);
            }
            if (name.contains("沐-")) {
                musicList.remove(i);
            }

            switch (name) {
                case "若当来世":
                    music = musicList.get(0);
                    musicList.set(0, musicList.get(i));
                    musicList.set(i, music);
                    break;
                case "梦回还":
                    music = musicList.get(1);
                    musicList.set(1, musicList.get(i));
                    musicList.set(i, music);
                    break;
                case "此彼绘卷":
                    music = musicList.get(2);
                    musicList.set(2, musicList.get(i));
                    musicList.set(i, music);
                    break;
            }
        }
        // 返回一个带数据的Music类的列表
        return musicList;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(int isMusic) {
        this.isMusic = isMusic;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
