package com.android.hospital.ui.activity;

import java.util.ArrayList;

import com.android.hospital.HospitalApp;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.InspectionEntity;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class InspectionChartActivity extends Activity{
	
	private HospitalApp app;
	
	private String sql;
	
	private String[] yDataArr;
	
	private String[] xDataArr;
	
	private int size;
	
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspection_chart);
		
		webView = (WebView)findViewById(R.id.webview);	
		app=(HospitalApp) getApplication();
		String value=getIntent().getStringExtra("value");
		String[] arrStrings=value.split("\\(");
		/*for (int i=0;i<arrStrings.length;i++){
			Log.e("检验项目22-----》", arrStrings[i]);
		}*/
		/*sql="select TO_CHAR(a.RESULT_DATE_TIME,'yyyy-MM-dd hh24:mi:ss') as " +
				"RESULT_DATE_TIME,a.report_item_name, a.result " +
				" from lab_result a,LAB_TEST_MASTER b " +
				" where a.test_no=b.test_no  and b.patient_id='"+app.getPatientEntity().patient_id+
				"' and visit_Id ='"+app.getPatientEntity().visit_id + "' and a.report_item_name='"+arrStrings[0]+"'"+
				" order  by  RESULT_DATE_TIME";*/
		sql=" select distinct  TO_CHAR(a.RESULT_DATE_TIME, 'yyyy-MM-dd hh24:mi:ss') as RESULT_DATE_TIME, "+
			" a.report_item_name, a.result  from lab_result a, " +
			" (select distinct b.test_no, b.patient_id, b.visit_id, c.item_name from LAB_TEST_MASTER b, lab_test_items c  where c.test_no = b.test_no ) tem "+
			" where a.test_no = tem.test_no  and tem.item_name in ("+ arrStrings[1]+") "+
			"  and a.report_item_name = '"+arrStrings[0]+"'"+
			"  and tem.patient_id='"+app.getPatientEntity().patient_id+"'"+
			"  and tem.visit_Id ='"+app.getPatientEntity().visit_id + "'"+
			" order by RESULT_DATE_TIME";
		Log.e("检验曲线图的sql----->", sql);
		fillData();
	}
	
	private void fillData(){
		new AsyncTask() {
			
			private ProgressDialog mDialog;
			
			protected void onPreExecute() {
				mDialog=new ProgressDialog(InspectionChartActivity.this);
				mDialog.setMessage("正在获取...");
				mDialog.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
				if (dataList==null||dataList.size()==0) {
					return null;
				}
				size=dataList.size();
				yDataArr=new String[size];
				xDataArr=new String[size];
				for (int i = 0; i < size; i++) {
					Log.d("InspectionChartActivity", dataList.get(i).get("result"));
					String result=dataList.get(i).get("result");
					String time=dataList.get(i).get("result_date_time");
					Log.d("InspectionChartActivity", time);
					yDataArr[i]=result;
					xDataArr[i]=time;
				}
				return "succ";
			}
			
			protected void onPostExecute(Object result) {
				mDialog.cancel();
				String resultStr=(String) result;
				if (TextUtils.isEmpty(resultStr)) {
					Toast.makeText(InspectionChartActivity.this, "未获取到相关数据...", 200).show();
				}else {
					/*Toast.makeText(InspectionChartActivity.this, "有数据...", 200).show();*/
					String inspectionChart=getXYData(xDataArr, yDataArr);
					Log.d("InspectionChartActivity", "getXYData"+inspectionChart);
					webView.getSettings().setJavaScriptEnabled(true);
					/*webView.loadUrl("http://10.10.10.13:8888/FlotServer/flot/flot.jsp");*/
                    String url="http://"+Util.getServerIP(InspectionChartActivity.this)+"/FlotServer/flot/flot.jsp?inspection="+inspectionChart;
                    Log.d("InspectionChartActivity",url);
					webView.loadUrl(url);
				}
			};
			
		}.execute();
	}
	
    public static String getXYData(String[] tempDateArr,String[] tempValueArr){
    	StringBuffer buffer=new StringBuffer();
    	int length=tempValueArr.length;
    	if (length==1) {
    		String utcTime=Util.converToDateUtc2(tempDateArr[0]);
    		buffer.append("[ ["+utcTime+","+tempValueArr[0]+"] ]");
		}else {
	    	for (int i = 0; i < tempValueArr.length; i++) {
	    		String utcTime=Util.converToDateUtc2(tempDateArr[i]);
				if (i==0) {
					buffer.append("[ ["+utcTime+","+tempValueArr[i]+"],");
				}else if (i==(tempValueArr.length-1)) {
					buffer.append("["+utcTime+","+tempValueArr[i]+"] ]");
				}else {
					buffer.append("["+utcTime+","+tempValueArr[i]+"],");
				}
			}
		}
    	
    	return buffer.toString();
    }

}
