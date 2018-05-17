package com.android.hospital.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.hospital.model.Instance;
import com.android.hospital.util.PacsApi;
import com.android.hospital.widgets.HackyViewPager;
import com.github.snowdream.android.util.Log;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.util.Random;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by WHL on 2015/2/7.
 */
//临时用，本地图片。
public class MonovoTempImagesActivity extends FragmentActivity{

    private ViewPager mViewPager;
    
    private int[] imagesIndex;

    private static Random rand = new Random(47);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcs_photo_view);

        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);

        imagesIndex=new int[]{R.drawable.temp_111,R.drawable.temp_222,R.drawable.temp_333,R.drawable.temp_444,R.drawable.temp_5555,R.drawable.temp_7777,R.drawable.temp_8888,R.drawable.temp_99999};
        
        PcsPagerAdapter adapter=new PcsPagerAdapter();
        mViewPager.setAdapter(adapter);
        setTitle("图像列表（"+"8"+"）张");
    }




    private class PcsPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            if (imagesIndex==null) {
                return 0;
            }
            return imagesIndex.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            if (container.getContext()==null){
                return null;
            }
            ImageView imageView = new ImageView(container.getContext());
            
            int resId=rand.nextInt(7);
            Log.d("MonovoTempImagesActivity","resId="+resId);
/*
            imageUrl="http://szxc.pacs120.com:88/Partner/Image.ashx?UserName=monovo&Password=monovo&Width=1024&Height=768&StudyUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180&SeriesUID="+mSeriesUid+"&Index="+position+"&Layout=1x1&Invert=false&WW=&WC=&MoveX=&MoveY=&Angle=&FlipImage=&Mode=&ImageType=JPEG";
*/
            
            Picasso.with(MonovoTempImagesActivity.this).load(imagesIndex[resId]).into(imageView);

            // Now just add PhotoView to ViewPager and return it
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
    
}
