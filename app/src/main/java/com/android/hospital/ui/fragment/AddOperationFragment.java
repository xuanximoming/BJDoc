package com.android.hospital.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.DoctorAdapter;
import com.android.hospital.adapter.OperationLeftItemAdapter;
import com.android.hospital.adapter.OperationPatientAdapter;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.DoctorEntity;
import com.android.hospital.entity.OperationItemEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.ui.activity.AddOperationActivity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;
import android.R.integer;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.provider.ContactsContract.Contacts.Data;
import android.text.TextUtils;
import android.util.DebugUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
* @ClassName: AddOperationFragment 
* @Description: TODO(新增手术界面布局) 
* @author lll 
* @date 2013-02-24 
*
 */
public class AddOperationFragment extends BaseFragment implements OnItemClickListener,OnItemSelectedListener{

    private View view; 
    private TextView patient_info; //病人基本信息显示
    private Spinner sbingqing,ssshi,ssjian,sgeli,sdengji,smazui;
    private Spinner department;//科室spinner
    private EditText mdiagnoise,mnote,mdoctor;//诊断、备注、申请医生
    private EditText mtaici,sskeshi;//台次,手术科室
    private EditText operationdate,sqdate;//手术日期，申请日期 
    private ListView mpatient;//病人列表
    private List<String> mDeptList;//科室列表
    private List<String> mList;//麻醉列表
    private HospitalApp app; 
    private PatientEntity entity;//病人实体
    private ArrayList<PatientEntity> arrayList; //病人实体列表
	private OperationLeftItemAdapter mAdapter;//左侧list
	private ListView mListView;//存放选中的手术项目
	private OperationItemEntity operationItemEntity;//手术项目实体
	private String schedule_id;//手术安排标识，在operation_schedule，scheduled_operation_name表中需要用到
	private DoctorEntity doctorEntity;//医生实体
	private ArrayList<DoctorEntity>  doctorList ;//医生列表
	private Button bt_doctor,bt_zhushou01,bt_zhushou02,bt_zhushou03,bt_zhushou04,bt_sxyishi;//手术医生及助手 ,输血医师
	private TextView tv_doctor,tv_zhushou01,tv_zhushou02,tv_zhushou03,tv_zhushou04,tv_sxyishi;//手术医生及助手， ,输血医师
	public  int flag = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.app=(HospitalApp) getActivity().getApplication();
		this.mDeptList=app.getDepartnameList(); //获取科室名称列表
		this.mList=app.getAnaesthesiaList(); //获取麻醉项目列表
		this.doctorList = app.getStaff_doctorList();//获取医生姓名列表
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_add_operation_left, null);
		init();
		setSpinner();//设置spinner
		new OperationPatientTask().execute();// 病人列表
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mAdapter=new OperationLeftItemAdapter(getActivity(), null);
		mListView.setAdapter(mAdapter);
	}
	
	/**
	 * 
	* @Title: initView 
	* @Description: TODO(初始化控件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub
		patient_info=(TextView) view.findViewById(R.id.patient_infprmation);
		sbingqing=(Spinner) view.findViewById(R.id.operation_spinner_bingqing);
		ssshi=(Spinner) view.findViewById(R.id.operation_spinner_shoushushi);
		ssjian=(Spinner) view.findViewById(R.id.operation_spinner_shoushujian);
		sgeli=(Spinner) view.findViewById(R.id.operation_spinner_geli);
		sdengji=(Spinner) view.findViewById(R.id.operation_spinner_dengji);
		bt_doctor=(Button) view.findViewById(R.id.button_operation_doctor);
		bt_zhushou01=(Button) view.findViewById(R.id.button_opertion_zhushou01);
		bt_zhushou02=(Button) view.findViewById(R.id.button_opertion_zhushou02);
		bt_zhushou03=(Button) view.findViewById(R.id.button_opertion_zhushou03);
		bt_zhushou04=(Button) view.findViewById(R.id.button_opertion_zhushou04);
		tv_doctor=(TextView) view.findViewById(R.id.operation_textView_doctor);
		tv_zhushou01=(TextView) view.findViewById(R.id.operation_textView_zhushou01);
		tv_zhushou02=(TextView) view.findViewById(R.id.operation_textView_zhushou02);
		tv_zhushou03=(TextView) view.findViewById(R.id.operation_textView_zhushou03);
		tv_zhushou04=(TextView) view.findViewById(R.id.operation_textView_zhushou04);
		bt_sxyishi=(Button) view.findViewById(R.id.button_opertion_sxyishi);
		tv_sxyishi=(TextView) view.findViewById(R.id.operation_textView_sxyishi);
		smazui=(Spinner) view.findViewById(R.id.operation_spinner_mzfangfa);
		department=(Spinner) view.findViewById(R.id.add_operation_spinner_depart);
		mdiagnoise=(EditText) view.findViewById(R.id.operation_diagnoise);
		mnote=(EditText) view.findViewById(R.id.operation_beizhu);
		mdoctor=(EditText) view.findViewById(R.id.operation_sqyisheng);
		operationdate=(EditText) view.findViewById(R.id.operation_time);
		sqdate=(EditText) view.findViewById(R.id.operation_shenqingtime);
		mtaici=(EditText) view.findViewById(R.id.operation_taici);
		sskeshi=(EditText) view.findViewById(R.id.operation_department);
		mListView=(ListView) view.findViewById(R.id.operation_select_list);
		mpatient=(ListView) view.findViewById(R.id.operation_patient_list);
		
		mdoctor.setText(app.getDoctor());//申请医生
		sqdate.setText(Util.toSimpleDate());//申请日期（系统时间）
		operationdate.setText(operationTime());//手术日期
		sbingqing.setOnItemSelectedListener(this);
		ssshi.setOnItemSelectedListener(this);
		ssjian.setOnItemSelectedListener(this);
		sgeli.setOnItemSelectedListener(this);
		sdengji.setOnItemSelectedListener(this);		
		smazui.setOnItemSelectedListener(this);
		department.setOnItemSelectedListener(this);
		mpatient.setOnItemClickListener(this);
		
		bt_doctor.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				flag = 1;
				showSearchDialog();//弹出医生选择框
			}
		});
		bt_zhushou01.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				flag = 2;
				showSearchDialog();
			}
		});
		bt_zhushou02.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				flag = 3;
				showSearchDialog();
			}
		});
		bt_zhushou03.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				flag = 4;
				showSearchDialog();
			}
		});
		bt_zhushou04.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				flag = 5;
				showSearchDialog();
			}
		});
		bt_sxyishi.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				flag = 6;
				showSearchDialog();
			}
		});
	}

	/**
	 * 
	* @Title: showSearchDialog 
	* @Description: TODO(医生列表对话框) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private String nameString  =  "医生姓名";
	public void showSearchDialog(){
	       LayoutInflater factory = LayoutInflater.from(getActivity());
	       final View textEntryView = factory.inflate(R.layout.doctor_list, null);
	       final SearchView searchView=(SearchView) textEntryView.findViewById(R.id.searchView1);
	       final ListView listView=(ListView) textEntryView.findViewById(R.id.listView1);
	       DoctorAdapter adapter=new DoctorAdapter(getActivity(), R.layout.doctor_list_item, doctorList);
	       listView.setAdapter(adapter);	
	       listView.setTextFilterEnabled(true);
	       searchView.setIconifiedByDefault(false);
		   searchView.setSubmitButtonEnabled(false);
		   searchView.setQueryHint("请输入");
		   listView.setOnItemClickListener(this);
	       searchView.setOnQueryTextListener(new OnQueryTextListener() {
			public boolean onQueryTextSubmit(String arg0) {
				return false;
			}
			public boolean onQueryTextChange(String arg0) {
				if (TextUtils.isEmpty(arg0)) {
		        	listView.clearTextFilter();
		        } else {
		        	listView.setFilterText(arg0.toString());
		        }
				return true;
			  }
		    });
	       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		   //弹出框的点击事件，然后得到的值传给公共变量nameString
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				switch (parent.getId()) {
				case R.id.listView1:
					doctorEntity = (DoctorEntity) parent.getAdapter().getItem(position);
					nameString = doctorEntity.name;
					if (flag==1) {
						tv_doctor.setText(nameString);
					}
					if (flag==2) {
						tv_zhushou01.setText(nameString);
					}
					if (flag==3) {
						tv_zhushou02.setText(nameString);
					}
					if (flag==4) {
						tv_zhushou03.setText(nameString);
					}
					if (flag==5) {
						tv_zhushou04.setText(nameString);
					}
					if (flag==6) {
						tv_sxyishi.setText(nameString);
					}
				default:
					break;
				}
			}
		}) ;
	       
	       new AlertDialog.Builder(getActivity())
	           .setIconAttribute(android.R.attr.alertDialogIcon)
	           .setTitle("请编辑")
	           .setView(textEntryView)
	           .setPositiveButton("确认", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int whichButton) {
	                   /* User clicked OK so do some stuff */
	               }
	           })
	           .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int whichButton) {
	                   /* User clicked cancel so do some stuff */
	               }
	           })
	           .create().show();
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
		//科室列表spinner
		ArrayAdapter<String> deptadapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,mDeptList );
		deptadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		department.setAdapter(deptadapter);
		//麻醉列表spinner
		ArrayList<String> mazuiList=new ArrayList<String>();
		mazuiList.add("");//添加一个空白项
		for (int i = 0; i < mList.size(); i++) {
			String item="";
			item=mList.get(i);
			mazuiList.add(item);
		}
		ArrayAdapter<String>  mazuiAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,mazuiList);
		mazuiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		smazui.setAdapter(mazuiAdapter);	
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
		if (mAdapter.getCount()==0) {
			Toast.makeText(getActivity(), "请选择项目!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (ssshi.getSelectedItemPosition()==0) {
			Toast.makeText(getActivity(), "未选择手术室!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (entity==null) {
			Toast.makeText(getActivity(), "请选择病人!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	@Override
	public void onOtherFragmentClick() {
		// TODO Auto-generated method stub
	}
	
	//获取选中的手术项目，添加到adapter中，显示到选中项列表中
	public void setListView(OperationItemEntity entity){
		operationItemEntity=entity;
		mAdapter.addItem(operationItemEntity);	
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
		OperationInsertSql();	//手术插入方法	
	}
	
	
	
	/**
	 * 选择科室，获得病人列表
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		    switch (parent.getId()) {
			case R.id.add_operation_spinner_depart:
				if (arrayList!=null) {
					arrayList.clear();
				}
				int location = department.getSelectedItemPosition();
				String departname=app.getDepartnameList().get(location);
				sskeshi.setText(departname);//科室回显
				new OperationPatientTask().execute();//根据科室，重新获得病人列表
				break;
			default:
				break;
			}			
	}
	/**
	 * 对list的点击事件 《病人列表》
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.operation_patient_list:
			entity=(PatientEntity) parent.getAdapter().getItem(position);
			String  patInformation=patientInfo(entity);
			mdiagnoise.setText(entity.diagnosis);//临床诊断
			patient_info.setText(patInformation);//病人基本信息
			new maxNumberTask().execute(entity.patient_id,entity.visit_id);//获取最大序号任务
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	* @Title: patientInfo 
	* @Description: TODO(病人基本信息) 
	* @param     设定文件 
	* @return String    返回病人信息字符串
	* @throws
	 */
	public String patientInfo(PatientEntity entity){
		String patientInfoString="";
	    String age=String.valueOf(Util.userBirthdayGetAge(entity.date_of_birth));
		patientInfoString="床位："+entity.bed_no+"    病人ID："+entity.patient_id+"    住院号："+entity.inp_no
						+"    姓名："+ entity.name+"      性别："+entity.sex +"    年龄："+age;	
		return patientInfoString;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	/**
	 *   
	* @Title: operationTime 
	* @Description: TODO(手术日期时间) 
	* @param     设定文件 
	* @return String   返回类型 
	* @throws java.text.ParseException
	 */
	public String operationTime(){
		//得到申请时间-->手术日期及时间 不是星期五和星期六，
		//则天数+1 时间点为8:00 ，是星期五，天数+3 时间点为8:00 ，
		//是星期六，天数+2，时间点为8:00
		Calendar c = Calendar.getInstance();
		Date date = new Date();
		c.setTime(date);
		String timeString = getWeekDay(c);
		return timeString;
	}

	private static String getWeekDay(Calendar c){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		String returnDate = "无日期显示";
		   if(c == null){
		    return returnDate;
		   }
		   //如果是周一，则加一天
		   if(Calendar.MONDAY == c.get(Calendar.DAY_OF_WEEK)){
		   cal.add(Calendar.DAY_OF_WEEK, 1);
		   returnDate =  format.format(cal.getTime());
		   }
		   //如果是周二，则加一天
		   if(Calendar.TUESDAY == c.get(Calendar.DAY_OF_WEEK)){
		   cal.add(Calendar.DAY_OF_WEEK, 1);
		   returnDate = format.format(cal.getTime());
		   }
		   //如果是周三，则加一天
		   if(Calendar.WEDNESDAY == c.get(Calendar.DAY_OF_WEEK)){
		   cal.add(Calendar.DAY_OF_WEEK, 1);
		   returnDate = format.format(cal.getTime());
		   }
		   //如果是周四，则加一天
		   if(Calendar.THURSDAY == c.get(Calendar.DAY_OF_WEEK)){
		   cal.add(Calendar.DAY_OF_WEEK, 1);
		   returnDate = format.format(cal.getTime());
		   }
		   //如果是周五，则加三天
		   if(Calendar.FRIDAY == c.get(Calendar.DAY_OF_WEEK)){
		   cal.add(Calendar.DAY_OF_WEEK, 3);
		   returnDate = format.format(cal.getTime());
		   }
		   //如果是周六，则加两天
		   if(Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK)){
		   cal.add(Calendar.DAY_OF_WEEK, 2);
		   returnDate = format.format(cal.getTime());
		   }
		   //如果是周日，则加一天
		   if(Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK)){
		   cal.add(Calendar.DAY_OF_WEEK, 1);
		   returnDate = format.format(cal.getTime());
		   }
		   DebugUtil.debug("日期的返回值", returnDate);
		   return returnDate;
		}

	/**
	 * 
	* @Title: getpatientSql 
	* @Description: TODO(得到获取手术病人列表语句) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public  String getpatientSql(){
		int location = department.getSelectedItemPosition();
		String departcode=app.getDepartcodeList().get(location);
		String tableName="pats_in_hospital,pat_master_index";
		String[] paramArray1=new String[]{"pats_in_hospital.dept_code","pats_in_hospital.bed_no","pats_in_hospital.diagnosis",
				                          "pats_in_hospital.doctor_in_charge","pats_in_hospital.patient_id","pats_in_hospital.prepayments",
				                          "pat_master_index.name","pat_master_index.sex","pat_master_index.date_of_birth",
				                          "pats_in_hospital.visit_id","pat_master_index.inp_no"};
		String customWhere="where  pats_in_hospital.patient_id = pat_master_index.patient_id "
				+ " and pats_in_hospital.dept_code = '"+departcode+"' "
                + " order by pats_in_hospital.bed_no";
		String sql=ServerDao.getQueryCustom(tableName, paramArray1, customWhere);
		return sql;
	}
	/**
	 * 
	* @ClassName: OperationPatientTask 
	* @Description: TODO(手术病人列表任务) 
	* @author lll 
	* @date 2013-02-26 
	*
	 */
	private class OperationPatientTask extends AsyncTask<Void, Void, Object>{

		@Override
		protected Object doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(getpatientSql());
			arrayList=PatientEntity.init(dataList);
			return arrayList;
		}
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (arrayList.size()!=0) {
				OperationPatientAdapter adapter=new OperationPatientAdapter(getActivity(), arrayList);
				mpatient.setAdapter(adapter);
			}else{
				Toast.makeText(getActivity(), "没有数据!", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	/**
	 * 
	* @ClassName: maxNumberTask 
	* @Description: TODO(最大序号) 同时获得schedule_id的值
	* @author lll 
	* @date 2013-03-07 
	*
	 */
	private class  maxNumberTask extends AsyncTask<String, Void, Object>{

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			String sql= "select max (order_no) from orders where patient_id ='"
					+ params[0] + "' and visit_Id ='"+ params[1] + "'";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
			String max="0";
			for (int i = 0; i < dataList.size(); i++) {
				String result=dataList.get(i).get("max(order_no)").trim();
				if (!result.equals("")) {
					max=result;
				}
			}
			Log.e("获取的最大值----->", max);
			scheduleId(params[0], params[1]);
			Log.e("schedule_id-------->", schedule_id);
			return max;
		}
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				String temp=(String) result;
				int order_no=Integer.parseInt(temp);
				int max_no=order_no+1;
				String maxNumber=String.valueOf(max_no);
				app.setMaxNumber(maxNumber);
			} catch (Exception e) {
				Toast.makeText(getActivity(), "获取最大值失败!", Toast.LENGTH_SHORT).show();
			}
		}
		
		public void scheduleId(String patientID,String visitID){
			String  sqlString="select max(schedule_id) as sid,count(*) as num  from operation_schedule where patient_id='"+patientID+"'"
					       + "  and visit_id='"+visitID+"'";
			Log.e("scheduleID---->", sqlString);
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sqlString);
			String num=dataList.get(0).get("num");
			if ("0".equals(num)) {
				schedule_id="1";
			}else {
				String sid=dataList.get(0).get("sid");
				if (sid!=null) {
					int intnum=Integer.parseInt(sid);
					int temp=intnum+1;
					schedule_id=String.valueOf(temp);
				}				
			}		
		}
	}
	
	/**
	 * 
	* @Title: OperationInsertSql 
	* @Description: TODO(手术插入方法) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private void OperationInsertSql(){
		StringBuffer operationBuffer=new StringBuffer();
		String req_date_time=Util.toSimpleDate();
		String date_of_birth=Util.toDateOfBirth(app.getPatientEntity().date_of_birth);
		String operationTime=operationdate.getText().toString();
		Log.e("手术时间---->", operationTime);
		operationBuffer.append("insert into operation_schedule ("
				+ "patient_id,visit_id,schedule_id,dept_stayed,bed_no,scheduled_date_time," 
				+ "operating_room,diag_before_operation,patient_condition,operation_scale,"
				+ "isolation_indicator,operating_dept,surgeon,first_assistant,second_assistant,"
				+ "third_assistant,fourth_assistant,blood_tran_doctor,notes_on_operation,"
				+ "req_date_time,sequence,anesthesia_method,entered_by,operating_room_no,"
				+ "ack_indicator,doctor_user) "
				+ "values (");
		operationBuffer.append("'"+entity.patient_id).append("',");
		operationBuffer.append("'"+entity.visit_id).append("',");
		operationBuffer.append("'"+schedule_id).append("',");
		operationBuffer.append("'"+entity.dept_code).append("',");
		operationBuffer.append("'"+entity.bed_no).append("',");
		operationBuffer.append("TO_DATE('"+operationTime).append("','yyyy-MM-dd hh24:mi:ss'),");//手术时间
		operationBuffer.append("'"+"52201").append("',");//手术室代码
		operationBuffer.append("'"+entity.diagnosis).append("',");
		operationBuffer.append("'"+sbingqing.getSelectedItem().toString()).append("',");
		operationBuffer.append("'"+sdengji.getSelectedItem().toString()).append("',");
		operationBuffer.append("'"+sgeli.getSelectedItemPosition()).append("',");
		operationBuffer.append("'"+entity.dept_code).append("',");
		operationBuffer.append("'"+tv_doctor.getText().toString()).append("',");//手术医生
		operationBuffer.append("'"+tv_zhushou01.getText().toString()).append("',");//助手
		operationBuffer.append("'"+tv_zhushou02.getText().toString()).append("',");//助手
		operationBuffer.append("'"+tv_zhushou03.getText().toString()).append("',");//助手
		operationBuffer.append("'"+tv_zhushou04.getText().toString()).append("',");//助手
		operationBuffer.append("'"+tv_sxyishi.getText().toString()).append("',");//输血者
		operationBuffer.append("'"+mnote.getText().toString()).append("',");
		operationBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
		operationBuffer.append("'"+mtaici.getText().toString()).append("',");
		operationBuffer.append("'"+smazui.getSelectedItem().toString()).append("',");
		operationBuffer.append("'"+app.getDoctor()).append("',");
		operationBuffer.append("'"+ssjian.getSelectedItem().toString()).append("',");
		operationBuffer.append("'"+"0").append("',");
		operationBuffer.append("'"+app.getLoginName()).append("')");
		WebServiceHelper.insertWebServiceData(operationBuffer.toString());//手术插入数据1
		int max_no=Integer.parseInt(app.getMaxNumber())-1;//最大序号
		int count=mAdapter.getCount();
		for (int i = 0; i < count; i++) {
			int operation_item_no=i+1;
			//Map<String, String> map=(Map<String, String>) mAdapter.getItem(i);
			OperationItemEntity entityitem=(OperationItemEntity)mAdapter.getItem(i);
			String operation_itemsSQL = "insert into scheduled_operation_name "
					+ "(patient_id,visit_id,schedule_id,operation_no,"
					+ "operation,operation_scale)  values ('" + entity.patient_id+ "','"
					+ entity.visit_id+ "','" +schedule_id+"','"+operation_item_no+"','"
					+ entityitem.operation_name + "','"+entityitem.operation_scale+"')";
			WebServiceHelper.insertWebServiceData(operation_itemsSQL);//手术插入数据2
			String text="";
			if (smazui.getSelectedItemPosition()==0) {
				text="通知："+operationTime+"在无麻醉下进行“"+entityitem.operation_name+"”手术";
				
			}else{
				text="通知："+operationTime+"在"+smazui.getSelectedItem().toString()+"下进行“"+entityitem.operation_name+"”手术";
			}
			max_no++;
			StringBuffer orderBuffer=new StringBuffer();
			orderBuffer.append("insert into orders" + " (patient_id,"
					        + "  visit_id," + "  order_no," + "  order_sub_no,"
					        + "  start_date_time," + "  repeat_indicator,"
					        + "  order_class," + "  order_text," + "  order_code,"
					        + "  order_status," + "  ordering_dept," + "  doctor,"
					        + "  doctor_user," + "  enter_date_time," + "  billing_attr,"
					        + "  drug_billing_attr,"+"stop_date_time)" + "values (");
			orderBuffer.append("'"+entity.patient_id).append("',");
			orderBuffer.append("'"+entity.visit_id).append("',");
			orderBuffer.append("'"+max_no).append("',");
			orderBuffer.append("'"+"1").append("',");
			orderBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
			orderBuffer.append("'"+"0").append("',");
			orderBuffer.append("'"+"F").append("',");
			orderBuffer.append("'"+text).append("',");//医嘱内容
			orderBuffer.append("'"+entityitem.operation_code).append("',");
			orderBuffer.append("'"+"6").append("',");
			orderBuffer.append("'"+entity.dept_code).append("',");
			orderBuffer.append("'"+app.getDoctor()).append("',");
			orderBuffer.append("'"+app.getLoginName()).append("',");
			orderBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
			orderBuffer.append("'"+"3").append("',");
			orderBuffer.append("'"+"3").append("',");
			orderBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'))");
			WebServiceHelper.insertWebServiceData(orderBuffer.toString());
		}
	}
	
}
