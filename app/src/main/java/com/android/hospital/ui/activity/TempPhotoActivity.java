package com.android.hospital.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by Jack on 2015/6/13.
 */
public class TempPhotoActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView=new ImageView(this);
        imageView.setImageResource(R.drawable.temp_b_img);
        setContentView(imageView);
    }
}
