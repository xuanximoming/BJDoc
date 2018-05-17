package com.android.hospital.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.DrugAdapter;
import com.android.hospital.adapter.FreqSpinnerAdapter;
import com.android.hospital.asyntask.add.InsertDcAdviceTask;
import com.android.hospital.asyntask.add.PriceTask;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.entity.DrugEntity;
import com.android.hospital.entity.NonDrugEntity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
* @ClassName: AddDcAdviceFragment 
* @Description: TODO(新增医嘱) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:34:25 
*
 */
public class AddDcAdviceFragment extends Fragment implements OnClickListener,OnItemSelectedListener{

	private View view;	
	private RadioButton mDrugBut,mNonDrugBut,mLongTimeBut,mShortTimeBut;//药品非药品、长期临时 
	private TextView mDcAdViceTev,mDosageunitsTev;	//医嘱名称、剂量单位、
	private EditText mDosageEdit,mInstrutionEdit,mTimeTev;	//剂量、医生说明、执行时间
	private Spinner mWaySpinner,mFreqSpinner,mZiSpinner; //途径、频次、自	
	private LinearLayout mHideLayout1,mHideLayout2;//需隐藏的布局	
	private CheckBox mSubCheckBox;	//是否为子医嘱
	private HospitalApp app;	
	private List<Map<String, String>> mFreqList;//频次列表	
	private List<Map<String, String>> mWayList;	//途径列表
	private String order_class="";	//药品类别
	private String order_code="";	//药品代码
	private String drug_spec="";//药品规格	
	private DcAdviceEntity subEntity;//子医嘱	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app=(HospitalApp) getActivity().getApplication();
		this.mFreqList=app.getFreqList();//获取频次列表
		this.mWayList=app.getWayList();//获取途径列表
		this.subEntity=(DcAdviceEntity) getActivity().getIntent().getSerializableExtra("subentity");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_add_dcadvice_left, null);
		initView();
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mNonDrugBut.setOnClickListener(this);
		mDrugBut.setOnClickListener(this);
		mLongTimeBut.setOnClickListener(this);
		mShortTimeBut.setOnClickListener(this);
		mSubCheckBox.setOnClickListener(this);
		mWaySpinner.setOnItemSelectedListener(this);
		mFreqSpinner.setOnItemSelectedListener(this);
		setSpinner();
		if (subEntity.order_class.equals("A")) {
			mSubCheckBox.setVisibility(View.VISIBLE);
		}else {
			mSubCheckBox.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	* @Title: initView 
	* @Description: TODO(初始化控件) 
	* @param
	* @return void    返回类型 
	* @throws
	 */
	public void initView(){
		mDrugBut=(RadioButton) view.findViewById(R.id.add_dcadvice_but_drug);
		mNonDrugBut=(RadioButton) view.findViewById(R.id.add_dcadvice_but_nondrug);
		mLongTimeBut=(RadioButton) view.findViewById(R.id.add_dcadvice_but_longtime);
		mShortTimeBut=(RadioButton) view.findViewById(R.id.add_dcadvice_but_shorttime);
		mHideLayout1=(LinearLayout) view.findViewById(R.id.hidelayout1);
		mHideLayout2=(LinearLayout) view.findViewById(R.id.hidelayout2);
		mDcAdViceTev=(TextView) view.findViewById(R.id.add_dcadvice_tev_info);
		mDosageunitsTev=(TextView) view.findViewById(R.id.add_dcadvice_tev_dosage_units);
		mTimeTev=(EditText) view.findViewById(R.id.add_dcadvice_tev_time); 
		mDosageEdit=(EditText) view.findViewById(R.id.add_dcadvice_edit_dosage);
		mWaySpinner=(Spinner) view.findViewById(R.id.add_dcadvice_spinner_administration);
		mZiSpinner=(Spinner) view.findViewById(R.id.add_dcadvice_spinner_zi);
		mFreqSpinner=(Spinner) view.findViewById(R.id.add_dcadvice_spinner_frequency);
		mInstrutionEdit=(EditText) view.findViewById(R.id.add_dcadvice_edit_instrution);
		mSubCheckBox=(CheckBox) view.findViewById(R.id.add_dcadvice_checkbox_sub);
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
		//设置频次
		List<String> freqSpList=new ArrayList<String>();
		for (int i = 0; i < mFreqList.size(); i++) {
			String item=mFreqList.get(i).get("freq_desc");
			freqSpList.add(item);
		}
		ArrayAdapter<String> freqAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, freqSpList);
		freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mFreqSpinner.setAdapter(freqAdapter);
		//设置途径
		List<String> waySpList=new ArrayList<String>();
		for (int i = 0; i < mWayList.size(); i++) {
			String item=mWayList.get(i).get("administration_name");
			waySpList.add(item);
		}
		ArrayAdapter<String> wayAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, waySpList);
		wayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mWaySpinner.setAdapter(wayAdapter);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SearchAddDcAdviceFragment searchFm=(SearchAddDcAdviceFragment) getActivity().getFragmentManager().findFragmentByTag("searchfm");
		switch (v.getId()) {
		case R.id.add_dcadvice_but_drug:
			if (mDrugBut.isChecked()) {
				searchFm.showDrug();
			}
			if (mLongTimeBut.isChecked()) {
				mHideLayout1.setVisibility(View.VISIBLE);
				mHideLayout2.setVisibility(View.VISIBLE);
			}else {
				mHideLayout1.setVisibility(View.VISIBLE);
				mHideLayout2.setVisibility(View.GONE);
			}
			break;
		case R.id.add_dcadvice_but_nondrug:
			if (mNonDrugBut.isChecked()) {
				searchFm.showNonDrug();
			}
			if (mLongTimeBut.isChecked()) {
				mHideLayout1.setVisibility(View.GONE);
				mHideLayout2.setVisibility(View.VISIBLE);
			}else {
				mHideLayout1.setVisibility(View.GONE);
				mHideLayout2.setVisibility(View.GONE);
			}
			break;
		case R.id.add_dcadvice_but_longtime:
			if (mNonDrugBut.isChecked()) {
				searchFm.showNonDrug();
			}
			if (mDrugBut.isChecked()) {
				mHideLayout1.setVisibility(View.VISIBLE);
				mHideLayout2.setVisibility(View.VISIBLE);
			}else {
				mHideLayout1.setVisibility(View.GONE);
				mHideLayout2.setVisibility(View.VISIBLE);		
			}
			break;
		case R.id.add_dcadvice_but_shorttime:
			if (mNonDrugBut.isChecked()) {
				searchFm.showNonDrug();
			}
			if (mDrugBut.isChecked()) {
				mHideLayout1.setVisibility(View.VISIBLE);
				mHideLayout2.setVisibility(View.GONE);
			}else {
				mHideLayout1.setVisibility(View.GONE);
				mHideLayout2.setVisibility(View.GONE);
			}
			break;
		case R.id.add_dcadvice_checkbox_sub:
			if (mSubCheckBox.isChecked()) {
				view.findViewById(R.id.radiolayout).setVisibility(View.GONE);
				view.findViewById(R.id.dosage_units_layout).setVisibility(View.GONE);
				view.findViewById(R.id.administration_layout).setVisibility(View.GONE);
				mHideLayout2.setVisibility(View.GONE);
			}else {
				view.findViewById(R.id.radiolayout).setVisibility(View.VISIBLE);
				view.findViewById(R.id.dosage_units_layout).setVisibility(View.VISIBLE);
				view.findViewById(R.id.administration_layout).setVisibility(View.VISIBLE);
				mHideLayout2.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 
	* @Title: initDrug 
	* @Description: TODO(显示药品内容) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void initDrug(DrugEntity drugEntity){
		mDcAdViceTev.setText(drugEntity.drug_name);
		mDosageunitsTev.setText(drugEntity.dose_units);
		mDosageEdit.setText(drugEntity.dose_per_unit);
		order_class=drugEntity.drug_indicator;
		order_code=drugEntity.drug_code;
		drug_spec=drugEntity.drug_spec;
	}
	/**
	 * 
	* @Title: initNonDrug 
	* @Description: TODO(显示非药品内容) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void initNonDrug(NonDrugEntity nonDrugEntity){
		mDcAdViceTev.setText(nonDrugEntity.item_name);
		order_class=nonDrugEntity.item_class;
		order_code=nonDrugEntity.item_code;
	}
	
	/**
	 * 
	* @Title: getAddData 
	* @Description: TODO(得到基本数据) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void getAddData(){
		DcAdviceEntity entity=new DcAdviceEntity();
		entity.start_date_time=Util.toSimpleDate();// 获取当前时间
		entity.order_text=mDcAdViceTev.getText().toString();// 医嘱内容
		entity.dosage=mDosageEdit.getText().toString();// 剂量
		entity.dosage_units=mDosageunitsTev.getText().toString();// 剂量单位
		entity.freq_detail=mInstrutionEdit.getText().toString();// 医生说明
		entity.administration=mWaySpinner.getItemAtPosition(
				mWaySpinner.getSelectedItemPosition()).toString();// 途径
		Map<String, String> freqMap = mFreqList.get(mFreqSpinner
				.getSelectedItemPosition());
		entity.frequency=freqMap.get("freq_desc");// 频次
		entity.freq_counter=freqMap.get("freq_counter");// 频率次数
		entity.freq_interval=freqMap.get("freq_interval");/// 频率间隔
		entity.freq_interval_unit=freqMap.get("freq_interval_unit");// 频率间隔单位
		if (null!=entity.freq_interval_unit&&entity.freq_interval_unit.equals("")) {
			entity.freq_interval_unit="日";
		}
		entity.doctor=app.getDoctor();// 医生
		entity.order_class=this.order_class;// 医嘱类别 药品：
		                                    // input_drug_lists.drug_indicator
		                                    // 非药品:v_clinic_name_dict.item_class,通过drug或nondrug获得
		entity.order_code=this.order_code;//医嘱代码,医嘱代码  非药品:item_code  药品:drug_code
		entity.repeat_indicator="1";// 长期临时，0为 期，1为临时
		entity.stop_date_time="";// 停止时间,如果为长期，则为空，否则等于当前时间
		if (mShortTimeBut.isChecked()) {
			entity.repeat_indicator = "0";
			entity.stop_date_time = entity.start_date_time;
		}
        entity.order_status="6";// 在ORDER_STATUS_DICT中查看
		entity.enter_date_time=entity.start_date_time;// 开医嘱录入日期及时间，（同START_DATE_TI*ME时间，即保存时系统时间)
        entity.patient_id=app.getPatientEntity().patient_id;//病人ID
        entity.visit_id=app.getPatientEntity().visit_id;//住院次数
        entity.order_no=app.getMaxNumber();// 医嘱序号
        entity.order_sub_no="1";// 子医嘱号 对独立的医嘱，为1，在成组医嘱内部，从1开始顺序排列
        entity.perform_schedule = "";// 医嘱执行时间
		                             // 长期医嘱即：执行时间，临时医嘱：可以为空。（护士转抄时可以手动填写）
		if (mLongTimeBut.isChecked()) {
			entity.perform_schedule = mTimeTev.getText().toString();
		}
		entity.billing_attr = String.valueOf(mZiSpinner
				.getSelectedItemPosition());// 计价属性 反映本条医嘱计价方面的信息。0-正常计价 1-自带药
											// 2-需手工计价
											// 3-不计价。由护士录入医嘱时，根据医嘱内容确定。(一般是0）
		entity.ordering_dept = app.getPatientEntity().dept_code;// 开医嘱科室 staff_dict.dept_code
		entity.drug_billing_attr = "0";// 药品计价属性 反映药品是否计价，0-正常，1-自带药(一般是0）
		if (order_class.equals("A")) {// 如果order_class=A,BILLING_ATTR=1,则c=1,BILLING_ATTR=4,则DRUG_BILLING_ATTR=4
			if (entity.billing_attr.equals("1")) {
				entity.drug_billing_attr = "1";
			}
			if (entity.billing_attr.equals("4")) {
				entity.drug_billing_attr = "4";
			}
		}
		entity.doctor_user = app.getLoginName();// 医生登陆账号    	
        entity.drug_spec=drug_spec;
		
		if (mShortTimeBut.isChecked() && mDrugBut.isChecked()) {// 如果为临药，清空频次执行时间
			butChoose(0, entity);
		}
		if (mShortTimeBut.isChecked() && mNonDrugBut.isChecked()) {// 如果为临非药，清空单位，剂量，途径，频次，执行时间
			butChoose(1, entity);
		}
		if (mLongTimeBut.isChecked() && mNonDrugBut.isChecked()) {// 如果为长非药，清空单位，剂量，途径
			if(entity.frequency.equals("")){
				entity.freq_interval_unit="";
			}else{
				entity.freq_interval_unit="日";
			}
			butChoose(2, entity);
		}
		if(mLongTimeBut.isChecked()&& mDrugBut.isChecked()){ //如果为长期药品
			butChoose(3, entity);
		}
		//途径为“必要时”，freq_interval_unit=“”，freq_counter,freq_interval为0
		if ("必要时".equals(entity.frequency)) {
			butChoose(4, entity);
		}
		if (mSubCheckBox.isChecked()) {//设置子医嘱
			setSubOrders(entity);
		}
		String sql=ServerDao.getInsertOrders(entity);
		new InsertDcAdviceTask(getActivity(), sql).execute();
		
		int isDrug=0; 
		if (!mDrugBut.isChecked()) {
			isDrug=1;
		}
		new PriceTask(getActivity(), entity, isDrug).execute();
	}
	
	/**
	 * 
	* @Title: butChoose 
	* @Description: TODO(根据选中的不同类型，设置数据) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void butChoose(int id,DcAdviceEntity entity){
		switch (id) {
		case 0:
			entity.frequency = "";
			entity.freq_counter = "1";
			entity.freq_interval = "1";
			entity.freq_interval_unit = "日";
			entity.perform_schedule = "";
			break;
		case 1:
			entity.dosage = "";
			entity.dosage_units = "";
			entity.administration = "";
			entity.frequency = "";
			entity.freq_counter = "1";
			entity.freq_interval = "1";
			entity.freq_interval_unit = "日";
			entity.perform_schedule = "";
			break;
		case 2:
			entity.dosage = "";
			entity.dosage_units = "";
			entity.administration = "";
			break;
		case 3:
			entity.freq_interval_unit = "日";
			break;
		case 4:
			entity.freq_interval_unit="";
		default:
			break;
		}
	}
	
	
	/**
	 * 
	* @Title: setSubOrders 
	* @Description: TODO(如果为子医嘱，则重新设置信息) 
	* @param @param entity    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void setSubOrders(DcAdviceEntity entity){
		entity.start_date_time=subEntity.start_date_time;
		entity.administration=subEntity.administration;
		entity.frequency=subEntity.frequency;
		entity.freq_counter=subEntity.freq_counter;
		entity.freq_interval=subEntity.freq_interval;
		entity.freq_interval_unit=subEntity.freq_interval_unit;
		entity.repeat_indicator=subEntity.repeat_indicator;
		entity.order_no=subEntity.order_no;
		int order_sub_no=Integer.parseInt(subEntity.order_sub_no)+1;
		entity.order_sub_no=String.valueOf(order_sub_no);
		entity.perform_schedule=subEntity.perform_schedule;
		entity.ordering_dept=subEntity.ordering_dept;
		entity.drug_billing_attr=entity.drug_billing_attr;
		entity.enter_date_time=subEntity.enter_date_time;
	}
	
	/**
	 * 
	* @Title: validate 
	* @Description: TODO(是否有必填信息) 
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public boolean validate(){
		if (mDcAdViceTev.getText().toString().equals("")) {
			return false;
		}
		if (mDrugBut.isChecked()&&!mSubCheckBox.isChecked()) {
			if (mLongTimeBut.isChecked()) {
				if (mFreqSpinner.getSelectedItemPosition()==0||mWaySpinner.getSelectedItemPosition()==0) {
					return false;
				}
			}
			if (mShortTimeBut.isChecked()&&!mSubCheckBox.isChecked()) {
				if (mWaySpinner.getSelectedItemPosition()==0) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String administration="";//途径
		String frequency="";//频次
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.add_dcadvice_spinner_administration:
			administration=parent.getItemAtPosition(position).toString();
			frequency=mFreqSpinner.getItemAtPosition(mFreqSpinner.getSelectedItemPosition()).toString();
			new TimeTask(mTimeTev,null).execute(administration,frequency);
			break;
		case R.id.add_dcadvice_spinner_frequency:
			frequency=parent.getItemAtPosition(position).toString();
			administration=mWaySpinner.getItemAtPosition(mWaySpinner.getSelectedItemPosition()).toString();
			new TimeTask(mTimeTev,null).execute(administration,frequency);
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
	* @ClassName: TimeTask 
	* @Description: TODO(通过途径，频次获取执行时间任务) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2012-12-25 上午8:37:55 
	*
	 */
	public static class TimeTask extends AsyncTask<String, Void, String>{
		private TextView mTextView;
		private TimeTaskCallback mTaskCallback;
		
		public TimeTask(TextView textView,TimeTaskCallback taskCallback){
			this.mTextView=textView;
			this.mTaskCallback=taskCallback;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (params[0].equals("")) {
				params[0]=" is NULL";
			}
			String sqlStr = "select default_schedule from perform_default_schedule where freq_desc='"
					+ params[1] + "' and administration='" + params[0] + "'";
			String time="";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sqlStr);
			for (int i = 0; i < dataList.size(); i++) {
				time=dataList.get(i).get("default_schedule").trim();
			}
			return time;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (mTextView!=null) {
				mTextView.setText(result);
			}else {
				mTaskCallback.callback(result);
			}
		}
	}
	
	/**
	 * 
	* @ClassName: TimeTaskCallback 
	* @Description: TODO(获取执行时间回调方法) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2013-1-2 上午10:40:14 
	*
	 */
	
	public interface TimeTaskCallback{
		void callback(String result);
	}
	/**
	 * 
	* @ClassName: getOrderCode 
	* @Description: TODO(获取药品代码  抗生素限制使用) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2013-1-2 上午10:40:14 
	*
	 */
    public String getOrderCode(){
		return this.order_code;
	}



}
