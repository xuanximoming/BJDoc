package com.android.hospital.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.android.hospital.HospitalApp;
import com.android.hospital.entity.CheckEntity;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;
import com.android.hospital.widgets.HackyViewPager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class PcsPhotoViewActivity extends Activity{
	
    private HospitalApp mApp;
	
	private CheckEntity mCheckEntity;
	
	private String mPatientLocalID="";
	
	private String mFirstPath="";
	
	private ProgressDialog mDialog;
	
	private String[] mPcsArr;
	
	private ViewPager mViewPager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pcs_photo_view);
		
		mApp=(HospitalApp) getApplication();
		
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		
        mDialog=new ProgressDialog(this);
		
		mCheckEntity=(CheckEntity) getIntent().getSerializableExtra("check");
		
	    getParams();
		
	}
	
	private void getParams(){
		String examNo=mCheckEntity.exam_no;
		/*String examNo="486772";//for test
*/		final String sql="select  *  from  exam_master_item_data where exam_no='"+examNo+"'";
		new AsyncTask<Void, Void, String>() {
			
			protected void onPreExecute() {
				mDialog.setMessage("正在获取...");
				mDialog.show();
			};

			@Override
			protected String doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				ArrayList<DataEntity> dataList= WebServiceHelper.getWebServiceData(sql);
				if (dataList!=null&&dataList.size()>0) {
					for (int i = 0; i < dataList.size(); i++) {
						mFirstPath=dataList.get(i).get("first_path");
						mPatientLocalID=dataList.get(i).get("patient_local_id");
						Log.d("Pcs",mFirstPath );
					}
				}
				return mFirstPath;
			}
			
			protected void onPostExecute(String result) {
				if (TextUtils.isEmpty(mFirstPath)) {
					mDialog.cancel();
					Toast.makeText(PcsPhotoViewActivity.this, "没有该图像信息...", 500).show();
				}else {
					fillDataFromNet(result);
				}
			};
			
		}.execute();
	}
	
	private void fillDataFromNet(String studyId){
		   String url= "http://"+ Util.getServerIP(PcsPhotoViewActivity.this)+"/Partner/Series.ashx?UserName=admin&Password=admin&StudyUID="+studyId+"&patientID="+mPatientLocalID;
		   Log.d("Pcs", url);
		   AsyncHttpClient client=new AsyncHttpClient();
		   client.get(url, new TextHttpResponseHandler(){

               @Override
               public void onFailure(int statusCode, Header[] headers, String content, Throwable throwable) {
                   mDialog.cancel();
                   Log.d("Pcs", "error-->"+content);
                   Toast.makeText(PcsPhotoViewActivity.this, "获取图像失败...", 500).show();
               }

               @Override
               public void onSuccess(int statusCode, Header[] headers, String content) {
                   Log.d("Pcs", content);
                   mDialog.cancel();
                   if (TextUtils.isEmpty(content)) {
                       Toast.makeText(PcsPhotoViewActivity.this, "获取图像失败...", 500).show();
                       return;
                   }
                   try {
                       JSONObject json=new JSONObject(content);
                       JSONArray jsonArray=json.getJSONArray("SeriesList");
                       if (jsonArray!=null&&jsonArray.length()>0) {
                           mPcsArr=new String[jsonArray.length()];
                           for (int i = 0; i < jsonArray.length(); i++) {
                               JSONObject object=jsonArray.optJSONObject(i);
                               String SeriesUID =object.optString("SeriesUID");
                               mPcsArr[i]=SeriesUID;
                           }
                           PcsPagerAdapter adapter=new PcsPagerAdapter();
                           mViewPager.setAdapter(adapter);
                       }
                   } catch (JSONException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                   }
               }
           });
		}
	
	private class PcsPagerAdapter extends PagerAdapter {

		
		@Override
		public int getCount() {
			if (mPcsArr==null) {
				return 0;
			}
			return mPcsArr.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			Picasso.with(PcsPhotoViewActivity.this).load("http://"+ Util.getServerIP(PcsPhotoViewActivity.this)+"/Partner/Image.ashx?UserName=admin&Password=admin&Width=1024&Height=768&StudyUID="+mFirstPath+"&SeriesUID="+mPcsArr[position]+"&Index=0&Layout=1x1&Invert=false&WW=&WC=&MoveX=&MoveY=&Angle=&FlipImage=&Mode=&ImageType=JPEG").into(photoView);

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

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
