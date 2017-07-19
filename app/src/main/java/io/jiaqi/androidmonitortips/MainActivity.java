package io.jiaqi.androidmonitortips;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivImage;
    Button btnPrev;
    Button btnNext;

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

        new SetImageTask().execute(currentImageIndex);
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_prev:
                new SetImageTask().execute(--currentImageIndex);
                break;
            case R.id.btn_next:
                new SetImageTask().execute(++currentImageIndex);
                break;
        }
    }

    class SetImageTask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            int imageIndex = integers[0];
            Log.i("MainActivity", imageIndex + "");
            Bitmap bitmap = imageLoader.getBitmap(imageIndex);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ivImage.setImageBitmap(bitmap);
        }
    }
}
