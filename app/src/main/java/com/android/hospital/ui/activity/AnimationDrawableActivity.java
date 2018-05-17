package com.android.hospital.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hospital.util.PacsApi;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by WHL on 2015/2/14.
 */
public class AnimationDrawableActivity extends FragmentActivity {

    private ImageView imageView;

    private int[] imagesIndex;
    
    private String seriesUid;
    private String studyUid;

    AnimationDrawable animationDrawable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_drawable);
        EditText textView= (EditText) findViewById(R.id.test);
        initConfig();
        
        imageView= (ImageView) findViewById(R.id.image);
        
        imagesIndex=getIntent().getIntArrayExtra("images");
        seriesUid=getIntent().getStringExtra("seriesUid");
        studyUid=getIntent().getStringExtra("studyUid");

        if (imagesIndex!=null){
            animationDrawable= new AnimationDrawable();
            for (int i = 0; i < imagesIndex.length; i++) {
                String imageUrl= PacsApi.getImage(studyUid, seriesUid, String.valueOf(i));
                textView.setText(imageUrl);
                ImageLoader.getInstance().loadImage(imageUrl,new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        BitmapDrawable drawable=new BitmapDrawable(loadedImage);
                        animationDrawable.addFrame(drawable,1000);
                        Toast.makeText(AnimationDrawableActivity.this,"onLoadingComplete",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        Toast.makeText(AnimationDrawableActivity.this,"onLoadingFailed"+failReason.getCause().toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
            imageView.setImageDrawable(animationDrawable);
        }else {
            Toast.makeText(this,"获取的imagesIndex为空",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animationDrawable.setOneShot(false);
                animationDrawable.start();
            }
        },3000);
    }


    public void initConfig(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
    
}
