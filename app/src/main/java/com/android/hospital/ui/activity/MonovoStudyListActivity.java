package com.android.hospital.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.BaseListAdapter;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.model.Study;
import com.android.hospital.util.PacsApi;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;
import com.github.snowdream.android.util.Log;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WHL on 2015/2/7.
 */
public class MonovoStudyListActivity extends FragmentActivity{
    
    private static final String TAG="MonovoPacsListActivity";
    ListView mListView;
    TextView mHeaderTev;
    

    private String mPacsPatientId;
    
    private String studyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_monovo_pacs_list);

        mListView= (ListView) findViewById(R.id.list_view);
        mHeaderTev= (TextView) findViewById(R.id.header);
        
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudyAdapter adapter= (StudyAdapter) mListView.getAdapter();
                Study study= (Study) adapter.getItem(position);
                Intent intent=new Intent(MonovoStudyListActivity.this,MonovoSeriesActivity.class);
                intent.putExtra("studyUid",study.StudyUID);
                intent.putExtra("pacsPatientId",mPacsPatientId);
                startActivity(intent);
            }
        });
        
        getPacsPatientId();
    }

    private void getPacsPatientId(){
        final String sql="select * from exam_report where exam_no = '"+getIntent().getStringExtra("exam_no")+"'";
        new AsyncTask<Void,Void,String>(){
            private ProgressDialog mProgress=new ProgressDialog(MonovoStudyListActivity.this);
            @Override
            protected void onPreExecute() {
                mProgress.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                ArrayList<DataEntity> dataList= WebServiceHelper.getWebServiceData(sql);
                if (dataList==null||dataList.size()==0){
                    return null;
                }
                for (int i = 0; i < dataList.size(); i++) {
                    mPacsPatientId=dataList.get(i).get("pacs_patientid");
                    studyId=dataList.get(i).get("pacs_studyid");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                mProgress.cancel();
                if (TextUtils.isEmpty(mPacsPatientId)){
                    Toast.makeText(MonovoStudyListActivity.this,"获取Pacs图像ID失败...",Toast.LENGTH_LONG).show();
                    return;
                }
                fillDataFromNet();
            }
        }.execute();
        
    }
    
    private void fillDataFromNet() {
        
        String requestUrl= PacsApi.getStudy(this,mPacsPatientId,studyId);
/*
        requestUrl="http://szxc.pacs120.com:88/Partner/Study.ashx?UserName=monovo&Password=monovo&PatientName=&PatientID=2012017521&StudyID=&StudyAccessionNumber=&StudyDateStart=&StudyDateEnd=&Modality=";
*/
        mHeaderTev.setText("访问地址="+requestUrl);
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(requestUrl,null,new TextHttpResponseHandler() {
            
            private ProgressDialog mProgress=new ProgressDialog(MonovoStudyListActivity.this);
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MonovoStudyListActivity.this,"服务器连接失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG,"onSuccess="+responseString);
                if (TextUtils.isEmpty(responseString)){
                    return;
                }
                Study.StudyResult studyResult=new Gson().fromJson(responseString, Study.StudyResult.class);
                if (studyResult==null){
                    return;
                }
                List<Study> studies=studyResult.Rows;
                if (studies.size()==0){
                    Toast.makeText(MonovoStudyListActivity.this,"暂无数据...",Toast.LENGTH_LONG).show();
                    return;
                }
                Study study=studies.get(0);
                mHeaderTev.setText("姓名："+study.Name+"    年龄："+study.Age+"    性别："+study.Sex);
                StudyAdapter adapter=new StudyAdapter(MonovoStudyListActivity.this,studies);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onFinish() {
                Log.d(TAG,"onFinish");
                mProgress.cancel();
            }

            @Override
            public void onStart() {
                Log.d(TAG,"onStart");
                mProgress.show();
            }
        });
    }
    
    private class StudyAdapter extends BaseListAdapter{


        public StudyAdapter(Context context, List list) {
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
                convertView.setTag(holder);
            }else {
                holder= (Holder) convertView.getTag();
            }
            Study study= (Study) getItem(position);
            holder.StudyID.setText("检查编号："+study.StudyID);
            holder.StudyDateTime.setText("检查日期："+study.StudyDateTime);
            holder.StudyDescription.setText("检查描述："+study.StudyDescription);
            holder.NumberOfSeries.setText("序  列  数："+study.NumberOfSeries);
            return convertView;
        }
    }
    
    private class Holder{
        public TextView StudyID;
        public TextView StudyDateTime;
        public TextView StudyDescription;
        public TextView NumberOfSeries;
    }
    
}

