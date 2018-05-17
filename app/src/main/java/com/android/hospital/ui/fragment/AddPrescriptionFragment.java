package com.android.hospital.ui.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.PrescriptionLeftItemAdapter;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.DrugEntity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;

import android.R.integer;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
* @ClassName: AddInspectionFragment 
* @Description: TODO(新增处方界面) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-21 下午11:54:54 
*
 */
public class AddPrescriptionFragment extends BaseFragment implements OnClickListener,OnItemSelectedListener{
    private View view;
	private Spinner mClassSp;
	private CheckBox mTaskDrugBox,mIsOrNotBox;//出院带药,是否带煎
	private TextView mDrugHouseTev,mNextvalTev,mTempJudgeTev,mAgentNumTev,mEachNumTev;//药局，处方，诊断,剂数，每剂煎
	private Button mAgentPlusBut,mAgentMinusBut,mEachPlusBut,mEachMinusBut;//加减
	private EditText mHowEdit,mNextvalEdit;//用法，处方名
	private ListView mListView;
	private LinearLayout mHideLayout;
	private HospitalApp app;
	private ArrayList<DrugEntity> mWestDrugList;//西药
	private SeachPrescriptionFragment fm;//搜索界面
	private PrescriptionLeftItemAdapter mAdapter;
	private int drugFlag=0;//0为中药，1为西药
	private ArrayList<DrugEntity> insertList;//需要插入的集合 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app=(HospitalApp) getActivity().getApplication();
		fm=(SeachPrescriptionFragment) getActivity().getFragmentManager().findFragmentByTag("searchfm");
		new WestDrugTaskStep1().execute();//获取西药任务
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_add_prescription_left, null);
		init();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mAdapter=new PrescriptionLeftItemAdapter(getActivity(), null);
		mListView.setAdapter(mAdapter);	
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		mClassSp=(Spinner) view.findViewById(R.id.add_prescription_spinner_1);
		mTaskDrugBox=(CheckBox) view.findViewById(R.id.add_prescription_checkbox_taskdrug);
		mIsOrNotBox=(CheckBox) view.findViewById(R.id.add_prescription_checkbox_isornot);
		mDrugHouseTev=(TextView) view.findViewById(R.id.add_prescription_tev_1);
		mNextvalTev=(TextView) view.findViewById(R.id.add_prescription_tev_2);
		mTempJudgeTev=(TextView) view.findViewById(R.id.add_prescription_tev_3);
		mAgentNumTev=(TextView) view.findViewById(R.id.add_prescription_tev_agent);
		mEachNumTev=(TextView) view.findViewById(R.id.add_prescription_tev_each);
		mAgentPlusBut=(Button) view.findViewById(R.id.add_prescription_but_agentplus);
		mAgentMinusBut=(Button) view.findViewById(R.id.add_prescription_but_agentminus);
		mEachPlusBut=(Button) view.findViewById(R.id.add_prescription_but_eachplus);
		mEachMinusBut=(Button) view.findViewById(R.id.add_prescription_but_eachminus);
		mHowEdit=(EditText) view.findViewById(R.id.add_prescription_edit_how);
		mNextvalEdit=(EditText) view.findViewById(R.id.add_prescription_edit_nextval);
		mListView=(ListView) view.findViewById(R.id.add_prescription_listview);
		mHideLayout=(LinearLayout) view.findViewById(R.id.prescription_hidelayout);
		
		mTempJudgeTev.setText(app.getPatientEntity().diagnosis);//诊断
		mAgentPlusBut.setOnClickListener(this);
		mAgentMinusBut.setOnClickListener(this);
		mEachPlusBut.setOnClickListener(this);
		mEachMinusBut.setOnClickListener(this);
		mClassSp.setOnItemSelectedListener(this);
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
		if (mClassSp.getSelectedItemPosition()==0) {
			if (mNextvalEdit.getText().toString().equals("")) {
				Toast.makeText(getActivity(), "处方名不能为空！", Toast.LENGTH_SHORT).show();
				return false;
			}
			if (numNull()) {
				return true;
			}else {
				Toast.makeText(getActivity(), "剂量和总量不能为空！", Toast.LENGTH_SHORT).show();
				return false;
			}
		}else {
			return true;
		}
	}

	@Override
	public void onOtherFragmentClick() {
		// TODO Auto-generated method stub
		
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
		prescriptionInsertSql(); //处方插入
	}

	/**
	 * 
	* @Title: setListView 
	* @Description: TODO(设置选中的药品列表) 
	* @param     不同类别，不同药房 的药品，不能同时显示在列表中
	* @return void    返回类型 
	* @throws
	 */
	public void setListView(DrugEntity entity){
		
		if (mAdapter.getCount()==1) {
			drugFlag=mClassSp.getSelectedItemPosition();//药品类别
		}
		if (mAdapter.getCount()>1) {
			if (drugFlag!=mClassSp.getSelectedItemId()) {
				Toast.makeText(getActivity(), "中药和西药不能开在一张单子上!!", Toast.LENGTH_SHORT).show();
				return;
			}			
		}
		if (!mDrugHouseTev.getText().equals("")) {
			if (!entity.storage_name.equals(mDrugHouseTev.getText().toString())) {
				Toast.makeText(getActivity(), "药房不同不能开在一张单子上!!", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		mDrugHouseTev.setText(entity.storage_name);
		mAdapter.addItem(entity, mClassSp.getSelectedItemPosition());	
	}
	
	/**
	 * 
	* @Title: clear 
	* @Description: TODO(清空按钮) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void clear(){
		mAdapter.clear();
		mClassSp.setSelection(0);
		mDrugHouseTev.setText("");
		drugFlag=0;		
	}
	
	/**
	 * 
	* @Title: onClick 
	* @Description: TODO(加减点击事件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_prescription_but_agentplus:
			int num=Integer.parseInt(mAgentNumTev.getText().toString());
			if (num<20) {
				num++;
				mAgentNumTev.setText(Integer.toString(num));
			}
			break;
		case R.id.add_prescription_but_agentminus:
			int numMinus=Integer.parseInt(mAgentNumTev.getText().toString());
			if (numMinus>1) {
				numMinus--;
				mAgentNumTev.setText(Integer.toString(numMinus));
			}
			break;
		case R.id.add_prescription_but_eachplus:
			int num2=0;
			if (!"".equals(mEachNumTev.getText().toString())) {
				num2=Integer.parseInt(mEachNumTev.getText().toString());
			}
			if (num2<20) {
				num2++;
				mEachNumTev.setText(Integer.toString(num2));
			}
			break;
		case R.id.add_prescription_but_eachminus:
			int numMinus2=0;
			if (!"".equals(mEachNumTev.getText().toString())) {
				numMinus2=Integer.parseInt(mEachNumTev.getText().toString());
			}
			if (numMinus2>1) {
				numMinus2--;
				mEachNumTev.setText(Integer.toString(numMinus2));
			}else {
				mEachNumTev.setText("");
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	* @Title: onItemSelected 
	* @Description: TODO(药品类别选择事件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			fm.show(null);
			mHideLayout.setVisibility(View.VISIBLE);
			if(mAdapter!=null){
				mAdapter.clear();
			}
			mDrugHouseTev.setText("");//药房
			break;
		case 1:
			DebugUtil.debug("positon-case--1");
			if (mWestDrugList!=null) {
				fm.showWestDrug(mWestDrugList);//药品检索fragment中的showWestDrug（）函数
											   //西药根据医嘱提取
			}
			mHideLayout.setVisibility(View.GONE);
			if(mAdapter!=null){
				mAdapter.clear();
			}
			mDrugHouseTev.setText("");
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
	* @ClassName: WestDrugTaskStep1 
	* @Description: TODO(获取西药步骤) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2012-12-26 下午5:03:29 
	*
	 */
	private class WestDrugTaskStep1 extends AsyncTask<Void, Void, List<String>>{

		@Override
		protected List<String> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String sql = "select order_code from orders where patient_id = '"
					+ app.getPatientEntity().patient_id
					+ "'"
					+ " and visit_id = '"
					+ app.getPatientEntity().visit_id
					+ "'"
					+ " and order_class = 'A'"
					+ " and ((start_date_time < sysdate and start_date_time >sysdate-1) or ( repeat_indicator = '1' and administration in( '口服','雾化吸入')))";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
			int size=dataList.size();
			List<String> drug_codeList=new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				drug_codeList.add(dataList.get(i).get("order_code"));
			}
			return drug_codeList;
		}
		@Override
		protected void onPostExecute(List<String> result) {
			// TODO Auto-generated method stub
			mNextvalTev.setText(app.getNextval());//处方号
			new WestDrugTaskStep2().execute(result);
		}
	}	
	private class WestDrugTaskStep2 extends AsyncTask<List<String>, Void, ArrayList<DrugEntity>>{

		@Override
		protected ArrayList<DrugEntity> doInBackground(List<String>... params) {
			// TODO Auto-generated method stub
			StringBuffer buffer=new StringBuffer();
			buffer.append("select * from input_drug_lists where storage in ('301201', '301304') and drug_code in(");
			//3102中药房,3103住院西药房   （新）301201西药房   301304 中药房
			int size=params[0].size();
			for (int i = 0; i < size; i++) {
				if (i==(size-1)) {
					buffer.append("'").append(params[0].get(i)).append("')");
					break;
				}
				buffer.append("'").append(params[0].get(i)).append("',");
			}
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(buffer.toString());
			ArrayList<DrugEntity> westDrugList=DrugEntity.init(dataList);
			return westDrugList;
		}
		@Override
		protected void onPostExecute(ArrayList<DrugEntity> result) {
			// TODO Auto-generated method stub
            mWestDrugList=result;
		}
	}
	
	/**
	 * 
	* @Title: prescriptionInsertSql 
	* @Description: TODO(处方插入方法) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void prescriptionInsertSql(){
		String start_date_time=Util.toSimpleDate();
		String presc_type="0";  //处方类型 0西药 1中药
		String dispensary="301201"; //药房代码
		String decoction=""; //是否代煎（中药）（没有为空，代煎为1）
		String presc_no=mNextvalTev.getText().toString(); //处方号
		String repetition=mAgentNumTev.getText().toString(); // 剂数 （对于中药，数量变化），西药为1
		String count_per_repetition=mEachNumTev.getText().toString(); // 每剂份数（中药）
		String usage=mHowEdit.getText().toString(); //用法（中药）
		String binding_presc_title=mNextvalEdit.getText().toString(); //处方名（中药）
		String discharge_taking_indicator=""; //出院带药标志（1为出院带药，没有为空）
		if (mTaskDrugBox.isChecked()) {
			discharge_taking_indicator="1";
		}
		if (mClassSp.getSelectedItemPosition()==0) {//如果为中药
			if (binding_presc_title.equals("")) {
				return;
			}
			dispensary="301304";
			presc_type="1";
			if (mIsOrNotBox.isChecked()) {
				decoction="1";
			}
		}else {
			//西药处方中，也有药品在中药房拿药，需要判断。如果选中的药品为中药房，则发往中药房
			if("中药房".equals(mDrugHouseTev.getText().toString())){
				dispensary="301304";
			}
			repetition="1";
			count_per_repetition="";
			usage="";
			binding_presc_title="";
		}	
		String costs=account();//计算价格方法
		
		StringBuffer prescriptionBuffer=new StringBuffer();
		prescriptionBuffer.append("insert into  doct_drug_presc_master"+
				"(patient_id,"+ //  病人id
				"visit_id,"+  //   住院次数
				"name,"+       //  姓名
				"name_phonetic,"+//姓名拼音码 
				"presc_date,"+  // 处方日期（系统时间）
				"identity,"+    // 身份（农民，工人等）
				"charge_type,"+ // 费别
				"ordered_by,"+  // 开单科室（病人所在的科室代码）
				"prescribed_by,"+ //执行人代码  （开单医生，登录医生的名字）-------
				"presc_attr, "+ // 处方属性（填空）--------------
				"dispensary,"+  // 发药药局（药局代码）
				"presc_source,"+ //处方来源（门诊，住院）住院为1-------
				"unit_in_contract,"+ //合同单位（填空）
				"presc_no,"+    //处方号
				"presc_type,"+  //处方类型 0西药 1中药 
				"repetition,"+  // 剂数 （对于中药而言，数量变化）一般为1，西药为1
				"costs, "+      // 计价金额（总花费）
				"payments, "+   // 应收金额（同上）
				"entered_by,"+ // 确认人代码（开单医生，登录医生的名字）-------
				"presc_status,"+// 处方状态 （为0，拿了药的为1，此处为0）
				"dispensing_provider,"+ // 确认人名称 （空）--------
				"count_per_repetition,"+// 每剂份数（中药）（可以为空）
				"usage,"+       //用法（中药）
				"binding_presc_title,"+ //处方名（中药）
				"discharge_taking_indicator,"+ //出院带药标志（1为出院带药，没有为空）
				"doctor_user,"+ //医生代码 （该病人的主治医生代码）
				"decoction,"+   //是否代煎（中药）（没有为空，代煎为1）
				"newly_print,"+ //是否重打（没有为空）
				"diagnosis_name)"+ //诊断名称
			"values(");
		prescriptionBuffer.append("'"+app.getPatientEntity().patient_id).append("',");
		prescriptionBuffer.append("'"+app.getPatientEntity().visit_id).append("',");
		prescriptionBuffer.append("'"+app.getPatientEntity().name).append("',");
		prescriptionBuffer.append("'"+app.getPatientEntity().name_phonetic).append("',");
		prescriptionBuffer.append("TO_DATE('"+start_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
		prescriptionBuffer.append("'"+app.getPatientEntity().identity).append("',");
		prescriptionBuffer.append("'"+app.getPatientEntity().charge_type).append("',");
		prescriptionBuffer.append("'"+app.getPatientEntity().dept_code).append("',");
		prescriptionBuffer.append("'"+app.getDoctor()).append("',");
		prescriptionBuffer.append("'"+"").append("',");
		prescriptionBuffer.append("'"+dispensary).append("',");
		prescriptionBuffer.append("'"+"1").append("',");
		prescriptionBuffer.append("'"+"").append("',");
		prescriptionBuffer.append("'"+presc_no).append("',");
		prescriptionBuffer.append("'"+presc_type).append("',");
		prescriptionBuffer.append("'"+repetition).append("',");
		prescriptionBuffer.append("'"+costs).append("',");
		prescriptionBuffer.append("'"+costs).append("',");
		prescriptionBuffer.append("'"+app.getDoctor()).append("',");
		prescriptionBuffer.append("'"+"0").append("',");
		prescriptionBuffer.append("'"+"").append("',");
		prescriptionBuffer.append("'"+count_per_repetition).append("',");
		prescriptionBuffer.append("'"+usage).append("',");
		prescriptionBuffer.append("'"+binding_presc_title).append("',");
		prescriptionBuffer.append("'"+discharge_taking_indicator).append("',");
		prescriptionBuffer.append("'"+app.getPatientEntity().user_name).append("',");//主治医生代码
		prescriptionBuffer.append("'"+decoction).append("',");
		prescriptionBuffer.append("'"+"").append("',");
		prescriptionBuffer.append("'"+app.getPatientEntity().diagnosis).append("')");
		WebServiceHelper.insertWebServiceData(prescriptionBuffer.toString());
		for (int i = 0; i < insertList.size(); i++) {
			int item_no=i+1;
			DrugEntity itemEntity=insertList.get(i);
			if("是".equals(itemEntity.is_basic)){
				itemEntity.is_basic="1";
			}else{
				itemEntity.is_basic="";
			}
			StringBuffer buffer=new StringBuffer();
			buffer.append("insert into doct_drug_presc_detail" + " (presc_date,"
					+ // 处方日期（系统时间）
					"presc_no," + // 处方号
					"item_no," + // 项目序号（自动编号，从1开始）
					"order_no," + // 医嘱序号 （空）最好西药时能填入
					"order_sub_no," + // 医嘱子序号（空）
					"drug_code," + // 药品编码
					"drug_spec," + // 药品规格
					"drug_name," + // 药品名称
					"firm_id," + // 生产厂商
					"package_spec," + // 包装规格
					"package_units," + // 包装单位（总量的单位）
					"quantity," + // 数量（即总量）
					"costs," + // 费用，价格（单个项目的费用）
					"payments," + // 已付费用（单个项目的费用，同上）
					"administration," + // 途径
					"dosage," + // 剂量（即总量*包装规格）
					"dosage_units," + // 剂量单位（单次剂量的单位）
					"amount_per_package," + // 每包装数量（空）
					"frequency," + // 执行频次
					"dosage_each," + // 单次剂量
					"freq_detail," + // 医生说明
					"is_basic) " + // 是否为基药（不是为空，基药填1）
					"values(");
			buffer.append("TO_DATE('"+start_date_time).append("','yyyy-MM-dd hh24:mi:ss'),");
			buffer.append("'"+presc_no).append("',");
			buffer.append("'"+item_no).append("',");
			buffer.append("'"+"").append("',");
			buffer.append("'"+"").append("',");
			buffer.append("'"+itemEntity.drug_code).append("',");
			buffer.append("'"+itemEntity.drug_spec).append("',");
			buffer.append("'"+itemEntity.drug_name).append("',");
			buffer.append("'"+itemEntity.firm_id).append("',");
			buffer.append("'"+itemEntity.package_spec).append("',");
			buffer.append("'"+itemEntity.package_units).append("',");
			buffer.append("'"+itemEntity.quantity).append("',");
			buffer.append("'"+itemEntity.single_price).append("',");
			buffer.append("'"+itemEntity.single_price).append("',");
			buffer.append("'"+itemEntity.administration).append("',");
			buffer.append("'"+itemEntity.dosage).append("',");
			buffer.append("'"+itemEntity.dose_units).append("',");
			buffer.append("'"+"").append("',");
			buffer.append("'"+itemEntity.frequency).append("',");
			buffer.append("'"+itemEntity.dosage_each).append("',");
			buffer.append("'"+itemEntity.freq_detail).append("',");
			buffer.append("'"+itemEntity.is_basic.trim()).append("')");
			WebServiceHelper.insertWebServiceData(buffer.toString());
		}
	}
	
	/**
	 * 
	* @Title: account 
	* @Description: TODO(计算价格) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public String account(){
		insertList=new ArrayList<DrugEntity>();
		double total_price=0;//总价格
		int size=mAdapter.getCount();
		if (size!=0) {
			for (int i = 0; i < size; i++) {
				DrugEntity itemEntity=(DrugEntity) mAdapter.getItem(i);
				StringBuffer buffer=new StringBuffer();
				buffer.append(itemEntity.quantity).append("/").append(itemEntity.package_units);//总量/单位
				itemEntity.freq_detail=buffer.toString();
				if (itemEntity.quantity==null) {
					itemEntity.quantity="";
				}
				if (itemEntity.quantity.equals("")||itemEntity.quantity.equals("0")) {
					itemEntity.quantity="1";
				}
				double num=Double.parseDouble(itemEntity.quantity);//计算是总量*价格
				String purchase_price=itemEntity.purchase_price;
				if (null==purchase_price||purchase_price.equals("")) {
					purchase_price="1";
				}
				double num1=Double.parseDouble(purchase_price);
				double num2=num*num1;
				itemEntity.single_price=String.valueOf(num2);
				total_price+=num2;
				itemEntity.total_price=String.valueOf(total_price);
				DebugUtil.debug("所的num值-->"+num+","+num1+","+num2+","+total_price);
				accout2(itemEntity);//继续计算
			}
			return String.valueOf(total_price);
		}
		return "";
	}

	/**
	 * 
	* @Title: accout2 
	* @Description: TODO(获取一些需用到的数据) 
	* @param @param itemEntity    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void accout2(DrugEntity itemEntity) {
		// TODO Auto-generated method stub
		String ssString2 = "select DRUG_CODE,DRUG_SPEC,FIRM_ID,UNITS,AMOUNT_PER_PACKAGE,MIN_SPEC"
				+ " from drug_price_list "
				+ "where drug_code= '"
				+ itemEntity.drug_code
				+ "'"
				+ " and  DRUG_SPEC||FIRM_ID='"
				+ itemEntity.drug_spec + "'" + " and stop_date is null";
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(ssString2);
		for (int i = 0; i < dataList.size(); i++) {
			itemEntity.firm_id=dataList.get(i).get("firm_id");
			itemEntity.package_spec=dataList.get(i).get("drug_spec");
			itemEntity.amount_per_package=dataList.get(i).get("amount_per_package");
			itemEntity.min_spec=dataList.get(i).get("min_spec");
			itemEntity.dosage=accout3(itemEntity);
		}
		insertList.add(itemEntity);
	}
	/**
	 * 
	* @Title: accout3 
	* @Description: TODO(继续计算) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private String accout3(DrugEntity itemEntity){
		boolean arrFlag=false;
		StringBuffer numBuffer=new StringBuffer();
		StringBuffer numBuffer2=new StringBuffer();
		//DebugUtil.debug("param1-->"+param1);
		String[] numArr=itemEntity.min_spec.split("\\.");
		DebugUtil.debug("数组长度--->"+numArr.length);
		if (numArr.length>1) {
			numBuffer.append(numArr[0]).append(".").append(getNum(numArr[1]));
		}else {
			numBuffer.append(getNum(itemEntity.min_spec));
		}  
		DebugUtil.debug("分割后的值"+numBuffer.toString());
		double i=Double.parseDouble(numBuffer.toString());
		double j=Double.parseDouble(itemEntity.amount_per_package);
		if (itemEntity.quantity.equals("")) {
			itemEntity.quantity="1";
		}
		double k=Double.parseDouble(itemEntity.quantity);
		double m=i*j*k;
		DecimalFormat df = new DecimalFormat("0.0000");
		String dosage = df.format(m);

		DebugUtil.debug("m的值-->"+dosage);
		return dosage;
	}
	/**'
	 * 
	* @Title: getNum 
	* @Description: TODO() 
	* @param @param s
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String getNum(String s){
		ArrayList<String> nums = new ArrayList<String>();
		String num = "";
		boolean lastIsNum = false;
		for(int i = 0;i<s.length();i++){
			 if(s.charAt(i)<='9' && s.charAt(i)>='0'){
				   lastIsNum = true;
				   num += s.substring(i,i+1);
			 }else {
				 if (lastIsNum)
					 nums.add(num);
				 num="";
				 lastIsNum=false;
			}
		}
		int[] rs = new int[nums.size()];
		StringBuffer buffer=new StringBuffer();
		for(int i = 0;i<rs.length;i++){
			 rs[i] = Integer.parseInt((nums.get(i)));
			 buffer.append(rs[i]);	 
			 DebugUtil.debug("得到的数字-->"+rs[i]);
		}
		DebugUtil.debug("得到的buffer-->"+buffer.toString());
		return buffer.toString();
	}
	
	/**
	 * 
	* @Title: numNull 
	* @Description: TODO(剂量或者总量是否为空) 
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	private boolean numNull(){
		int size=mAdapter.getCount();
		if (size!=0) {
			for (int i = 0; i < size; i++) {
				DrugEntity itemEntity=(DrugEntity) mAdapter.getItem(i);
				if (itemEntity.dosage_each==null||itemEntity.dosage_each.equals("")) {
					return false;
				}
				if (itemEntity.quantity==null||itemEntity.quantity.equals("")) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
