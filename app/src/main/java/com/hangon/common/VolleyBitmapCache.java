package com.hangon.common;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;


/**
 * Created by Administrator on 2016/6/13.
 */
public class VolleyBitmapCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> mMemoryCache;

    public static Bitmap cacheBitmap;

    public VolleyBitmapCache() {
        mMemoryCache = new LruCache<String, Bitmap>(MyApplication.memoryCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String s) {
        return mMemoryCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        mMemoryCache.put(s, bitmap);
    }
}
