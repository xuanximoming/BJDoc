package com.android.hospital.asyntask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.hospital.HospitalApp;
import com.android.hospital.entity.BloodTypeEntity;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.DoctorEntity;
import com.android.hospital.entity.DrugEntity;
import com.android.hospital.entity.InspectionItemEntity;
import com.android.hospital.entity.NonDrugEntity;
import com.android.hospital.entity.OperationItemEntity;
import com.android.hospital.ui.activity.LoginActivity;
import com.android.hospital.webservice.WebServiceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
* @ClassName: UpdateDBTask 
* @Description: TODO(登录更新任务) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-28 上午11:26:25 
*
 */
public class UpdateDBTask extends AsyncTask<Object, Integer, Object>{

	private LoginActivity mActivity;
	
	private HospitalApp app;
	
	private ArrayList<DrugEntity> drugList;//药品集合
	
	private ArrayList<NonDrugEntity> nondrugList;//非药品集合
	
	private List<Map<String, String>> wayList;//途径集合
	
	private List<Map<String, String>> freqList; //频次集合
	
	private List<Map<String, String>> classList;//检查类别
	
	private List<Map<String, String>> deptList;//发往科室
	
	private List<Map<String, String>> inspectionDeptList;//检验科室
	
	private List<Map<String, String>> inspectionClassList;//检验类别
	
	private ArrayList<InspectionItemEntity> inspectionItemList;//检验项目集合
	
	private ArrayList<OperationItemEntity>  operationItemlist;//手术项目集合
	
	private ArrayList<String> anaesthesiaList;//麻醉项目
	
	private ArrayList<BloodTypeEntity> bloodtypeList;//血液要求集合
	
	private ArrayList<DoctorEntity> doctorList;//医生列表
	
	private ProgressDialog mProgressDialog;
	
	private static final int MAX_PROGRESS = 100;
	
	private int mProgress;
	
