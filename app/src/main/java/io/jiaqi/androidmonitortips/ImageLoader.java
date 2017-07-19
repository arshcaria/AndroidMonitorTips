package io.jiaqi.androidmonitortips;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * Created by Jiaqi on 2017/7/19.
 */

public class ImageLoader {

    LruCache<Integer, Bitmap> cache;

    public ImageLoader() {
        cache = new LruCache<>(5);
    }

    public Bitmap getBitmap(int image) {
        Bitmap bitmap = cache.get(image);
        if (bitmap == null) {
            try {
                java.net.URL url = new java.net.URL(Images.imageURLs[(image % Images.imageURLs.length + Images.imageURLs.length) % Images.imageURLs.length]);
                URLConnection connection = url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                if (bitmap != null) cache.put(image, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void clearCache() {
        cache.evictAll();
    }

}
