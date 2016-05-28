package com.hangon.common;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.hangon.bean.music.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/10.
 */
public class MusicUtil {
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
                    .getColumnIndex((MediaStore.Audio.Media.TITLE))));
            String name = cursor.getString(cursor
                    .getColumnIndex((MediaStore.Audio.Media.DISPLAY_NAME)));
            if (name.contains("(")) {
                name = name.substring(0, name.indexOf("("));
            }
            music.setName(name);
            String singer = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
            if (singer == null || "".equals(singer)
                    || "<unkown>".equals(singer)) {
                singer = "未知艺术家";
            }
            music.setSinger(singer);
            music.setUrl(cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA)));
            music.setTime(cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION)));
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
