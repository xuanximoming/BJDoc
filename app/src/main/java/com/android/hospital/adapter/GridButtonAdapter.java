package com.android.hospital.adapter;

import java.util.ArrayList;

import com.android.hospital.HospitalApp;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.SignsLifeEntity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.ui.activity.TemperatureActivity;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.webservice.WebServiceHelper;
import com.android.hospital.widgets.MyProssDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 
* @ClassName: GridButtonAdapter 
* @Description: TODO(体温或者生命体征中一组button) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-30 上午10:51:25 
*
 */
public class GridButtonAdapter extends BaseAdapter{

	private Context mContext;
	
	private LayoutInflater mInflater;
	
	private ListView detailListView;
	
	private View detaillayout;
	
	
	private String[] textArr = { "体温图", "腋下体温", "血压", "呼吸", "脉搏", "大便次数", "体重",
			"血糖", "心率", "尿量", "降温", "痰量", "入院", "转入", "入量", "引流量",
			"死亡", "分娩", "呼吸", "请假", "摄入液量","总量","呕吐量",
			"出院","手术","口入量"};
	
	
	public GridButtonAdapter(Context context){
		this.mContext=context;
		this.mInflater=LayoutInflater.from(mContext);
		
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return textArr.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView=mInflater.inflate(R.layout.gridview_item_button, null);
		final Button button=(Button) convertView.findViewById(R.id.gridview_item_but);
		final String item=textArr[position];
		button.setText(item);	
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (item.equals("体温图")) {
					new TemperatureTask().execute(item);
				}else {
					new SignDetailTask(item).execute();
				}
			}
		});				
		return convertView;
	}

	/**
	 * 
	* @Title: showDetailInfo 
	* @Description: TODO(弹出详细信息dialog) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void showDetailInfo(String item){
		detaillayout=mInflater.inflate(R.layout.fragment_signslife_listview, null);
		detailListView=(ListView) detaillayout.findViewById(R.id.signslife_list_view);		
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext);	
		builder.setView(detaillayout);
		builder.setIconAttribute(android.R.attr.alertDialogIcon);
		builder.setTitle(item+"详细信息")
           .setNegativeButton("取消", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int whichButton) {

                   /* User clicked cancel so do some stuff */
               }
           }).create().show();
	}
	
	
	/**
	 * 
	* @ClassName: SignDetailTask 
	* @Description: TODO(获取生命体征详信息任务) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2013-1-5 下午8:23:08 
	*
	 */
	private class SignDetailTask extends AsyncTask<String, Void, String>{

		private String itemName;
		
		private ArrayList<SignsLifeEntity> signsList;
		
		public SignDetailTask(String item){
			this.itemName=item;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDetailInfo(itemName);
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HospitalApp app=(HospitalApp) mContext.getApplicationContext();
			String sql = "select recording_date,time_point,vital_signs,vital_signs_cvalues,units,nurse from vital_signs_rec where patient_id='"
					+ app.getPatientEntity().patient_id
					+ "' and visit_id='"
					+ app.getPatientEntity().visit_id
					+ "' and vital_signs='"
					+ itemName + "'";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
			signsList=SignsLifeEntity.init(dataList);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (detailListView!=null&&signsList.size()!=0) {
				SignsLifeDetailAdapter adapter=new SignsLifeDetailAdapter(mContext, signsList);
				detailListView.setAdapter(adapter);
			}else {
				Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * 
	* @ClassName: TemperatureTask 
	* @Description: TODO(获取体温图的相关信息) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2013-1-15 上午11:06:31 
	*
	 */
	private class TemperatureTask extends AsyncTask<String, Void, String>{

		private HospitalApp app;
		
		private String[] temperatureTimeArr=null;//体温时间
		
		private String[] temperatureArr=null;//温度
		
		private MyProssDialog mDialog;
		
		private String[] BloodPressureTimeArr=null;//血压时间
		
		private String[] BloodPressureArr=null;//血压值	
		
		private String[] bloodPressureHighArr=null;//高血压
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
	        mDialog=new MyProssDialog(mContext, "提交", "正在提交请求，请稍候...");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			app=(HospitalApp)mContext.getApplicationContext();
			AppConstant.patientEntity=app.getPatientEntity();
			String sql="select TO_CHAR(time_point,'yyyy-MM-dd hh24:mi') as time_point, vital_signs_cvalues, units" +
					" from vital_signs_rec" +
					" where patient_id = '"+app.getPatientEntity().patient_id+"'" +
					" and visit_id = '"+app.getPatientEntity().visit_id+"'" +
					" and vital_signs = '腋下体温'";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
			temperatureArr=new String[dataList.size()];
			temperatureTimeArr=new String[dataList.size()];
			for (int i = 0; i < dataList.size(); i++) {
				temperatureArr[i]=dataList.get(i).get("vital_signs_cvalues");
				temperatureTimeArr[i]=dataList.get(i).get("time_point");
				DebugUtil.debug("温度--->"+temperatureArr[i]);
				DebugUtil.debug("时间--->"+temperatureTimeArr[i]);
			}
			
			AppConstant.temperatureArr=temperatureArr;
			AppConstant.temperatureTimeArr=temperatureTimeArr;
			
			//血压获取   有问题，需测试完善
/*			String sql2="select TO_CHAR(time_point,'yyyy-MM-dd hh24:mi') as time_point, vital_signs_cvalues, units" +
					" from vital_signs_rec" +
					" where patient_id = '"+app.getPatientEntity().patient_id+"'" +
					" and visit_id = '"+app.getPatientEntity().visit_id+"'" +
					" and vital_signs = '血压'";
			ArrayList<DataEntity> dataList2=WebServiceHelper.getWebServiceData(sql2);
            if (dataList2!=null&&dataList2.size()>0){
                BloodPressureArr=new String[dataList2.size()];
                BloodPressureTimeArr=new String[dataList2.size()];
                bloodPressureHighArr=new String[dataList2.size()];
                for (int i = 0; i < dataList2.size(); i++) {
                    String tempString=dataList2.get(i).get("vital_signs_cvalues");
                    String[] bloodArr=tempString.split("/");
                    BloodPressureArr[i]=bloodArr[1];
                    bloodPressureHighArr[i]=bloodArr[0];
                    BloodPressureTimeArr[i]=dataList2.get(i).get("time_point");
                    DebugUtil.debug("血压--->"+BloodPressureArr[i]);
                    DebugUtil.debug("血压时间--->"+BloodPressureTimeArr[i]);
                }

                AppConstant.BloodPressureArr = BloodPressureArr;
                AppConstant.BloodPressureTimeArr = BloodPressureTimeArr;
                AppConstant.BloodPressureHighArr = bloodPressureHighArr;
            }*/
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
/*			mDialog.cancel();
//			IDemoChart mChart=new AverageTemperatureChart();
//			Intent intent=mChart.execute(mContext);
			Intent intent=new Intent(mContext,ChartDemo.class);
			mContext.startActivity(intent);*/
			mDialog.cancel();
			Intent intent=new Intent(mContext,TemperatureActivity.class);
			mContext.startActivity(intent);

		}
	}
	
}
