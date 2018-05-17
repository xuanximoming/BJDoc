package com.android.hospital.ui.activity;

import java.util.ArrayList;
import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.GroupDcAdviceAdapter;
import com.android.hospital.asyntask.add.GroupInsertDcAdviceTask;
import com.android.hospital.asyntask.add.MaxNumberTask;
import com.android.hospital.asyntask.add.PriceTask;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
* @ClassName: GroupDcAdviceActivity 
* @Description: TODO(套餐医嘱明细) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2013-1-1 下午4:58:41 
*
 */
public class GroupDcAdviceActivity extends Activity implements OnClickListener{

	private ListView mListView;
	private Button mCancleBut,mOkBut;
	private String group_order_id;//套餐医嘱id
	private ArrayList<DcAdviceEntity> groupAdviceList;//套餐医嘱明细集合
	private GroupDcAdviceAdapter adapter;
	private LinearLayout prossbarLayout; //界面部分布局
	private HospitalApp app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app=(HospitalApp) getApplication();
		setContentView(R.layout.activity_group_dcadvice);
		Intent intent=getIntent();
		group_order_id=intent.getStringExtra("id");
		mListView=(ListView) findViewById(R.id.group_dcadvice_listview);
		mOkBut=(Button) findViewById(R.id.common_but_ok);
		mCancleBut=(Button) findViewById(R.id.common_but_cancle);
		prossbarLayout=(LinearLayout) findViewById(R.id.group_progressbar);
		
