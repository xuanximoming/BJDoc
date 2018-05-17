package com.android.hospital.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.InspectionLeftItemAdapter;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.InspectionItemEntity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
* @ClassName: AddInspectionFragment 
* @Description: TODO(新增检验界面) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-21 下午11:54:54 
*
 */
public class AddInspectionFragment extends BaseFragment implements OnItemSelectedListener{

    private View view;   
	private Spinner mInspectionDeptSp,mClassSp,mSpecimenSp;//检验科室，类别，标本
	private EditText mSpecimenEdit;//标本说明
	private TextView mTempJudgeTev,mDoctorTev,mTimeTev;//临床诊断，送检医生，申请时间
	private CheckBox mCheckBox;//急诊
	private List<Map<String, String>> mDeptList;//检验科室
	private List<Map<String, String>> mClassList;//检验类别
	private HospitalApp app;
	private InspectionItemEntity inspectionItemEntity;//检验项目实体
	private InspectionLeftItemAdapter mAdapter;
	private ListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.app=(HospitalApp) getActivity().getApplication();
		this.mDeptList=app.getInspectionDeptList();//获取检验科室列表
		this.mClassList=app.getInspecitonClassList();//获取检验类别列表
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_add_inspection_left, null);
		init();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mAdapter=new InspectionLeftItemAdapter(getActivity(), null);
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		mInspectionDeptSp=(Spinner) view.findViewById(R.id.add_inspection_spinner_1);
		mClassSp=(Spinner) view.findViewById(R.id.add_inspection_spinner_2);
		mSpecimenSp=(Spinner) view.findViewById(R.id.add_inspection_spinner_3);
		mSpecimenEdit=(EditText) view.findViewById(R.id.add_inspection_edit_1);
		mTempJudgeTev=(TextView) view.findViewById(R.id.add_inspection_tev_1);
		mDoctorTev=(TextView) view.findViewById(R.id.add_inspection_tev_2);
		mTimeTev=(TextView) view.findViewById(R.id.add_inspection_tev_3);
		mCheckBox=(CheckBox) view.findViewById(R.id.add_inspection_checkbox);
		mListView=(ListView) view.findViewById(R.id.add_inspection_listview);
		
		mInspectionDeptSp.setOnItemSelectedListener(this);
		mClassSp.setOnItemSelectedListener(this);
		mSpecimenSp.setOnItemSelectedListener(this);
		mTempJudgeTev.setText(app.getPatientEntity().diagnosis);//诊断
		mDoctorTev.setText(app.getDoctor()); //医生
		mTimeTev.setText(Util.toSimpleDate());//申请时间（系统时间）
		setSpinner();
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
		//检验类别
		List<String> freqSpList=new ArrayList<String>();
		for (int i = 0; i < mClassList.size(); i++) {
			String item=mClassList.get(i).get("class_name");
			freqSpList.add(item);
		}
		ArrayAdapter<String> freqAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, freqSpList);
		freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mClassSp.setAdapter(freqAdapter);
		
		//检验科室
		List<String> waySpList=new ArrayList<String>();
		for (int i = 0; i < mDeptList.size(); i++) {
			String item=mDeptList.get(i).get("dept_name");
			waySpList.add(item);
		}
		ArrayAdapter<String> wayAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, waySpList);
		wayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mInspectionDeptSp.setAdapter(wayAdapter);
	}
	
	/**
	 * 
	* @Title: validate 
	* @Description: TODO(判断选项是否符合) 
	* @param     确定保存前，对所选内容进行检测，若满足则保存，否则弹出提示信息
	* @return void    返回类型 
	* @throws
	 */
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		if (mAdapter.getCount()!=0) {
			return true;
		}
		Toast.makeText(getActivity(), "请选择项目!", Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public void onOtherFragmentClick() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	* @Title: clear 
	* @Description: TODO(清空列表) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void clear(){
		mAdapter.clear(); //清空adapter
        setSpinner(); //spinner重新设置
	}
	
	/**
	 * 
	* @Title: setListView 
	* @Description: TODO(设置选中的项目) 
	* @param     选中的项目科室、标本、类别与第一个不同时，不能选择
	* @return void    返回类型 
	* @throws
	 */
	public void setListView(InspectionItemEntity entity){
		DebugUtil.debug("expand1-->"+entity.expand1+","+"expand2-->"+entity.expand2+","+"expand3-->"+entity.expand3);
		inspectionItemEntity=entity;
		String expand1=mSpecimenSp.getItemAtPosition(mSpecimenSp.getSelectedItemPosition()).toString();
		String expand2=mClassSp.getItemAtPosition(mClassSp.getSelectedItemPosition()).toString();
		String expand3=getPerformed_by();//通过科室名称找到科室代码
		DebugUtil.debug("spinner1-->"+expand1+","+"spinner2-->"+expand2+","+"spinner3-->"+expand3);
		if (!expand3.equals("")) {
			if (!inspectionItemEntity.expand3.equals("")) {
				if (!expand3.equals(inspectionItemEntity.expand3)) {
					DebugUtil.debug("执行到1，被返回");
					return;
				}
			}
		}
		if (!expand2.equals("")) {
			if (!inspectionItemEntity.expand2.equals("")) {
				if (!expand2.equals(inspectionItemEntity.expand2)) {
					DebugUtil.debug("执行到2，被返回");
					return;
				}
			}
		}
		if (!expand1.equals("")) {
			if (!inspectionItemEntity.expand1.equals("")) {
				if (!expand1.equals(inspectionItemEntity.expand1)) {
					DebugUtil.debug("执行到3，被返回");
					return;
				}
			}
		}
		if (!inspectionItemEntity.expand3.equals("")) {//实体的expand3检验科室
			List<String> list=new ArrayList<String>();
			for (int i = 0; i < mDeptList.size(); i++) {//通过科室代码找到科室名称
				if (inspectionItemEntity.expand3.equals(mDeptList.get(i).get("performed_by"))) {
					list.add(mDeptList.get(i).get("dept_name"));
					break;
				}
			}	
			ArrayAdapter<String> wayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, list);
			wayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mInspectionDeptSp.setAdapter(wayAdapter);
		}
		if (!inspectionItemEntity.expand2.equals("")) {
			List<String> list=new ArrayList<String>();
			list.add(inspectionItemEntity.expand2);
			ArrayAdapter<String> wayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, list);
			wayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mClassSp.setAdapter(wayAdapter);
		}
		if (!inspectionItemEntity.expand1.equals("")) {//实体的expand1为标本
			List<String> list=new ArrayList<String>();
			list.add(inspectionItemEntity.expand1);
			ArrayAdapter<String> wayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, list);
			wayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSpecimenSp.setAdapter(wayAdapter);
		}
		mAdapter.addItem(inspectionItemEntity);		
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
		inspectionInsertSql();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.add_inspection_spinner_1:
			String params=mDeptList.get(position).get("performed_by");
			if (inspectionItemEntity!=null) {
				if (inspectionItemEntity.expand1.equals("")) {//如果传过来的标本属性为空，则根据科室异步去加载
					new SpecimenTask().execute(params);
				}/*else {//不为空，则重新setAdapter
					List<String> list=new ArrayList<String>();
					list.add(inspectionItemEntity.expand1);
					ArrayAdapter<String> wayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, list);
					wayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					mSpecimenSp.setAdapter(wayAdapter);
				}		*/	
			}else {
				new SpecimenTask().execute(params);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	* @ClassName: SpecimenTask 
	* @Description: TODO(获取标本) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2012-12-26 上午8:09:30 
	*
	 */
	private class SpecimenTask extends AsyncTask<String, Void, List<String>>{

		@Override
		protected List<String> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String sql="select speciman_code,speciman_name  from speciman_dict  where dept_code = '"+params[0]+"'";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
			int size=dataList.size();
			List<String> specimanList=new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				specimanList.add(dataList.get(i).get("speciman_name").trim());
			}
			if (size==0) {
				specimanList.add("");
			}
			return specimanList;
		}
		
		@Override
		protected void onPostExecute(List<String> result) {
			// TODO Auto-generated method stub
			ArrayAdapter<String> wayAdapter = new ArrayAdapter(getActivity(),
					android.R.layout.simple_spinner_item, result);
			wayAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSpecimenSp.setAdapter(wayAdapter);
		}
	}
	
	/**
	 * 
	* @Title: inspectionInsertSql 
	* @Description: TODO(检验插入方法) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void inspectionInsertSql(){
		String test_no=Util.toTest_No(app.getNextval());//检验序号
		String priority_indicator="0";
		String requested_date_time=Util.toSimpleDate();
		String performed_by=getPerformed_by();//项目代码
		String age=String.valueOf(Util.userBirthdayGetAge(app.getPatientEntity().date_of_birth));
		if (mCheckBox.isChecked()) {
			priority_indicator="1";
		}	
		StringBuffer inspectionBuffer=new StringBuffer();//有些不必要的字段是否可以不用插入
		inspectionBuffer.append("insert into lab_test_master "
						+ "(test_no ,priority_indicator ,patient_id ,visit_id ,name , "
						+ "sex ,age ,relevant_clinic_diag ,specimen ,notes_for_spcm , "
						+ "requested_date_time ,ordering_dept ,ordering_provider ,performed_by ,name_phonetic ,"
						+ "charge_type ,result_status )" + "values (");
		inspectionBuffer.append("'"+test_no).append("',");
		inspectionBuffer.append("'"+priority_indicator).append("',");
		inspectionBuffer.append("'"+app.getPatientEntity().patient_id).append("',");
		inspectionBuffer.append("'"+app.getPatientEntity().visit_id).append("',");
		inspectionBuffer.append("'"+app.getPatientEntity().name).append("',");
		inspectionBuffer.append("'"+app.getPatientEntity().sex).append("',");
		inspectionBuffer.append("'"+age).append("',");//需计算病人年龄
		inspectionBuffer.append("'"+app.getPatientEntity().diagnosis).append("',");
		inspectionBuffer.append("'"+mSpecimenSp.getSelectedItem().toString()).append("',");
		inspectionBuffer.append("'"+mSpecimenEdit.getText().toString()).append("',");
		inspectionBuffer.append("TO_DATE('"+requested_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
		inspectionBuffer.append("'"+app.getPatientEntity().dept_code).append("',");
		inspectionBuffer.append("'"+app.getLoginName()).append("',"); //保存医生的用户名
		inspectionBuffer.append("'"+performed_by).append("',");
		inspectionBuffer.append("'"+app.getPatientEntity().name_phonetic).append("',");
		inspectionBuffer.append("'"+app.getPatientEntity().charge_type).append("',");
		inspectionBuffer.append("'"+"1").append("')");
		WebServiceHelper.insertWebServiceData(inspectionBuffer.toString());//检验插入数据1
		
		int max_no=Integer.parseInt(app.getMaxNumber())-1;//最大序号
		int count=mAdapter.getCount();
		for (int i = 0; i < count; i++) {
			int item_no=i+1;
			InspectionItemEntity itemEntity=(InspectionItemEntity) mAdapter.getItem(i);
			String lab_test_itemssql = "insert into lab_test_items "
					              + "(test_no ,item_no ,item_name ,item_code )" + "values "
					              + "('" + test_no + "','" + item_no + "','"
					              + itemEntity.item_name + "','" + itemEntity.item_code
					              + "')";
			WebServiceHelper.insertWebServiceData(lab_test_itemssql);	
			max_no++;
			StringBuffer orderBuffer=new StringBuffer();
			orderBuffer.append("insert into orders " + " (patient_id,"
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
			orderBuffer.append("TO_DATE('"+requested_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
			orderBuffer.append("'"+"0").append("',");
			orderBuffer.append("'"+"C").append("',");
			orderBuffer.append("'"+itemEntity.item_name).append("',");
			orderBuffer.append("'"+itemEntity.item_code).append("',");
			orderBuffer.append("'"+"6").append("',");
			orderBuffer.append("'"+app.getPatientEntity().dept_code).append("',");
			orderBuffer.append("'"+app.getDoctor()).append("',");
			orderBuffer.append("'"+app.getLoginName()).append("',");
			orderBuffer.append("TO_DATE('"+requested_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
			orderBuffer.append("'"+"3").append("',");
			orderBuffer.append("'"+"3").append("',");
			orderBuffer.append("'"+test_no).append("')");
			WebServiceHelper.insertWebServiceData(orderBuffer.toString());
	    }	
	}
	
    /**
     * 
    * @Title: getPerformed_by 
    * @Description: TODO(通过科室名称找到科室代码) 
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
	public String getPerformed_by(){
		String expand3="";
		for (int i = 0; i < mDeptList.size(); i++) {//通过科室名称找到科室代码
			String temp=mInspectionDeptSp.getItemAtPosition(mInspectionDeptSp.getSelectedItemPosition()).toString();
			if (temp.equals(mDeptList.get(i).get("dept_name"))) {
				//DebugUtil.debug("判断---->"+temp+","+mDeptList.get(i).get("dept_name"));
				expand3=mDeptList.get(i).get("performed_by");
			}
		}
		return expand3;
	}
}
