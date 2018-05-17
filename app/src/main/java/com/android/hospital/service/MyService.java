package com.android.hospital.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.logging.Log;

import com.android.hospital.HospitalApp;
import com.android.hospital.asyntask.add.DrugOrNonDrugTask;
import com.android.hospital.asyntask.add.FreqAndWayTask;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.InspectionItemEntity;
import com.android.hospital.entity.OperationItemEntity;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.webservice.WebServiceHelper;
import android.util.Log;
import android.app.Service;
import android.content.Entity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
/**
 * 
 * @author Administrator 
 * 此类已被弃用
 *
 */
public class MyService extends Service{
	
	private HospitalApp app;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		app=(HospitalApp) getApplication();
		putDrugOrNonDrugTask();
		putFreqAndWayTask();
		putClassAndDeptTask();
		putInspectionDeptTask();
		putInspectionItemTask();
		putOperationItemTask();
	}
	
	/**
	 * 
	* @Title: putDrugOrNonDrugTask 
	* @Description: TODO(开始获取药品或者非药品的任务) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void putDrugOrNonDrugTask() {
		String drugSql="select * from input_drug_lists";
		String nondrugSql="select * from v_clinic_name_dict";
		new DrugOrNonDrugTask(this, drugSql, nondrugSql).execute();
	}

	/**
	 * 
	* @Title: putFreqAndWayTask 
	* @Description: TODO(获取途径和频次任务) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void putFreqAndWayTask(){
		String freqSql = "select serial_no,freq_desc,freq_counter,freq_interval,freq_interval_units from perform_freq_dict";
		String waySql = "select administration_name,serial_no,administration_code,input_code from administration_dict where inp_outp_flag ='1' or inp_outp_flag is null";
		String[] sqlArr=new String[]{waySql,freqSql};
		new FreqAndWayTask(this, sqlArr).execute();
	}
	/**
	 * 
	* @Title: putClassAndDeptTask 
	* @Description: TODO(获取检查类别和发往科室任务) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void putClassAndDeptTask(){
		new ClassAndDeptTask().execute();
	}
	
	private class ClassAndDeptTask extends AsyncTask<Void, Void, String>{

		private List<Map<String, String>> classList;//检查类别
		private List<Map<String, String>> deptList;//发往科室
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String classSql="select exam_class_code,exam_class_name,serial_no,perform_by from exam_class_dict";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(classSql);
			classList=new ArrayList<Map<String,String>>();
			int size=dataList.size();
			for (int i = 0; i < size; i++) {
				Map<String, String> map=new HashMap<String, String>();
				map.put("exam_class_name", dataList.get(i).get("exam_class_name").trim());
				classList.add(map);
			}
			String deptSql="select dept_name,dept_code from dept_dict where clinic_attr='1'";
			ArrayList<DataEntity> dataList2=WebServiceHelper.getWebServiceData(deptSql);
			deptList=new ArrayList<Map<String,String>>();
			int size2=dataList2.size();
			for (int i = 0; i < size2; i++) {
				Map<String, String> map=new HashMap<String, String>();
				map.put("dept_name", dataList2.get(i).get("dept_name").trim());
				map.put("dept_code", dataList2.get(i).get("dept_code").trim());
				deptList.add(map);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			app.setClassList(classList);
			app.setDeptList(deptList);
		}
	}
	/**
	 * 
	* @Title: putInspectionDeptTask 
	* @Description: TODO(获取检验科室和类别) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void putInspectionDeptTask(){
		new InspectionDeptTask().execute();
	}
	
	private class InspectionDeptTask extends AsyncTask<Void, Void, String>{

		private List<Map<String, String>> deptList;//检验科室
		private List<Map<String, String>> classList;//检验类别
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String deptSql = "select distinct  lab_sheet_master.performed_by, dept_dict.dept_name  "
					+ "from dept_dict,  lab_sheet_master   "
					+ "where ( dept_dict.dept_code = lab_sheet_master.performed_by )";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(deptSql);
			deptList=new ArrayList<Map<String,String>>();
			int size=dataList.size();
			Map<String, String> mapNull=new HashMap<String, String>();
			mapNull.put("dept_name", "");
			mapNull.put("performed_by", "");
			deptList.add(mapNull);
			for (int i = 0; i < size; i++) {
				Map<String, String> map=new HashMap<String, String>();
				map.put("dept_name", dataList.get(i).get("dept_name"));
				map.put("performed_by", dataList.get(i).get("performed_by"));
				deptList.add(map);
			}
			String classSql="select serial_no,class_code,class_name  from lab_item_class_dict";
			ArrayList<DataEntity> dataList2=WebServiceHelper.getWebServiceData(classSql);
			classList=new ArrayList<Map<String,String>>();
			int size2=dataList2.size();
			Map<String, String> mapNull2=new HashMap<String, String>();
			mapNull2.put("class_name", "");
			classList.add(mapNull2);
			for (int i = 0; i < size2; i++) {
				Map<String, String> map=new HashMap<String, String>();
				map.put("class_name", dataList2.get(i).get("class_name"));
				classList.add(map);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			app.setInspecitonClassList(classList);
			app.setInspectionDeptList(deptList);
		}
	}
	
	/**
	 * 
	* @Title: putInspectionItemTask 
	* @Description: TODO(检验项目任务) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void putInspectionItemTask(){
		new InspectionItemTask().execute();
	}
	
	private class InspectionItemTask extends AsyncTask<Void, Void, ArrayList<InspectionItemEntity>>{

		@Override
		protected ArrayList<InspectionItemEntity> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String sql="select * from v_clab_name_dict";
			DebugUtil.debug("检验的问题", sql);
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
			ArrayList<InspectionItemEntity> arrayList=new ArrayList<InspectionItemEntity>();
			int size=dataList.size();
			DebugUtil.debug("长度", ""+size);
			for (int i = 0; i < size; i++) {
				InspectionItemEntity entity=new InspectionItemEntity();
				entity.item_class=dataList.get(i).get("item_class").trim();
				entity.item_name=dataList.get(i).get("item_name").trim();
				entity.item_code=dataList.get(i).get("item_code").trim();
				entity.std_indicator=dataList.get(i).get("std_indicator").trim();
				entity.input_code=dataList.get(i).get("input_code").trim();
				entity.input_code_wb=dataList.get(i).get("input_code_wb").trim();
				entity.performed_by=dataList.get(i).get("performed_by").trim();
				entity.expand1=dataList.get(i).get("expand1").trim();
				entity.expand2=dataList.get(i).get("expand2").trim();
				entity.expand3=dataList.get(i).get("expand3").trim();
				arrayList.add(entity);
			}
			return arrayList;
		}
		@Override
		protected void onPostExecute(ArrayList<InspectionItemEntity> result) {
			// TODO Auto-generated method stub
			app.setInspectionItemList(result);
			DebugUtil.debug("执行了吧", "22222222");
		}
	}
	/**
	 * 
	* @Title: putOperationItemTask 
	* @Description: TODO(手术项目任务) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */

	public void putOperationItemTask(){
		new OperationItemTask().execute();
		
		
	}
	private class OperationItemTask extends AsyncTask<Void, Void, ArrayList<OperationItemEntity>>{
		@Override
		protected ArrayList<OperationItemEntity> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String osql="select * from operation_dict";
			Log.e("检索的语句是否执行了", osql);
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(osql);
			ArrayList<OperationItemEntity> arrayList=new ArrayList<OperationItemEntity>();
			int size=dataList.size();
			for (int i = 0; i < size; i++) {
				OperationItemEntity entity=new OperationItemEntity();
				entity.operation_code=dataList.get(i).get("operation_code").trim();
				entity.operation_name=dataList.get(i).get("operation_name").trim();
				entity.operation_scale=dataList.get(i).get("operation_scale").trim();
				entity.std_indicator=dataList.get(i).get("std_indicator").trim();
				entity.input_code=dataList.get(i).get("input_code").trim();
				entity.approved_indicator=dataList.get(i).get("approved_indicator").trim();
				arrayList.add(entity);
			}
			return arrayList;
		}
		@Override
		protected void onPostExecute(ArrayList<OperationItemEntity> result) {
			// TODO Auto-generated method stub
			app.setOperationItemList(result);
		}
	}
}