		mOkBut.setOnClickListener(this);
		mCancleBut.setOnClickListener(this);
		new GroupDcAcviceTask().execute();//获取套餐医嘱列表
		new MaxNumberTask(this).execute("");//获取最大序号任务
	}
	
	/**
	 * 
	* @ClassName: GroupDcAcviceTask 
	* @Description: TODO(获取套餐医嘱明细集合) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2013-1-1 下午5:15:18 
	*
	 */
	private class GroupDcAcviceTask extends AsyncTask<Void, Void, String>{
	
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String sql="select group_order_id,item_no,item_sub_no,repeat_indicator,order_class,order_text,order_code,dosage," +
					"dosage_units,administration,frequency,freq_counter,freq_interval,freq_interval_unit,freq_detail,"+
					"drug_spec,drug_billing_attr " +
					"from group_order_items where group_order_id='"+group_order_id+"'";
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
			groupAdviceList=new ArrayList<DcAdviceEntity>();
			String start_time=Util.toSimpleDate();//当前时间赋值
			for (int i = 0; i < dataList.size(); i++) {
				DcAdviceEntity entity=new DcAdviceEntity();
				entity.order_no=dataList.get(i).get("item_no").trim();
				entity.order_sub_no=dataList.get(i).get("item_sub_no").trim();
				entity.repeat_indicator=dataList.get(i).get("repeat_indicator").trim();
				entity.order_class=dataList.get(i).get("order_class").trim();
				entity.order_text=dataList.get(i).get("order_text").trim();
				entity.order_code=dataList.get(i).get("order_code").trim();
				entity.dosage=dataList.get(i).get("dosage").trim();
				entity.dosage_units=dataList.get(i).get("dosage_units").trim();
				entity.administration=dataList.get(i).get("administration").trim();
				entity.frequency=dataList.get(i).get("frequency").trim();
				entity.freq_counter=dataList.get(i).get("freq_counter").trim();
				entity.freq_interval=dataList.get(i).get("freq_interval").trim();
				entity.freq_interval_unit=dataList.get(i).get("freq_interval_unit").trim();
				entity.freq_detail=dataList.get(i).get("freq_detail").trim();
				entity.drug_spec=dataList.get(i).get("drug_spec").trim();
				entity.drug_billing_attr=dataList.get(i).get("drug_billing_attr").trim();
				entity.start_date_time=start_time;
				groupAdviceList.add(entity);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			prossbarLayout.setVisibility(View.GONE); //”载入中。。。“ 隐藏
			adapter=new GroupDcAdviceAdapter(GroupDcAdviceActivity.this, groupAdviceList);
			mListView.setAdapter(adapter);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.common_but_ok:
			if (adapter.getCount()==0) {
				Toast.makeText(getApplicationContext(), "没有数据!", Toast.LENGTH_SHORT).show();
			}else {
				new AlertDialog.Builder(GroupDcAdviceActivity.this)
	            .setIconAttribute(android.R.attr.alertDialogIcon)
	            .setTitle("是否确认提交？")
	            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked OK so do some stuff */
	                	insertGroupDc(); //插入任务
	                }
	            })
	            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    /* User clicked Cancel so do some stuff */
	                }
	            })
	            .create().show();
			}
			break;
		case R.id.common_but_cancle:
			Intent intent=new Intent();
			setResult(1, intent);
			finish();
			break;
		default:
			break;
		}
	}
	/**
	 * 
	* @Title: insertGroupDc 
	* @Description: TODO(插入任务) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void insertGroupDc(){
		int size=adapter.getCount();
		int max_num=Integer.parseInt(app.getMaxNumber())-1;//未添加前，医嘱的最大值
		for (int i = 0; i < size; i++) {
			DcAdviceEntity entity=(DcAdviceEntity) adapter.getItem(i);
			entity.patient_id=app.getPatientEntity().patient_id;//病人ID
	        entity.visit_id=app.getPatientEntity().visit_id;//住院次数
	        entity.order_status="6";//医嘱状态保存为‘6’
	        entity.ordering_dept=app.getPatientEntity().dept_code;//开医嘱科室，即病人所在科室
	        entity.doctor=app.getDoctor();//医生姓名
	        entity.doctor_user=app.getLoginName();//登录的用户名
			entity.enter_date_time=entity.start_date_time;//开医嘱录入日期及时间，（同START_DATE_TIME时间，即保存时系统时间)
			entity.stop_date_time="";// 停止时间,如果为长期，则为空，否则等于当前时间
			if(entity.repeat_indicator.equals("0")){
				entity.stop_date_time = entity.start_date_time;
			}
			if (entity.order_class.equals("A")) {// 如果order_class=A,BILLING_ATTR=1,则DRUG_BILLING_ATTR=1, 若BILLING_ATTR=4,则DRUG_BILLING_ATTR=4
				if (entity.billing_attr!=null&&entity.billing_attr.equals("1")) {
					entity.drug_billing_attr = "1";
			    }
				if (entity.billing_attr!=null&&entity.billing_attr.equals("4")) {
					entity.drug_billing_attr = "4";
				}
			}
			if(entity.order_class.equals("C")||entity.order_class.equals("D")){
				entity.billing_attr="3";
				entity.drug_billing_attr="3";
			}
			if(null==entity.drug_billing_attr||entity.drug_billing_attr.equals("")){
				entity.billing_attr="0";
				entity.drug_billing_attr="0";
			}
			entity.billing_attr=entity.drug_billing_attr;
			if(entity.repeat_indicator.equals("0")){
				if(entity.order_class.equals("A")){
					butChoose(0, entity); // 如果为临药，清空频次执行时间
				}else{
					butChoose(1, entity); // 如果为临非药，清空单位，剂量，途径，频次，执行时间
				}
			}
			if(entity.repeat_indicator.equals("1")){
				if(entity.order_class.equals("A")){
					butChoose(3, entity); //如果为长期药品，当为空时，应该是填充“日”
				}else{
					if(entity.frequency.equals("")){
						entity.freq_interval_unit="";
					}else{
						entity.freq_interval_unit="日";
					}
					butChoose(2, entity); //如果为长非药，清空单位，剂量，途径
				}
			}
			//途径为“必要时”，freq_interval_unit=“”，freq_counter,freq_interval为0
			if ("必要时".equals(entity.frequency)) {
				entity.freq_interval_unit="";
			}
			if(entity.perform_schedule==null||entity.perform_schedule.equals("无")){
				entity.perform_schedule="";
			}
			//对医嘱进行重新编号
			if(entity.order_sub_no.equals("1")){ 
				max_num++;
				entity.order_no=String.valueOf(max_num);
			}else {
				entity.order_no=String.valueOf(max_num);
			}
			String sql=ServerDao.getInsertOrders(entity);
			new GroupInsertDcAdviceTask(this, sql).execute();
			int isDrug=0;
			if (!entity.order_class.equals("A")) {
				isDrug=1;
			}
			new PriceTask(this, entity, isDrug).execute();
		}
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
			entity.freq_interval_unit="日";
		default:
			break;
		}
	}
	
}
