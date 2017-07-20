package io.jiaqi.androidmonitortips;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivImage;
    Button btnPrev;
    Button btnNext;
    ProgressBar pbProgress;

    ImageLoader imageLoader;

    int currentImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageLoader = new ImageLoader();

        ivImage = (ImageView) findViewById(R.id.iv_image);
        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnNext = (Button) findViewById(R.id.btn_next);
        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
        pbProgress.setVisibility(View.INVISIBLE);

        new SetImageTask().execute(currentImageIndex);
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        ivImage.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mono:
                new ApplyFilterTask() {
                    @Override
                    protected Bitmap filter(Bitmap orig, ImageUtils.ImageProgress progress) {
                        return ImageUtils.mono(orig, progress);
                    }
                }.execute(currentImageIndex);
                break;
            case R.id.action_sharpen:
                new ApplyFilterTask() {
                    @Override
                    protected Bitmap filter(Bitmap orig, ImageUtils.ImageProgress progress) {
                        return ImageUtils.sharpen(orig, progress);
                    }
                }.execute(currentImageIndex);
                break;
            case R.id.action_clear_cache:
                imageLoader.clearCache();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_prev:
                if (currentImageIndex <= 0) return;
                else new SetImageTask().execute(--currentImageIndex);
                break;
            case R.id.btn_next:
                if (currentImageIndex >= 4) return;
                else new SetImageTask().execute(++currentImageIndex);
                break;
        }
    }

    private class SetImageTask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            int imageIndex = integers[0];
            Log.i("MainActivity", imageIndex + "");
            return imageLoader.getBitmap(imageIndex);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ivImage.setImageBitmap(bitmap);
        }
    }

    abstract private class ApplyFilterTask extends AsyncTask<Integer, Integer, Bitmap> {

        ImageUtils.ImageProgress progress = new ImageUtils.ImageProgress() {
            @Override
            public void progress(int i, int n) {
                pbProgress.setMax(n);
                pbProgress.setProgress(i);
            }
        };

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbProgress.setVisibility(View.VISIBLE);

        }

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            return filter(imageLoader.getBitmap(integers[0]), progress);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            pbProgress.setVisibility(View.INVISIBLE);
            ivImage.setImageBitmap(bitmap);
        }

        abstract protected Bitmap filter(Bitmap orig, ImageUtils.ImageProgress progress);

    }

}
