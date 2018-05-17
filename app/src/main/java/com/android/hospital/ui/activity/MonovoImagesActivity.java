package com.android.hospital.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.hospital.model.Instance;
import com.android.hospital.util.PacsApi;
import com.android.hospital.widgets.HackyViewPager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by WHL on 2015/2/7.
 */
public class MonovoImagesActivity extends FragmentActivity{

    private ViewPager mViewPager;
    
    private int[] imagesIndex;
    
    private String mSeriesUid;
    private String mStudyUid;
    
    private Button mPlayBut;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcs_photo_view);

        mPlayBut= (Button) findViewById(R.id.play);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mSeriesUid=getIntent().getStringExtra("seriesUid");
        mStudyUid=getIntent().getStringExtra("studyUid");
        fillDataFromNet();
        
        mPlayBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imagesIndex==null){
                    Toast.makeText(MonovoImagesActivity.this,"无图片信息",Toast.LENGTH_LONG).show();
                    return;
                }
                if (imagesIndex.length==0){
                    Toast.makeText(MonovoImagesActivity.this,"图片数量为0",Toast.LENGTH_LONG).show();
                    return;
                }

                if (imagesIndex.length==1){
                    Toast.makeText(MonovoImagesActivity.this,"当前只有一张图片...",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent=new Intent(MonovoImagesActivity.this,PacsImageSliderActivity.class);
                intent.putExtra("images",imagesIndex);
                intent.putExtra("seriesUid",mSeriesUid);
                intent.putExtra("studyUid",mStudyUid);
                startActivity(intent);
            }
        });
    }

    private void fillDataFromNet() {
        AsyncHttpClient client=new AsyncHttpClient();
        String requestUrl= PacsApi.getInstance(mStudyUid,mSeriesUid);
/*
        requestUrl="http://szxc.pacs120.com:88/Partner/Instance.ashx?UserName=monovo&Password=monovo&StudyUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180&SeriesUID="+mSeriesUid;
*/
        client.get(requestUrl,null,new TextHttpResponseHandler() {

            private ProgressDialog mProgress=new ProgressDialog(MonovoImagesActivity.this);
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MonovoImagesActivity.this, "服务器连接失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (TextUtils.isEmpty(responseString)){
                    return;
                }
                Instance instance=new Gson().fromJson(responseString, Instance.class);
                if (instance==null){
                    return;
                }
                if (instance.Records.equals("0")){
                    Toast.makeText(MonovoImagesActivity.this,"暂无数据...",Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    int records=Integer.parseInt(instance.Records);
                    imagesIndex=new int[records];
                    for (int i = 0; i < records; i++) {
                        imagesIndex[i]=i;
                        
                    }
                    PcsPagerAdapter adapter=new PcsPagerAdapter();
                    mViewPager.setAdapter(adapter);
                    setTitle("图像列表（"+records+"）张");
                }catch (Exception e){
                    Toast.makeText(MonovoImagesActivity.this,"图片数量解析错误",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFinish() {
                mProgress.cancel();
            }

            @Override
            public void onStart() {
                mProgress.show();
            }            
        });
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
            PhotoView photoView = new PhotoView(container.getContext());
            String imageUrl=PacsApi.getImage(mStudyUid,mSeriesUid,String.valueOf(position));
/*
            imageUrl="http://szxc.pacs120.com:88/Partner/Image.ashx?UserName=monovo&Password=monovo&Width=1024&Height=768&StudyUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180&SeriesUID="+mSeriesUid+"&Index="+position+"&Layout=1x1&Invert=false&WW=&WC=&MoveX=&MoveY=&Angle=&FlipImage=&Mode=&ImageType=JPEG";
*/
            Picasso.with(MonovoImagesActivity.this).load(imageUrl).into(photoView);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
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
