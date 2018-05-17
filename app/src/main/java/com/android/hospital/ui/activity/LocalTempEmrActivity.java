package com.android.hospital.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.github.snowdream.android.util.Log;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by WHL on 2015/2/9.
 */
//临时写本地图片用。
public class LocalTempEmrActivity extends Activity{
    
    private ViewPager viewPager;

    private int[] item1={R.drawable.pdf_dianzi_1};
    private int[] item2={R.drawable.pdf_bincheng_1,R.drawable.pdf_bincheng_2,R.drawable.pdf_bincheng_3,R.drawable.pdf_bincheng_4,R.drawable.pdf_bincheng_5,R.drawable.pdf_bincheng_6};
    private int[] item3={R.drawable.pdf_ruyuan_1,R.drawable.pdf_ruyuan_2,R.drawable.pdf_ruyuan_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_emr);

        viewPager= (ViewPager) findViewById(R.id.view_pager);
        int position=getIntent().getIntExtra("position",0);
        Log.d("LocalTempEmrActivity","position="+position);
        if (position==0){
            viewPager.setAdapter(new PcsPagerAdapter(item1));
        }else if (position==1){
            viewPager.setAdapter(new PcsPagerAdapter(item2));
        }else {
            viewPager.setAdapter(new PcsPagerAdapter(item3));
        }
    }


    private class PcsPagerAdapter extends PagerAdapter {
        
        private int[] imagesIndex;

        public PcsPagerAdapter(int[] arr){
            this.imagesIndex=arr;
        }

        @Override
        public int getCount() {
            if (imagesIndex==null) {
                return 0;
            }
            return imagesIndex.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView imageView = new PhotoView(container.getContext());
/*
            imageUrl="http://szxc.pacs120.com:88/Partner/Image.ashx?UserName=monovo&Password=monovo&Width=1024&Height=768&StudyUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180&SeriesUID="+mSeriesUid+"&Index="+position+"&Layout=1x1&Invert=false&WW=&WC=&MoveX=&MoveY=&Angle=&FlipImage=&Mode=&ImageType=JPEG";
*/

            Picasso.with(LocalTempEmrActivity.this).load(imagesIndex[position]).into(imageView);

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
