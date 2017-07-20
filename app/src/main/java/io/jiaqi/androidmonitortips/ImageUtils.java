package io.jiaqi.androidmonitortips;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Jiaqi on 2017/7/20.
 */

public class ImageUtils {
    public interface ImageProgress {
        void progress(int i, int n);
    }

    public static Bitmap mono(Bitmap origBitmap, ImageProgress progress) {
        // image size
        int width = origBitmap.getWidth();
        int height = origBitmap.getHeight();

        int total = width * height;
        int numProcessed = 0;
        int numProcessedPrev = 0;

        Bitmap resultBitmap = Bitmap.createBitmap(width, height, origBitmap.getConfig());
        int a, r, g, b;
        int pixel;

        double contrast = Math.pow(1, 2);

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = origBitmap.getPixel(x, y);
                a = Color.alpha(pixel);

                r = Color.red(pixel);
                r = (int)(((((r / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(r < 0) { r = 0; }
                else if(r > 255) { r = 255; }

                g = Color.red(pixel);
                g = (int)(((((g / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(g < 0) { g = 0; }
                else if(g > 255) { g = 255; }

                b = Color.red(pixel);
                b = (int)(((((b / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(b < 0) { b = 0; }
                else if(b > 255) { b = 255; }

                resultBitmap.setPixel(x, y, Color.argb(a, r, g, b));

                if (++numProcessed - numProcessedPrev > 100) {
                    numProcessedPrev = numProcessed;
                    progress.progress(numProcessed, total);
                }
            }
        }
        return resultBitmap;
    }

    public static Bitmap sharpen(Bitmap origBitmap, ImageProgress progress) {
        int size = 1;
        int width = origBitmap.getWidth();
        int height = origBitmap.getHeight();

        int total = width * height;
        int numProcessed = 0;
        int numProcessedPrev = 0;

        Bitmap resultBitmap = Bitmap.createBitmap(width, height, origBitmap.getConfig());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int[] rgb = new int[3];
                rgb[0] = rgb[1] = rgb[2] = 0;
                for (int dx = -size; dx <= size; dx++) {
                    for (int dy = -size; dy <= size; dy++) {
                        int nx = x + dx;
                        int ny = y + dy;
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            int p = origBitmap.getPixel(nx, ny);
                            int f = dx == 0 && dy == 0 ? 9 : -1;
                            rgb[0] += f * Color.red(p);
                            rgb[1] += f * Color.green(p);
                            rgb[2] += f * Color.blue(p);
                        }
                    }
                }
                resultBitmap.setPixel(x, y, Color.rgb(rgb[0],rgb[1],rgb[2]));

                if (++numProcessed - numProcessedPrev > 100) {
                    numProcessedPrev = numProcessed;
                    progress.progress(numProcessed, total);
                }
            }
        }
        return resultBitmap;
    }
}
