package com.android.hospital.asyntask;

import java.util.ArrayList;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.PatientAdapter;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.ui.fragment.LeftListFragment;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.webservice.WebServiceHelper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
/**
 * 
* @ClassName: DepartmentTask 
* @Description: TODO(获取部门信息) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 上午8:54:24 
*
 */
public class DepartmentTask extends BaseAsyncTask implements OnItemSelectedListener{
	
	private Activity mActivity;
	
	private String sql;
	private HospitalApp app;
	private ArrayList<String> codeArrayList;//部门代码，通过部门代码获取病人列表
	private ArrayList<String> nameArrayList;//部门名称

	public DepartmentTask(Activity activity,String sql){
		this.mActivity=activity;
		this.sql=sql;
		app=(HospitalApp) mActivity.getApplication();
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		DebugUtil.debug("sql--->"+sql);
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
		ArrayList<String> arrayList=new ArrayList<String>();
		codeArrayList=new ArrayList<String>();
		nameArrayList=new ArrayList<String>();
		for (int i = 0; i < dataList.size(); i++) {
			String item=dataList.get(i).get("dept_name");
			String code=dataList.get(i).get("group_code");
			arrayList.add(item);
			codeArrayList.add(code);
			nameArrayList.add(item);
            Log.d("DepartmentTask","name="+item);
            Log.d("DepartmentTask","group_code="+code);
		}
		app.setDepartcodeList(codeArrayList);
		app.setDepartnameList(nameArrayList);
		return arrayList;
	}

	@Override
	protected void onPostExecute(Object result) {
		if (result!=null) {
			ArrayList<String> arrayList=(ArrayList<String>) result;
			/////////2013.02.19修改，在科室后添加了一个 医生自己负责的病人/////////////////////
			ArrayList<String> arraynew=new ArrayList<String>();
			arraynew.add("我的患者");
			for (int i = 0; i < arrayList.size(); i++) {
				String item=arrayList.get(i);
				arraynew.add(item);
			}
			//////////////////////////////////////////////////////////////////////////////
			Spinner spinner=(Spinner) mActivity.findViewById(R.id.main_left_department_spinner);
			ArrayAdapter<String> sAdapter=new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, arraynew);
	        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner.setAdapter(sAdapter);
	        spinner.setOnItemSelectedListener(this);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		LeftListFragment fm=(LeftListFragment) mActivity.getFragmentManager().findFragmentByTag("leftfm");
		if (fm!=null) {
			PatientAdapter adapter=(PatientAdapter) fm.getListAdapter();
			if (null!=adapter&&adapter.getCount()!=0) {
				adapter.clearAdapter();
				if (fm.isAdded()) {
					fm.setListShown(false);
				}
				DebugUtil.debug("spinner里病人集合--->"+fm.getListAdapter().getCount());//如果病人有，则清空,后期添加
			}		
		}
		//////////当position=0时，为该登陆医生的病人，不需要科室代码////////////
		String code="";
		if (position>0) {
			code=codeArrayList.get(position-1);
		}
		/////////////////////////////////////////////////////
		String tableName="pats_in_hospital, pat_master_index, mr_on_line,bed_rec";
		String[] paramArray1=new String[]{"pats_in_hospital.dept_code","bed_rec.bed_label","pats_in_hospital.bed_no","pats_in_hospital.diagnosis","prepayments - total_charges as charge",
				                          "pats_in_hospital.doctor_in_charge","mr_on_line.request_doctor_id as user_name","pats_in_hospital.patient_id","pats_in_hospital.prepayments",
				                          "pat_master_index.name","pat_master_index.sex","pat_master_index.name_phonetic","pat_master_index.date_of_birth",
				                          "pat_master_index.birth_place","pat_master_index.identity","pat_master_index.mailing_address","pat_master_index.zip_code",
				                          "pat_master_index.phone_number_home","pat_master_index.charge_type",
				                          "pats_in_hospital.visit_id","pat_master_index.inp_no","pat_master_index.nation","pat_master_index.id_no",
				                          "TO_CHAR(pats_in_hospital.admission_date_time,'yyyy-MM-dd hh24:mi:ss') as admission_date_time"};
//		String customWhere="where pats_in_hospital.patient_id = pat_master_index.patient_id " 
//				                          +"and pats_in_hospital.patient_id = mr_on_line.patient_id "
//				                          +"and pats_in_hospital.visit_id = mr_on_line.visit_id "
//				                          +"and pats_in_hospital.doctor_in_charge=staff_dict.name "
//				                          +"and mr_on_line.status = '0' "
//				                          +"and pats_in_hospital.dept_code = '"+code+"' "
//				                          +"order by pats_in_hospital.bed_no";
		
		///////////////按照1.登陆医生（ownerString） 2.科室（allString）获取病人信息 ////////////////////////////
		String customWhere="where pats_in_hospital.patient_id = pat_master_index.patient_id " 
                +"and pats_in_hospital.patient_id = mr_on_line.patient_id "
                +"and pats_in_hospital.visit_id = mr_on_line.visit_id "
                +"and mr_on_line.status = '0' "
                +"and bed_rec.bed_no = pats_in_hospital.bed_no "
                +"and bed_rec.ward_code = pats_in_hospital.ward_code ";
        String allString= "and pats_in_hospital.dept_code = '"+code+"' "
                +"order by pats_in_hospital.bed_no";
        String ownerString="and mr_on_line.request_doctor_id = '"+app.getLoginName()+"'"
        		+"order by pats_in_hospital.bed_no";
		String sql=ServerDao.getQueryCustom(tableName, paramArray1, customWhere);
		if (position==0) {
			sql=sql+ownerString;
		}else {
			 sql=sql+allString;
		}
		////////////////////////////////////////////////////////////////////
		Log.e("病人信息--->", sql);
		new PatientTask(mActivity, sql).execute();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub		
	}
	
}
