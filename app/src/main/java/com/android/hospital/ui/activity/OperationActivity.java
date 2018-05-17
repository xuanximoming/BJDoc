package com.android.hospital.ui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.InspectionDetailAdapter;
import com.android.hospital.adapter.OperationAdapter;
import com.android.hospital.asyntask.DepartmentTask;
import com.android.hospital.asyntask.add.MaxNumberTask;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.InspectiondetailEntity;
import com.android.hospital.entity.OperationEntity;
import com.android.hospital.ui.fragment.AddCheckFragment;
import com.android.hospital.ui.fragment.LeftListFragment;
import com.android.hospital.ui.fragment.SeachCheckFragment;
import com.android.hospital.ui.fragment.SearchFragment;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;
import com.android.hospital.widgets.MyProssDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
* @ClassName: OperationActivity 
* @Description: TODO(手术查询显示界面) 
* @author lll
* @date 2013-01-16  
*
 */
public class OperationActivity extends Activity implements OnClickListener{
	
	private Button mCancleBut,mOkBut,mDeleteBut;
	private Button startBut, endBut; //开始、结束时间按钮
    private Spinner mSpinner; //科室spinner
    private ListView mListView;
    private HospitalApp app;
    private List<String> mDeptList;
    private ArrayList<OperationEntity> arrayList;
    private OperationAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operation_query);
		app=(HospitalApp) getApplication();
		mDeptList=app.getDepartnameList();
        mSpinner=(Spinner) findViewById(R.id.operation_department);	
		setSpinner();
		initView();
	}
	
	/**
	 * 
	* @Title: initView 
	* @Description: TODO(初始化控件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void initView(){
		mListView=(ListView) findViewById(R.id.operation_listview);
		mCancleBut=(Button) findViewById(R.id.common_but_cancle);
		mOkBut=(Button) findViewById(R.id.common_but_ok);
		mDeleteBut=(Button)findViewById(R.id.common_but_delete);
		findViewById(R.id.common_but_delete).setVisibility(View.GONE); //删除暂时隐藏
		startBut=(Button) findViewById(R.id.start_time_but);
		endBut=(Button) findViewById(R.id.end_time_but);
		
		mCancleBut.setOnClickListener(this);
		mOkBut.setOnClickListener(this);
		mDeleteBut.setOnClickListener(this);
		startBut.setOnClickListener(this);
		endBut.setOnClickListener(this);
		showDatePickerDialog();//显示时间
	}
	
	/**
	 * 
	* @Title: setSpinner 
	* @Description: TODO(设置科室spinner) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void setSpinner(){
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mDeptList );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
	}
	
	/**
	 * 
	* @Title: onClick 
	* @Description: TODO(button按钮点击事件) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.common_but_cancle:
			//取消，关闭界面
			intent=new Intent();
			setResult(1, intent);
			finish();
			break;
		case R.id.common_but_ok:
			//执行查询任务
			if(adapter!=null){
				adapter.clearAdapter();
			}	
			new OperationCheckTask().execute();
			break;
		case R.id.common_but_delete:
			//暂时不用
			break;
		default:
			break;
		}	
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	/**
	 * 
	* @Title: getSql 
	* @Description: TODO(得到获取手术的sql语句) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public  String getsql(){	
		int location=mSpinner.getSelectedItemPosition();//选择的position
		String departcode=app.getDepartcodeList().get(location);  //获得所选择的科室 departcode
		String tableName="pat_master_index,operation_schedule,scheduled_operation_name,pats_in_hospital";
		String[] paramArray1=new String[]{"scheduled_date_time","operating_room_no","sequence","operation_schedule.patient_id",
				"inp_no","name", "dept_stayed","diag_before_operation","operation_no",
				"operation","isolation_indicator","surgeon", "first_assistant",
				"second_assistant", "third_assistant","fourth_assistant",
				"anesthesia_method","anesthesia_doctor", "anesthesia_assistant",
				"blood_tran_doctor","operation_schedule.bed_no","operation_schedule.visit_id",
				"operation_schedule.schedule_id", "operating_room", "pat_master_index.sex",
				"notes_on_operation","ack_indicator" };
		String customWhere=" where operation_schedule.patient_id = scheduled_operation_name.patient_id "+
                                 " and operation_schedule.visit_id = scheduled_operation_name.visit_id"+
                                 " and operation_schedule.schedule_id =scheduled_operation_name.schedule_id"+
                                 " and scheduled_operation_name.patient_id = pat_master_index.patient_id"+
                                 " and scheduled_operation_name.patient_id = pats_in_hospital.patient_id"+
                                 " and (operation_schedule.dept_stayed = '"+departcode+"')";
		String operationSql= " and TO_CHAR(scheduled_date_time,'yyyy-MM-dd')>='"+ startBut.getText().toString()+"'"+
					             " and TO_CHAR(scheduled_date_time,'yyyy-MM-dd')<='"+ endBut.getText().toString() + "' ";
		String orderby="  order by scheduled_date_time  asc, operating_room_no asc,sequence asc";
		String sql=ServerDao.getQueryCustom(tableName, paramArray1, customWhere);
		sql=sql+operationSql+orderby;
		Log.e("手术查询时的语句---->", sql);
		return sql;	
	}
			
	/**
	 * 
	* @ClassName: OperationCheckTask 
	* @Description: TODO(手术查询数据任务) 
	* @author lll 
	* @date 2013-01-16 
	*
	 */
	private class OperationCheckTask extends  AsyncTask<Void, Void, Object>{

		@Override
		protected Object doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(getsql());
			arrayList=OperationEntity.init(dataList);
			return arrayList;
		}
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub		
			if (arrayList.size()!=0) {
				adapter=new OperationAdapter(OperationActivity.this, arrayList);
				mListView.setAdapter(adapter);
			}else{
				Toast.makeText(OperationActivity.this, "没有数据!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	/**
	* @Title: showDatePickerDialog 
	* @Description: TODO(显示时间查询对话框) 
	* @param     设定文件 （年月日）
	* @return void    返回类型 
	* @throws
	 */
	public void showDatePickerDialog(){
		final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
	   
	    startBut.setText(new StringBuilder().append(mYear).append("-")
                // Month is 0 based so add 1
                .append(Util.toQueryTime(mMonth + 1)).append("-")
                .append(Util.toQueryTime(mDay)));
	    endBut.setText(new StringBuilder().append(mYear).append("-")
                // Month is 0 based so add 1
	    		.append(Util.toQueryTime(mMonth + 1)).append("-")
                .append(Util.toQueryTime(mDay)));
	    startBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(OperationActivity.this, mStartDateSetListener, mYear, mMonth, mDay).show();
			}
		});
	    endBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(OperationActivity.this, mEndDateSetListener, mYear, mMonth, mDay).show();
			}
		});
	}
	
	private DatePickerDialog.OnDateSetListener mStartDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	startBut.setText(
                            new StringBuilder().append(year).append("-")
                                    // Month is 0 based so add 1
                                    .append(Util.toQueryTime(monthOfYear + 1)).append("-")
                                    .append(Util.toQueryTime(dayOfMonth))
                                    );
                }
            };
    private DatePickerDialog.OnDateSetListener mEndDateSetListener =
                    new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                int dayOfMonth) {
                        	endBut.setText(
                                    new StringBuilder().append(year).append("-")
                                            // Month is 0 based so add 1
                                    .append(Util.toQueryTime(monthOfYear + 1)).append("-")
                                    .append(Util.toQueryTime(dayOfMonth))
                                    );
                        }
                    };
                    

}
