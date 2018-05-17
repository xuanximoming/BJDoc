package com.android.hospital.asyntask.add;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.hospital.HospitalApp;
import com.android.hospital.asyntask.BaseAsyncTask;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.webservice.WebServiceHelper;
/**
 * 
* @ClassName: MaxNumberTask 
* @Description: TODO(最大序号,触发器) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-24 下午5:44:11 
*
 */
public class MaxNumberTask extends AsyncTask<String, Void, String>{

	private Context mContext;
	
	private HospitalApp app;
	
	private String nextval="";//触发器
	
	public MaxNumberTask(Context context){
		this.mContext=context;
		this.app=(HospitalApp) mContext.getApplicationContext();
		if (app==null) {
			DebugUtil.debug("测试app为空");
		}
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String sql = "select max (order_no) from orders where patient_id ='"
				+ app.getPatientEntity().patient_id + "' and visit_Id ='"
				+ app.getPatientEntity().visit_id + "'";
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
		String max="0";
		for (int i = 0; i < dataList.size(); i++) {
			String result=dataList.get(i).get("max(order_no)").trim();
			if (!result.equals("")) {
				max=result;
			}
		}
		if (!params[0].equals("")) {
			String nextvalStr=(String) params[0];
			nextvalTask(nextvalStr);//获取触发器
		}
		return max;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		try {
			String temp=(String) result;
			int order_no=Integer.parseInt(temp);
			int max_no=order_no+1;
			String maxNumber=String.valueOf(max_no);
			app.setMaxNumber(maxNumber);
			app.setNextval(nextval);
		} catch (Exception e) {
			Toast.makeText(mContext, "获取最大值失败!", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	* @Title: nextvalTask 
	* @Description: TODO(获取触发器号) 
	* @param @param param    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void nextvalTask(String param){
		String sql="select "+param+" from dual";
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
		for (int i = 0; i < dataList.size(); i++) {
			nextval=dataList.get(i).get("nextval");
		}
	}
}