    private Handler mProgressHandler;

	
	public UpdateDBTask(Activity activity){
		this.mActivity=(LoginActivity) activity;
		this.app=(HospitalApp) mActivity.getApplication();
		
		mProgressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (mProgress >= MAX_PROGRESS) {
                    mProgressDialog.dismiss();
                } else {
                    mProgress++;
                    mProgressDialog.incrementProgressBy(1);
                    mProgressHandler.sendEmptyMessageDelayed(0, 100);
                }
            }
        };
	}
		
	@Override
	protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setIconAttribute(android.R.attr.alertDialogIcon);
        /*mProgressDialog.setTitle(mActivity.getString(R.string.update_db));*/
        mProgressDialog.setTitle("正在更新..");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(MAX_PROGRESS);
        mProgressDialog.show();
        
        mProgress = 0;
        mProgressDialog.setProgress(0);
        mProgressHandler.sendEmptyMessage(0);
        mProgressDialog.setCancelable(false);
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		startDrugOrNonDrugTask();
		startFreqAndWayTask();
		startClassAndDeptTask();
		startInspectionDeptTask();
		startInspectionItemTask();
		startOperationItemTask();
		startAnaesthesiaItemTask();
		startStaff_doctorTask();
		startBlood_TypeTask();
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		mProgressDialog.setProgress(100);
		mProgressDialog.cancel();
		Toast.makeText(mActivity, "数据导入成功!", Toast.LENGTH_SHORT).show();
		endDrugOrNonDrug();
		endFreqAndTask();
		endClassAndDeptTask();
		endInspectionDeptTask();
		endInspectionItemTask();
		endOperationItemTask();
		endAnaesthesiaItemTask();
		endStaff_doctorTask();
		endBlood_TypeEask();

	}



	
	/**
	 * 
	* @Title: startDrugOrNonDrug 
	* @Description: TODO(获取药品非药品任务) 
	* @param     //设定文件
	* @return void    返回类型 
	* @throws
	 */
	private void startDrugOrNonDrugTask(){
		String drugSql = "select * from input_drug_lists where storage in('301304','301201','301303')";//中药房  西药房'3103','3102'
		String nondrugSql="select * from v_clinic_name_dict";
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(drugSql);
		drugList=DrugEntity.init(dataList);
		ArrayList<DataEntity> dataList2=WebServiceHelper.getWebServiceData(nondrugSql);
		nondrugList=NonDrugEntity.init(dataList2);
	}	
	private void endDrugOrNonDrug(){
		if (drugList.size()==0||nondrugList.size()==0) {
			Toast.makeText(mActivity, "获取数据失败!", Toast.LENGTH_SHORT).show();
		}else {	
			app.setDrugList(drugList);
			app.setNondrugList(nondrugList);
			setPrescriptionMiddleDrug();
		}
	}	
	/**
     * 
    * @Title: setPrescriptionMiddleDrug 
    * @Description: TODO(设置处方中药房药品集合) 
    * @param     //设定文件
    * @return void    返回类型 
    * @throws
     */
	private void setPrescriptionMiddleDrug() {
		// TODO Auto-generated method stub
		ArrayList<DrugEntity> middLeDrugList=new ArrayList<DrugEntity>();
		int size=drugList.size();
		for (int i = 0; i < size; i++) {
			if ("门诊中药房".equals(drugList.get(i).storage_name)) {
				middLeDrugList.add(drugList.get(i));
			}
		}
		app.setMiddleDrugList(middLeDrugList);
	}
	
	/**
	 * 
	* @Title: startFreqAndWayTask 
	* @Description: TODO(获取途径，频次任务) 
	* @param     //设定文件
	* @return void    返回类型 
	* @throws
	 */
	private void startFreqAndWayTask(){
		String freqSql = "select serial_no,freq_desc,freq_counter,freq_interval,freq_interval_units from perform_freq_dict  order by  serial_no";
		String waySql = "select administration_name,serial_no,administration_code,input_code from administration_dict  order by  serial_no";
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(waySql);
		wayList=new ArrayList<Map<String,String>>();
		Map<String, String> mapNull=new TreeMap<String, String>();
		mapNull.put("administration_name", "");
		wayList.add(mapNull);
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> map=new TreeMap<String, String>();
			map.put("administration_name", dataList.get(i).get("administration_name"));
			wayList.add(map);
		}
		ArrayList<DataEntity> dataList2=WebServiceHelper.getWebServiceData(freqSql);
		freqList=new ArrayList<Map<String,String>>();
		Map<String, String> mapNull2=new TreeMap<String, String>();
		mapNull2.put("freq_desc", "");
		mapNull2.put("freq_counter", "");
		mapNull2.put("freq_interval", "");
		mapNull2.put("freq_interval_unit", "");
		freqList.add(mapNull2);
		for (int i = 0; i < dataList2.size(); i++) {
			Map<String, String> map=new TreeMap<String, String>();
			map.put("freq_desc", dataList2.get(i).get("freq_desc").trim());
			map.put("freq_counter", dataList2.get(i).get("freq_counter").trim());
			map.put("freq_interval", dataList2.get(i).get("freq_interval").trim());
			map.put("freq_interval_units", dataList2.get(i).get("freq_interval_units").trim());
			freqList.add(map);
		}	
	}
	
	private void endFreqAndTask(){
		app.setFreqList(freqList);
		app.setWayList(wayList);
	}
	
	/**
	 * 
	* @Title: startClassAndDeptTask 
	* @Description: TODO(获取检查类别和科室) 
	* @param     //设定文件
	* @return void    返回类型 
	* @throws
	 */
	private void startClassAndDeptTask(){
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
	}
	
	private void endClassAndDeptTask(){
		app.setClassList(classList);
		app.setDeptList(deptList);
	}
	
	/**
	 * 
	* @Title: startInspectionDeptTask 
	* @Description: TODO(获取检验科室和类别) 
	* @param     //设定文件
	* @return void    返回类型 
	* @throws
	 */
	private void startInspectionDeptTask(){
		String deptSql = "select distinct  lab_sheet_master.performed_by, dept_dict.dept_name  "
				+ "from dept_dict,  lab_sheet_master   "
				+ "where ( dept_dict.dept_code = lab_sheet_master.performed_by )";
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(deptSql);
		inspectionDeptList=new ArrayList<Map<String,String>>();
		int size=dataList.size();
		Map<String, String> mapNull=new HashMap<String, String>();
		mapNull.put("dept_name", "");
		mapNull.put("performed_by", "");
		inspectionDeptList.add(mapNull);
		for (int i = 0; i < size; i++) {
			Map<String, String> map=new HashMap<String, String>();
			map.put("dept_name", dataList.get(i).get("dept_name"));
			map.put("performed_by", dataList.get(i).get("performed_by"));
			inspectionDeptList.add(map);
		}
		String classSql="select serial_no,class_code,class_name  from lab_item_class_dict";
		ArrayList<DataEntity> dataList2=WebServiceHelper.getWebServiceData(classSql);
		inspectionClassList=new ArrayList<Map<String,String>>();
		int size2=dataList2.size();
		Map<String, String> mapNull2=new HashMap<String, String>();
		mapNull2.put("class_name", "");
		inspectionClassList.add(mapNull2);
		for (int i = 0; i < size2; i++) {
			Map<String, String> map=new HashMap<String, String>();
			map.put("class_name", dataList2.get(i).get("class_name"));
			inspectionClassList.add(map);
		}
	}
	
	private void endInspectionDeptTask(){
		app.setInspecitonClassList(inspectionClassList);
		app.setInspectionDeptList(inspectionDeptList);
	}
	
	/**
	 * 
	* @Title: startInspectionItemTask 
	* @Description: TODO(检验项目任务) 
	* @param     //设定文件
	* @return void    返回类型 
	* @throws
	 */
	private void startInspectionItemTask(){
		String sql="select * from v_clab_name_dict";
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
		inspectionItemList=new ArrayList<InspectionItemEntity>();
		int size=dataList.size();
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
			inspectionItemList.add(entity);
		}
	}
	
	private void endInspectionItemTask(){
		app.setInspectionItemList(inspectionItemList);
	}
	
	/**
	 * 
	* @Title: startOperationItemTask 
	* @Description: TODO(手术项目任务) 
	* @param     //设定文件
	* @return void    返回类型 
	* @throws
	 */
	private void startOperationItemTask(){
		String sql="select * from operation_dict";
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
		operationItemlist=new ArrayList<OperationItemEntity>();
		int size=dataList.size();
		for (int i = 0; i < size; i++) {
			OperationItemEntity entity=new OperationItemEntity();
			entity.operation_code=dataList.get(i).get("operation_code").trim();
			entity.operation_name=dataList.get(i).get("operation_name").trim();
			entity.operation_scale=dataList.get(i).get("operation_scale").trim();
			entity.std_indicator=dataList.get(i).get("std_indicator").trim();
			entity.input_code=dataList.get(i).get("input_code").trim();
			entity.approved_indicator=dataList.get(i).get("approved_indicator").trim();
			operationItemlist.add(entity);
		}
	}
	
	private void endOperationItemTask(){
		app.setOperationItemList(operationItemlist);
	}
	/**
	 * 
	* @Title: startAnaesthesiaItemTask 
	* @Description: TODO(麻醉项目任务) 
	* @param     //设定文件
	* @return void    返回类型 
	* @throws
	 */
	private void startAnaesthesiaItemTask(){
		String sqlString="select anaesthesia_code,anaesthesia_name,serial_no  from anaesthesia_dict ";
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sqlString);
		anaesthesiaList=new ArrayList<String>();
		int size=dataList.size();
		for (int i = 0; i < size; i++) {
			String item="";
			item=dataList.get(i).get("anaesthesia_name").trim();
			anaesthesiaList.add(item);
		}
	}
	private void endAnaesthesiaItemTask(){
		app.setAnaesthesiaList(anaesthesiaList);
	}
	/**
	 * 
	* @Title: startStaff_doctorTask 
	* @Description: TODO(医生列表任务) 
	* @param     //设定文件
	* @return void    返回类型 
	* @throws
	 */
	private void startStaff_doctorTask(){
		String sqlString="select name,input_code,title,user_name from v_staff_doctor";
		ArrayList<DataEntity> arrayList=WebServiceHelper.getWebServiceData(sqlString);
		doctorList=new ArrayList<DoctorEntity>();
		int size=arrayList.size();
		for (int i = 0; i < size; i++) {
		    DoctorEntity  item=new  DoctorEntity();
			item.name=arrayList.get(i).get("name").trim();
			item.input_code=arrayList.get(i).get("input_code").trim();
			doctorList.add(item);
		}
	}
	private void endStaff_doctorTask(){
		app.setStaff_doctorList(doctorList);
	}
	/**
	 * 
	* @Title: startBlood_TypeTask 
	* @Description: TODO(血液要求任务) 
	* @param     //设定文件
	* @return void    返回类型 
	* @throws
	 */
	private void startBlood_TypeTask(){
		String sqlString="select blood_type_name,blood_type,blood_match,useful_life,temperature from blood_component ";
		ArrayList<DataEntity> arrayList=WebServiceHelper.getWebServiceData(sqlString);
		bloodtypeList=new ArrayList<BloodTypeEntity>();
		int size=arrayList.size();
		for (int i = 0; i < size; i++) {
			BloodTypeEntity item=new BloodTypeEntity();
			item.blood_type=arrayList.get(i).get("blood_type").trim();
			item.blood_type_name=arrayList.get(i).get("blood_type_name").trim();
			item.useful_life=arrayList.get(i).get("useful_life").trim();
			item.blood_match=arrayList.get(i).get("blood_match").trim();
			item.temperature=arrayList.get(i).get("temperature").trim();
			bloodtypeList.add(item);
		}
	}
	private void endBlood_TypeEask(){
		app.setBlood_TypeList(bloodtypeList);
	}
	
}
