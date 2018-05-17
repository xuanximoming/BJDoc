package com.android.hospital.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.BaseListAdapter;
import com.android.hospital.model.Series;
import com.android.hospital.util.PacsApi;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by WHL on 2015/2/7.
 */
public class MonovoSeriesActivity extends FragmentActivity {

    private ListView mListView;
    TextView mHeaderTev;
    private String studyUid;
    private String mPacsPatientId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monovo_pacs_list);

        studyUid=getIntent().getStringExtra("studyUid");
        mPacsPatientId=getIntent().getStringExtra("pacsPatientId");

        mListView= (ListView) findViewById(R.id.list_view);
        mHeaderTev= (TextView) findViewById(R.id.header);
        mHeaderTev.setVisibility(View.GONE);
        fillDataFromNet();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeriesAdapter adapter= (SeriesAdapter) mListView.getAdapter();
                Series series= (Series) adapter.getItem(position);
                Intent intent=new Intent(MonovoSeriesActivity.this,MonovoImagesActivity.class);
                intent.putExtra("seriesUid",series.SeriesUID);
                intent.putExtra("studyUid",studyUid);
                startActivity(intent);
            }
        });
    }

    private void fillDataFromNet() {
        
        String requestUrl= PacsApi.getSeries(this,mPacsPatientId,studyUid);
/*
        requestUrl="http://szxc.pacs120.com:88/Partner/Series.ashx?UserName=monovo&Password=monovo&StudyUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180&patientID=2012017521";
*/
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(requestUrl,null,new TextHttpResponseHandler() {

            private ProgressDialog mProgress=new ProgressDialog(MonovoSeriesActivity.this);
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MonovoSeriesActivity.this, "服务器连接失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (TextUtils.isEmpty(responseString)){
                    return;
                }
                Series.SeriesResult seriesResult=new Gson().fromJson(responseString, Series.SeriesResult.class);
                if (seriesResult==null){
                    return;
                }
                List<Series> serieses=seriesResult.SeriesList;
                if (serieses.size()==0){
                    Toast.makeText(MonovoSeriesActivity.this,"暂无数据...",Toast.LENGTH_LONG).show();
                    return;
                }
                SeriesAdapter adapter=new SeriesAdapter(MonovoSeriesActivity.this,serieses);
                mListView.setAdapter(adapter);
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
    
    private class SeriesAdapter extends BaseListAdapter{


        public SeriesAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView==null){
                convertView=mInflater.inflate(R.layout.item_study,parent,false);
                holder=new Holder();
                holder.StudyID= (TextView) convertView.findViewById(R.id.StudyID);
                holder.StudyDateTime= (TextView) convertView.findViewById(R.id.StudyDateTime);
                holder.StudyDescription= (TextView) convertView.findViewById(R.id.StudyDescription);
                holder.NumberOfSeries= (TextView) convertView.findViewById(R.id.NumberOfSeries);
                holder.Thumbs= (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            }else {
                holder= (Holder) convertView.getTag();
            }
            Series series= (Series) getItem(position);
            holder.StudyID.setText("序列描述："+series.SeriesDescription);
            holder.StudyDateTime.setText("序列UID："+series.SeriesUID);
            holder.StudyDescription.setText("序列编号："+series.SeriesNumber);
            holder.NumberOfSeries.setText("图片数量："+series.NumberOfImages);
            holder.Thumbs.setVisibility(View.VISIBLE);
            String imageUrl=PacsApi.getThumbs(studyUid,series.SeriesUID);
/*
            imageUrl="http://szxc.pacs120.com:88/Partner/Thumbs.ashx?UserName=monovo&Password=monovo&Width=120&Height=100&StudyUID=1.2.392.200036.9116.2.5.1.16.1613469867.1423276946.801180&SeriesUID="+series.SeriesUID;
*/
            Picasso.with(mContext).load(imageUrl).into(holder.Thumbs);
            return convertView;
        }
    }

    private class Holder{
        public TextView StudyID;
        public TextView StudyDateTime;
        public TextView StudyDescription;
        public TextView NumberOfSeries;
        public ImageView Thumbs;
    }
    
}
