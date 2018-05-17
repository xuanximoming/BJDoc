package com.android.hospital.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.BloodItemAdapter;
import com.android.hospital.adapter.DoctorAdapter;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.BloodItemEntity;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.DoctorEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.ui.activity.AddBloodActivity;
import com.android.hospital.ui.activity.BloodActivity;
import com.android.hospital.ui.activity.MainActivity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.ui.activity.R.id;
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
import android.widget.CheckBox;
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
* @ClassName: AddBloodFragment 
* @Description: TODO(用血申请界面布局) 
* @author lll 
* @date 2013-03-27 
*
 */
public class AddBloodFragment  extends BaseFragment implements OnItemSelectedListener{

    private View view; 
    private EditText applynumber;//申请单号
    private CheckBox cb_abolish;//作废
    private TextView patient_info; //病人基本信息显示
    private Spinner sp_xuexing,sp_Rh;//血型、RH
    private RadioButton bt_xueyuan1,bt_xueyuan2,bt_xueyuan3;//血源
    private RadioButton bt_shudi1,bt_shudi2,bt_shudi3,bt_shudi4,bt_shudi5;//属地
    private EditText mdiagnoise,mfangying,applytime;//诊断、输血反应和禁忌症、申请时间
    private EditText et_xhdb,et_xxb,et_bxb,et_hct,et_alt,et_fzx;
    private Spinner sp_hbsag,sp_antihcv,sp_antihiv,sp_meidu,sp_ysxx;
    private EditText doctorEditText;//医生
    private Button bt_zhuren,bt_zhuzhi;//主任、主治
	private EditText et_zhuren,et_zhuzhi;//主任、主治
	private ListView mListView;//存放新增的用血信息
	private PatientEntity entity;//病人实体
	private ArrayList<String> deptcodeList;//科室代码列表
	private BloodItemEntity bloodItemEntity;//新增项目实体
	private BloodItemAdapter mAdapter; //用血项目
    private HospitalApp app; 
	private ArrayList<DoctorEntity>  doctorList ;//医生列表
	private DoctorEntity doctorEntity;//医生实体
	private int flag=0;
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.app=(HospitalApp) getActivity().getApplication();
		this.doctorList = app.getStaff_doctorList();
		this.deptcodeList=app.getDepartcodeList();
		entity=app.getPatientEntity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_add_blood_main, null); 
		init();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mAdapter=new BloodItemAdapter(getActivity(), null);
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		applynumber=(EditText) view.findViewById(R.id.add_blood_main_applynumber);
		cb_abolish=(CheckBox) view.findViewById(R.id.add_blood_main_abolish);
		patient_info=(TextView) view.findViewById(R.id.add_blood_main_patientinformation);
		sp_xuexing=(Spinner) view.findViewById(R.id.add_blood_main_spinner_xx);
		sp_Rh=(Spinner) view.findViewById(R.id.add_blood_main_spinner_rh);
		bt_xueyuan1=(RadioButton) view.findViewById(R.id.add_blood_main_xueyuan_1);
		bt_xueyuan2=(RadioButton) view.findViewById(R.id.add_blood_main_xueyuan_2);
		bt_xueyuan3=(RadioButton) view.findViewById(R.id.add_blood_main_xueyuan_3);
		bt_shudi1=(RadioButton) view.findViewById(R.id.add_blood_main_shudi_1);
		bt_shudi2=(RadioButton) view.findViewById(R.id.add_blood_main_shudi_2);
		bt_shudi3=(RadioButton) view.findViewById(R.id.add_blood_main_shudi_3);
		bt_shudi4=(RadioButton) view.findViewById(R.id.add_blood_main_shudi_4);
		bt_shudi5=(RadioButton) view.findViewById(R.id.add_blood_main_shudi_5);
		mdiagnoise=(EditText) view.findViewById(R.id.add_blood_main_diagnoise);
		mfangying=(EditText) view.findViewById(R.id.add_blood_main_fanying);
		applytime=(EditText) view.findViewById(R.id.add_blood_main_applytime);
		doctorEditText=(EditText) view.findViewById(R.id.add_blood_main_doctor);
		et_zhuren=(EditText) view.findViewById(R.id.add_blood_main_zhuren);
		et_zhuzhi=(EditText) view.findViewById(R.id.add_blood_main_zhuzhi);
		bt_zhuren=(Button) view.findViewById(R.id.add_blood_main_btzhuren);
		bt_zhuzhi=(Button) view.findViewById(R.id.add_blood_main_btzhuzhi);
		et_xhdb=(EditText) view.findViewById(R.id.add_blood_main_xhdb);
		et_xxb=(EditText) view.findViewById(R.id.add_blood_main_xxb);
		et_bxb=(EditText) view.findViewById(R.id.add_blood_main_bxb);
        et_hct=(EditText) view.findViewById(R.id.add_blood_main_hct);
        et_alt=(EditText) view.findViewById(R.id.add_blood_main_alt);
        sp_hbsag=(Spinner) view.findViewById(R.id.add_blood_main_spinner_hbsag);
        sp_antihcv=(Spinner) view.findViewById(R.id.add_blood_main_spinner_antihcv);
        sp_antihiv=(Spinner) view.findViewById(R.id.add_blood_main_spinner_antihiv);
        sp_meidu=(Spinner) view.findViewById(R.id.add_blood_main_spinner_meidu);
        sp_ysxx=(Spinner) view.findViewById(R.id.add_blood_main_spinner_yushuxuexing);
        et_fzx=(EditText) view.findViewById(R.id.add_blood_main_fuzhaoxue);
		mListView=(ListView) view.findViewById(R.id.add_blood_main_list);		
		doctorEditText.setText(entity.doctor_in_charge);//医生
		applytime.setText(Util.toSimpleDate());//申请日期
		patient_info.setText(patientInfo());//病人基本信息
		mdiagnoise.setText(entity.diagnosis);//诊断
		sp_xuexing.setOnItemSelectedListener(this);
		sp_Rh.setOnItemSelectedListener(this);	
		//主任和主治医生，弹出对话框，选择医生
		bt_zhuren.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				flag = 1;
				showSearchDialog();
			}
		});
		bt_zhuzhi.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				flag = 2;
				showSearchDialog();
			}
		});	
	}

	/**
	 * 
	* @Title: showSearchDialog 
	* @Description: TODO(医生选择对话框) 
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	private String nameString  =  "11";
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
						et_zhuren.setText(nameString);
					}else if (flag==2) {
						et_zhuzhi.setText(nameString);
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
	* @Title: clear 
	* @Description: TODO(清空adapter) 
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public void clear(){
		mAdapter.clear();
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
		return true;
	}
	
	/**
	 * 
	* @Title: setListView 
	* @Description: TODO(设置选中的项目) 
	* @param     
	* @return void    返回类型 
	* @throws
	 */
	public void setListView(BloodItemEntity entity){
		//需要填写啊。获得的是新增的用血项目，在activity中调用该函数，可以将信息显示到listview中
		bloodItemEntity=entity;
		mAdapter.addItem(bloodItemEntity);	
	}

	/**
	 * 
	* @Title: String patientInfo 
	* @Description: TODO(获得病人的基本信息) 
	* @param     
	* @return String    返回类型 
	* @throws
	 */
	public String patientInfo(){	
		String patientInfoString="";
		String departname="";
		String dcString ="";
		String date_of_birth=Util.toDateOfBirth(entity.date_of_birth);
		int location=-1;
		for (int i = 0;i < deptcodeList.size() ; i++) {
		    dcString = deptcodeList.get(i);
			if (dcString.equals(entity.dept_code)) {
				location=i;
			}
		}
		departname=app.getDepartnameList().get(location);//获得科室名称
		patientInfoString="姓名："+entity.name+"   性别："+entity.sex+"   出生："+date_of_birth+"    id号："+
	           entity.patient_id+"  住院号："+entity.inp_no+"  费别："+entity.charge_type+"   申请科室:"+departname;	
		return patientInfoString;  
	} 

	/**
	 * 
	* @Title: setDate 
	* @Description: TODO(获得需要判断和编辑的项目信息) 
	* @param     
	* @return void    返回类型 
	* @throws
	 */
	String blood_inuse,pat_source;//用血方式，属地,
	int bloodsum=0;//用血总量
	public void setDate(){
		if (bt_xueyuan1.isChecked()) {
			blood_inuse="血库";
		}
		if (bt_xueyuan2.isChecked()) {
			blood_inuse="自体";
		}
		if (bt_xueyuan3.isChecked()) {
			blood_inuse="互助";
		}
		if (bt_shudi1.isChecked()) {
			pat_source="1";
		}
		if (bt_shudi2.isChecked()||bt_shudi3.isChecked()||bt_shudi4.isChecked()||bt_shudi5.isChecked()) {
			pat_source="2";
		}
		if (null==pat_source) {
			pat_source="";
		}
		int count=mAdapter.getCount();	
		for (int j = 0; j < count; j++) {
			BloodItemEntity blooditem=(BloodItemEntity) mAdapter.getItem(j);
			bloodsum+=Integer.parseInt(blooditem.trans_capacity);
		}	
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
		BloodInsertSql();		
	}
	/**
	 * 
	* @Title: BloodInsertSql 
	* @Description: TODO(用血插入方法) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private void BloodInsertSql(){
		setDate();
		StringBuffer bloodBuffer=new StringBuffer();
		String req_date_time=Util.toSimpleDate();
		String date_of_birth=Util.toDateOfBirth(entity.date_of_birth);
		bloodBuffer.append("insert into blood_apply ("
				+ "apply_num,inp_no,patient_id,dept_code, pat_name, pat_sex, birthday,fee_type," 
				+ "pat_source, blood_paper,blood_inuse,blood_taboo,hematin, platelet, leucocyte," 
				+ " pat_blood_group, rh,  blood_sum, apply_date, gather_date,director,physician," 
				+ "doctor, blood_diagnose, price, hct, alt, hbsag, hcv, hiv, anti_md, shine_blood," 
				+ "pre_blood_type) "
				+ "values (");
		bloodBuffer.append("'"+app.getNextval()).append("',");//申请单号
		bloodBuffer.append("'"+entity.inp_no).append("',");
		bloodBuffer.append("'"+entity.patient_id).append("',");//手术室代码
		bloodBuffer.append("'"+entity.dept_code).append("',");
		bloodBuffer.append("'"+entity.name).append("',");
		bloodBuffer.append("'"+entity.sex).append("',");
		bloodBuffer.append("TO_DATE('"+date_of_birth).append("','yyyy-MM-dd'),");
		bloodBuffer.append("'"+entity.charge_type).append("',");
		bloodBuffer.append("'"+pat_source).append("',");//属地
		bloodBuffer.append("'"+"").append("',");//献血证类别 （好像没用）
		bloodBuffer.append("'"+blood_inuse).append("',");//用血方式，血源（血库、自体、互助）
		bloodBuffer.append("'"+mfangying.getText().toString()).append("',");//输血反应及输血禁忌症 （手动填写）
		bloodBuffer.append("'"+et_xhdb.getText().toString()).append("',");//血色素（血红蛋白）
		bloodBuffer.append("'"+et_xxb.getText().toString()).append("',");//血小板
		bloodBuffer.append("'"+et_bxb.getText().toString()).append("',");//白血球
		bloodBuffer.append("'"+sp_xuexing.getSelectedItem().toString()).append("',");////受血者血型
		bloodBuffer.append("'"+sp_Rh.getSelectedItem().toString()).append("',");//// RH血型
		bloodBuffer.append("'"+bloodsum).append("',");//输血总量
		bloodBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");//申请时间
		bloodBuffer.append("'"+"").append("',");//血库收到时间 ，空
		bloodBuffer.append("'"+et_zhuren.getText().toString()).append("',");// 科主任
		bloodBuffer.append("'"+et_zhuzhi.getText().toString()).append("',");// 主治医生
		bloodBuffer.append("'"+entity.doctor_in_charge).append("',");// 病人主管医生 
		bloodBuffer.append("'"+entity.diagnosis).append("',");//诊断及输血适应症
		bloodBuffer.append("'"+"").append("',");// 划价标志 1划价 ，填空
		bloodBuffer.append("'"+et_hct.getText().toString()).append("',");
		bloodBuffer.append("'"+et_alt.getText().toString()).append("',");
		bloodBuffer.append("'"+sp_hbsag.getSelectedItem().toString()).append("',");
		bloodBuffer.append("'"+sp_antihcv.getSelectedItem().toString()).append("',");
		bloodBuffer.append("'"+sp_antihiv.getSelectedItem().toString()).append("',");
		bloodBuffer.append("'"+sp_meidu.getSelectedItem().toString()).append("',");//梅毒
		bloodBuffer.append("'"+et_fzx.getText().toString()).append("',");// 辐照血
		bloodBuffer.append("'"+sp_ysxx.getSelectedItem().toString()).append("')"); // 预输血型
		WebServiceHelper.insertWebServiceData(bloodBuffer.toString());//用血插入数据1
		int max_no=Integer.parseInt(app.getMaxNumber())-1;//最大序号
		int count=mAdapter.getCount();
		for (int i = 0; i < count; i++) {
			int blood_item_no=i+1;
			BloodItemEntity bentity=(BloodItemEntity)mAdapter.getItem(i);
			String order_text="";
			if ("急诊".equals(bentity.fast_slow)) {
				bentity.fast_slow="1";
				order_text="输"+bentity.blood_type_name+bentity.trans_capacity+bentity.unit;
			}
			if ("计划".equals(bentity.fast_slow)) {
				bentity.fast_slow="2";
				order_text="输"+bentity.blood_type_name+bentity.trans_capacity+bentity.unit;
			}
			if ("备血".equals(bentity.fast_slow)) {
				bentity.fast_slow="3";
				order_text="备"+bentity.blood_type_name+bentity.trans_capacity+bentity.unit;
			}
			String blood_itemsSQL = "insert into blood_capacity "
					+ "(fast_slow,match_sub_num,trans_date,trans_capacity,"
					+ "operator,apply_num,blood_type,unit)  values ('"+bentity.fast_slow+"','"
					+ blood_item_no+ "',TO_DATE('"+bentity.trans_date+"','yyyy-MM-dd hh24:mi:ss'),'"+bentity.trans_capacity+"','"
					+ app.getDoctor() + "','"+app.getNextval()+"','"+bentity.blood_type+"','"+bentity.unit+"')";
			WebServiceHelper.insertWebServiceData(blood_itemsSQL);//用血插入数据2	
			max_no++;
			StringBuffer orderBuffer=new StringBuffer();
			orderBuffer.append("insert into orders ("
					        + "  patient_id,visit_id,order_no, order_sub_no, start_date_time, repeat_indicator,"
					        + "  order_class,order_text, order_code,order_status, ordering_dept,doctor,"
					        + "  doctor_user, enter_date_time, billing_attr,"
					        + "  drug_billing_attr,"+"stop_date_time,app_no)" + "values (");
			orderBuffer.append("'"+entity.patient_id).append("',");
			orderBuffer.append("'"+entity.visit_id).append("',");
			orderBuffer.append("'"+max_no).append("',");
			orderBuffer.append("'"+"1").append("',");
			orderBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
			orderBuffer.append("'"+"0").append("',");
			orderBuffer.append("'"+"Z").append("',");
			orderBuffer.append("'"+order_text).append("',");//医嘱，分为 备。。。 输。。。
			orderBuffer.append("'").append("',");//代码  填‘’
			orderBuffer.append("'"+"6").append("',");
			orderBuffer.append("'"+entity.dept_code).append("',");
			orderBuffer.append("'"+app.getDoctor()).append("',");
			orderBuffer.append("'"+app.getLoginName()).append("',");
			orderBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
			orderBuffer.append("'"+"3").append("',");
			orderBuffer.append("'"+"3").append("',");
			orderBuffer.append("TO_DATE('"+req_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
			orderBuffer.append("'"+app.getNextval()).append("')");//申请单号
			WebServiceHelper.insertWebServiceData(orderBuffer.toString());
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
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
