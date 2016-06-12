package com.hangon.common;




import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.hangon.bean.music.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/10.
 */
public class MusicUtil {

    public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return query(context, uri, projection, selection, selectionArgs, sortOrder, 0);
    }

    public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, int limit) {
        try {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return null;
            }
            if (limit > 0) {
                uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (UnsupportedOperationException ex) {
            return null;
        }
    }
    /**
     * 得到媒体库中的全部歌曲
     */
    public static List<Music> getAllSongs(Context context) {
        Cursor c = query(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.MediaColumns.DATA,MediaStore.Audio.Media.DURATION},
                MediaStore.Audio.Media.IS_MUSIC + "=1", null, null);
        try {
            if (c == null || c.getCount() == 0) {
                return null;
            }
            int len = c.getCount();
            Log.e("xxxxxlen",len+"");

            ArrayList<Music> list = new ArrayList<Music>();
            int id = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int title = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int name = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int url = c.getColumnIndex(MediaStore.MediaColumns.DATA);
            int time=c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            for (int i = 0; i < len; i++) {
                Music mp3 = new Music();
                c.moveToNext();
                mp3.setName(c.getString(title));
                mp3.setSinger(c.getString(name));
                mp3.setUrl(c.getString(url));
                mp3.setTime(time);
                list.add(mp3);
            }

            return list;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }






    //查询系统的音频库
    public static List<Music> getMusicData(Context context) {
        int i = 1;
        List<Music> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media.DURATION + ">=?",
                new String[]{"2000"},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            Music music = new Music();
            if (i < 10) {
                music.setNumber("0" + i);
            } else {
                music.setNumber(i + "");
            }
            music.setTitle(cursor.getString(cursor
                    .getColumnIndexOrThrow((MediaStore.Audio.Media.TITLE))));
            String name = cursor.getString(cursor
                    .getColumnIndexOrThrow((MediaStore.Audio.Media.DISPLAY_NAME)));
            if (name.contains("(")) {
                name = name.substring(0, name.indexOf("("));
            }
            music.setName(name);
            String singer = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            if (singer == null || "".equals(singer)
                    || "<unkown>".equals(singer)) {
                singer = "未知艺术家";
            }
            music.setSinger(singer);
            music.setUrl(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
            music.setTime(cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
            if (music.getTitle().trim().length() < 10 && music.getSinger().length() < 10) {
                list.add(music);
                i++;
            }
        }
        return list;
    }

    public static String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int seconed = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, seconed);
    }
}
