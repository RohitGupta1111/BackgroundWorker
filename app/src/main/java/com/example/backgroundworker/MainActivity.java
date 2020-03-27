package com.example.backgroundworker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "MainActivity";

    ImageView imageView;
    Button btn1,btn2;
    AsyncWorker asyncWorker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        btn1 = findViewById(R.id.btn_1);
        btn1.setOnClickListener(this);
        btn2  = findViewById(R.id.btn_2);
        btn2.setOnClickListener(this);
        asyncWorker = new AsyncWorker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                loadImage(Constants.baseUrlPlane);
                break;
            case R.id.btn_2:
                loadImage(Constants.baseUrlMountain);
        }
    }

    public void loadImage(final String url) {
        Log.d(TAG,"clicked for loading image");
        asyncWorker.addTask(new Task<Bitmap>() {
            @Override
            public Bitmap onExecuteTask() {
                Bitmap bmp = null;
                try {
                    URL imageUrl = new URL(url);
                    bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;
            }

            @Override
            public void onTaskComplete(Bitmap args) {
                if(args!=null) {
                    imageView.setImageBitmap(args);
                }
            }
        });
    }
}
