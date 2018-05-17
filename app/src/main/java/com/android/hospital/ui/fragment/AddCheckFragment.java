package com.android.hospital.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.CheckItemAdapter;
import com.android.hospital.adapter.CheckLeftItemAdapter;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
* @ClassName: AddDcAdviceFragment 
* @Description: TODO(新增检查界面布局) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:34:25 
*
 */
public class AddCheckFragment extends BaseFragment implements OnItemSelectedListener{

    private View view;  
    private Spinner mClassSp,mSubClassSp,mDeptSp;//类别，子类别，发往科室    
    private TextView mTempJudgeTev;//临床诊断 
    private EditText mSymptomEdit,mSignsEdit;//症状，体征   
    private ListView mListView;    
    private HospitalApp app;   
	private List<Map<String, String>> mClassList;//类别集合	
	private List<Map<String, String>> mDeptList;//科室集合	
	private SeachCheckFragment fm;//搜索界面	
	private CheckLeftItemAdapter adapter;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.app=(HospitalApp) getActivity().getApplication();
		this.mClassList=app.getClassList();
		this.mDeptList=app.getDeptList();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_add_check_left, null);
		init();
		setSpinner();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		fm=(SeachCheckFragment) getActivity().getFragmentManager().findFragmentByTag("searchfm");
		adapter=new CheckLeftItemAdapter(getActivity(), null);
		mListView.setAdapter(adapter);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		mClassSp=(Spinner) view.findViewById(R.id.add_check_spinner_class);
		mSubClassSp=(Spinner) view.findViewById(R.id.add_check_spinner_class_sub);
		mDeptSp=(Spinner) view.findViewById(R.id.add_check_spinner_sendkeshi);
		mTempJudgeTev=(TextView) view.findViewById(R.id.add_check_tev_1);
		mSymptomEdit=(EditText) view.findViewById(R.id.add_check_edit_1);
		mSignsEdit=(EditText) view.findViewById(R.id.add_check_edit_2);
		mListView=(ListView) view.findViewById(R.id.add_check_listview);		
		mTempJudgeTev.setText(app.getPatientEntity().diagnosis);//临床诊断
		mClassSp.setOnItemSelectedListener(this);
		mSubClassSp.setOnItemSelectedListener(this);
	}

	/**
	 * 
	* @Title: setSpinner 
	* @Description: TODO(设置spinner显示信息) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void setSpinner(){
		//类别设置
		List<String> freqSpList=new ArrayList<String>();
		for (int i = 0; i < mClassList.size(); i++) {
			String item=mClassList.get(i).get("exam_class_name");
			freqSpList.add(item);
		}
		ArrayAdapter<String> freqAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, freqSpList);
		freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mClassSp.setAdapter(freqAdapter);
		//发往科室设置
		List<String> waySpList=new ArrayList<String>();
		for (int i = 0; i < mDeptList.size(); i++) {
			String item=mDeptList.get(i).get("dept_name");
			waySpList.add(item);
		}
		ArrayAdapter<String> wayAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, waySpList);
		wayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDeptSp.setAdapter(wayAdapter);
	}

	/**
	 * 
	* @Title: validate 
	* @Description: TODO(是否有必填信息) 
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		if (adapter.getCount()!=0) {
			return true;
		}
		Toast.makeText(getActivity(), "请选择项目!", Toast.LENGTH_SHORT).show();
		return false;
	}

	/**
	 * 
	* @Title: setListView 
	* @Description: TODO(设置选中的项目) 
	* @param     
	* @return void    返回类型 
	* @throws
	 */
	public void setListView(Map<String, String> map){
		adapter.addItem(map);
	}
	
	/**
	 * 
	* @Title: insert 
	* @Description: TODO(插入函数) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@Override
	public void insert() {
		// TODO Auto-generated method stub
		checkInsertSql();		
	}	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.add_check_spinner_class:
			String exam_class_name=parent.getItemAtPosition(position).toString();
			new SubClassTask().execute(exam_class_name);
			break;
		case R.id.add_check_spinner_class_sub:
			if (fm!=null) {
				CheckItemAdapter adapter=(CheckItemAdapter) fm.getListView().getAdapter();
				if (null!=adapter) {
					adapter.clear();
				}
			}
			String exam_sub_class=parent.getItemAtPosition(position).toString();
			new CheckItemTask().execute(exam_sub_class);
			break;
		default:
			break;
		}		
	}

	/**
	 * 
	* @ClassName: SubClassTask 
	* @Description: TODO(获取子类别 ) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2012-12-25 下午12:02:44 
	*
	 */
	private class SubClassTask extends AsyncTask<String, Void, String>{

		private List<String> subClassList;
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String subClassSql = "select exam_subclass_name,serial_no from exam_subclass_dict where exam_class_name='"
					+ params[0] + "'";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(subClassSql);
			subClassList=new ArrayList<String>();
			int size=dataList.size();
			for (int i = 0; i < size; i++) {
				subClassList.add(dataList.get(i).get("exam_subclass_name"));
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			ArrayAdapter<String> subClassAdapter = new ArrayAdapter(
					getActivity(), android.R.layout.simple_spinner_item, subClassList);
			subClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSubClassSp.setAdapter(subClassAdapter);
		}
	}
	
	/**
	 * 
	* @ClassName: CheckItemTask 
	* @Description: TODO(获取检查项目任务) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2012-12-25 下午1:38:27 
	*
	 */
	private class CheckItemTask extends AsyncTask<String, Void, ArrayList<Map<String, String>>>{

		@Override
		protected ArrayList<Map<String, String>> doInBackground(String... params) {
			
			String sql = "select description,description_code,input_code from exam_rpt_pattern where (exam_sub_class='"
					+ params[0] + "' and desc_item='检查项目')";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
			ArrayList<Map<String, String>> checkItemList=new ArrayList<Map<String,String>>();
			int size=dataList.size();
			for (int i = 0; i < size; i++) {
				Map<String, String> map=new HashMap<String, String>();
				map.put("description", dataList.get(i).get("description"));
				map.put("description_code", dataList.get(i).get("description_code"));
				map.put("input_code", dataList.get(i).get("input_code"));
				checkItemList.add(map);
			}
			return checkItemList;
		}

		@Override
		protected void onPostExecute(ArrayList<Map<String, String>> result) {
			// TODO Auto-generated method stub	
			CheckItemAdapter adapter=new CheckItemAdapter(getActivity(),R.layout.fragment_add_checkitem_list_item,result);
			fm.show(adapter);
		}

	}
	
	/**
	 * 
	* @Title: getCheckInsertSql 
	* @Description: TODO(检查插入方法) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private void checkInsertSql(){
		StringBuffer checkBuffer=new StringBuffer();
		String req_date_time=Util.toSimpleDate();
		String date_of_birth=Util.toDateOfBirth(app.getPatientEntity().date_of_birth);
		checkBuffer.append("insert into exam_appoints "
						+ "(exam_no,patient_id,name,name_phonetic,"
						+ "sex,date_of_birth,birth_place,identity,charge_type,mailing_address,"
						+ "zip_code,phone_number,exam_class,exam_sub_class,clin_symp,phys_sign,clin_diag,"
						+ "performed_by,patient_source,facility,req_date_time,req_dept,req_physician,"
						+ "visit_id,doctor_user) " + "values " + "(");
		checkBuffer.append("'"+app.getNextval()).append("',");
		checkBuffer.append("'"+app.getPatientEntity().patient_id).append("',");
		checkBuffer.append("'"+app.getPatientEntity().name).append("',");
		checkBuffer.append("'"+app.getPatientEntity().name_phonetic).append("',");
		checkBuffer.append("'"+app.getPatientEntity().sex).append("',");
		checkBuffer.append("TO_DATE('"+date_of_birth).append("','yyyy-MM-dd hh24:mi:ss'),");
		checkBuffer.append("'"+app.getPatientEntity().birth_place).append("',");
		checkBuffer.append("'"+app.getPatientEntity().identity).append("',");
		checkBuffer.append("'"+app.getPatientEntity().charge_type).append("',");
		checkBuffer.append("'"+app.getPatientEntity().mailing_address).append("',");
		checkBuffer.append("'"+app.getPatientEntity().zip_code).append("',");
		checkBuffer.append("'"+app.getPatientEntity().phone_number_home).append("',");
		checkBuffer.append("'"+mClassSp.getItemAtPosition(mClassSp.getSelectedItemPosition())).append("',");
		checkBuffer.append("'"+mSubClassSp.getItemAtPosition(mSubClassSp.getSelectedItemPosition())).append("',");
		checkBuffer.append("'"+mSymptomEdit.getText().toString()).append("',");
		checkBuffer.append("'"+mSignsEdit.getText().toString()).append("',");
		checkBuffer.append("'"+app.getPatientEntity().diagnosis).append("',");
		checkBuffer.append("'"+mDeptList.get(mDeptSp.getSelectedItemPosition()).get("dept_code")).append("',");//科室代码
		checkBuffer.append("'"+"2").append("',");
		checkBuffer.append("'"+"").append("',");
		checkBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
		checkBuffer.append("'"+app.getPatientEntity().dept_code).append("',");
		checkBuffer.append("'"+app.getDoctor()).append("',");
		checkBuffer.append("'"+app.getPatientEntity().visit_id).append("',");
		checkBuffer.append("'"+app.getLoginName()).append("')");
		WebServiceHelper.insertWebServiceData(checkBuffer.toString());//检查插入数据1
		
		int max_no=Integer.parseInt(app.getMaxNumber())-1;//最大序号
		int count=adapter.getCount();
		for (int i = 0; i < count; i++) {
			int exam_item_no=i+1;
			Map<String, String> map=(Map<String, String>) adapter.getItem(i);
			String exam_itemsSQL = "insert into exam_items ("
					+ "exam_no,exam_item_no,exam_item,exam_item_code) "
					+ "values (" + "'" + app.getNextval() + "'," + "'"
					+ String.valueOf(exam_item_no) + "'," + "'" + map.get("description")
					+ "'," + "'" + map.get("description_code") + "')";
			WebServiceHelper.insertWebServiceData(exam_itemsSQL);//检查插入数据2
			
			max_no++;
			StringBuffer orderBuffer=new StringBuffer();
			orderBuffer.append("insert into orders" + " (patient_id,"
					        + "  visit_id," + "  order_no," + "  order_sub_no,"
					        + "  start_date_time," + "  repeat_indicator,"
					        + "  order_class," + "  order_text," + "  order_code,"
					        + "  order_status," + "  ordering_dept," + "  doctor,"
					        + "  doctor_user," + "  enter_date_time," + "  billing_attr,"
					        + "  drug_billing_attr," + "  app_no)" + "values (");
			orderBuffer.append("'"+app.getPatientEntity().patient_id).append("',");
			orderBuffer.append("'"+app.getPatientEntity().visit_id).append("',");
			orderBuffer.append("'"+max_no).append("',");
			orderBuffer.append("'"+"1").append("',");
			orderBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
			orderBuffer.append("'"+"0").append("',");
			orderBuffer.append("'"+"D").append("',");
			orderBuffer.append("'"+map.get("description")).append("',");
			orderBuffer.append("'"+map.get("description_code")).append("',");
			orderBuffer.append("'"+"6").append("',");
			orderBuffer.append("'"+app.getPatientEntity().dept_code).append("',");
			orderBuffer.append("'"+app.getDoctor()).append("',");
			orderBuffer.append("'"+app.getLoginName()).append("',");
			orderBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
			orderBuffer.append("'"+"3").append("',");
			orderBuffer.append("'"+"3").append("',");
			orderBuffer.append("'"+app.getNextval()).append("')");
			WebServiceHelper.insertWebServiceData(orderBuffer.toString());
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onOtherFragmentClick() {
		// TODO Auto-generated method stub
	}
}
