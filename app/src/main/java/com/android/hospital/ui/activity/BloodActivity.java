package com.android.hospital.ui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.BloodAdapter;
import com.android.hospital.asyntask.DepartmentTask;
import com.android.hospital.asyntask.add.MaxNumberTask;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.BloodEntity;
import com.android.hospital.entity.DataEntity;
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
import android.renderscript.Sampler.Value;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
* @ClassName: BloodActivity 
* @Description: TODO(用血查询显示界面) 
* @author lll
* @date 2013-02-17  
*
 */
public class BloodActivity extends Activity implements OnClickListener{
	
	private Button mCancleBut,mOkBut;
	private Button startBut, endBut; 
    private Spinner mSpinner;//科室spinner
    private EditText mEditText;
    private ListView mListView;
    private HospitalApp app;
    private List<String> mDeptList;//科室列表
    private ArrayList<BloodEntity> arrayList;//用血明细信息列表
    private BloodAdapter adapter;
    private String pat_nameString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blood_query);
		app=(HospitalApp) getApplication();
		mDeptList=app.getDepartnameList(); //获取科室列表
		mEditText=(EditText) findViewById(R.id.blood_patient);
        mSpinner=(Spinner) findViewById(R.id.blood_department);	
		setSpinner();//设置spinner
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
		mListView=(ListView) findViewById(R.id.blood_listview);
		mCancleBut=(Button) findViewById(R.id.common_but_cancle);
		mOkBut=(Button) findViewById(R.id.common_but_ok);
		startBut=(Button) findViewById(R.id.start_time_but);
		endBut=(Button) findViewById(R.id.end_time_but);
		
		mCancleBut.setOnClickListener(this);
		mOkBut.setOnClickListener(this);
		startBut.setOnClickListener(this);
		endBut.setOnClickListener(this);
		showDatePickerDialog();//显示时间查询对话框
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
		//查询时，可以不选择科室，所以要添加一个”“
		//实现如下：将科室列表重新赋值到mDeptList_end中，然后再用adapter实现
		List<String> mDeptList_end=new ArrayList<String>();
		mDeptList_end.add("");
		for (int i = 0; i < mDeptList.size(); i++) {
			String item=mDeptList.get(i);
			mDeptList_end.add(item);
		}
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mDeptList_end ); 
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
			intent=new Intent();
			setResult(1, intent);
			finish();
			break;
		case R.id.common_but_ok:
			//执行查询任务
			if(adapter!=null){
				adapter.clearAdapter();
			}	
			new BloodCheckTask().execute();//执行查询任务
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
		//获得所选择的科室 departcode  app.getDepartcodeList()科室代码列表 
		//mSpinner.getSelectedItemPosition()选择的position
		int location = mSpinner.getSelectedItemPosition();
		String departcode = "";
		if (location>0) { //选择了科室，则找到对应的科室代码，需要-1，因为显示科室名称时，多一个”空“
			 departcode = app.getDepartcodeList().get(location-1);
		}
		pat_nameString = "";
		if (null == mEditText.getText().toString()) {
			pat_nameString = "";
		}
		else{
			pat_nameString = mEditText.getText().toString();
		}
		String tableName = "blood_apply , blood_capacity ,blood_component ,dept_dict ";
		String[] paramArray1 = new String[]{"blood_apply.apply_num","patient_id","dept_dict.dept_name","blood_apply.dept_code",
				                          "pat_name","blood_inuse","blood_diagnose", "pat_blood_group",
				                          "rh","blood_sum","apply_date", "price","fast_slow", "to_char(blood_capacity.trans_date,'yyyy-MM-dd hh24:mi:ss')  as trans_date",
				                          "trans_capacity", "blood_component.blood_type_name" };
		String customWhere = " where (blood_apply.apply_num = blood_capacity.apply_num) "+
                                 " and blood_capacity. blood_type = blood_component.blood_type"+
                                 " and blood_apply.dept_code = dept_dict.dept_code";
        String deptSql = " and blood_apply.dept_code ='"+departcode+"'";
		String operationSql = " and ((to_char(blood_capacity.trans_date,'yyyy-MM-dd') between'"+ startBut.getText().toString()+"'"+
					             " and '"+ endBut.getText().toString() + "') or (blood_apply.pat_name='"+pat_nameString+"'))";
		String orderby = "  order by dept_code,pat_name,trans_date";
		String sql = ServerDao.getQueryCustom(tableName, paramArray1, customWhere);
		if (location>0) {
			sql = sql + deptSql;
		}
		sql = sql + operationSql + orderby;
		Log.e("用血信息-->", sql);
		return sql;	
	}
			
	/**
	 * 
	* @ClassName: BloodCheckTask 
	* @Description: TODO(用血查询数据任务) 
	* @author lll 
	* @date 2013-02-17 
	*
	 */
	private class BloodCheckTask extends  AsyncTask<Void, Void, Object>{

		@Override
		protected Object doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(getsql());
			arrayList=BloodEntity.init(dataList);
			return arrayList;
		}
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			
			if (arrayList.size()!=0) {
				adapter=new BloodAdapter(BloodActivity.this, arrayList);
				mListView.setAdapter(adapter);
			}else{
				Toast.makeText(BloodActivity.this, "没有数据!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	* @Title: showDatePickerDialog 
	* @Description: TODO(显示时间查询对话框) 
	* @param     设定文件 
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
				new DatePickerDialog(BloodActivity.this, mStartDateSetListener, mYear, mMonth, mDay).show();
			}
		});
	    endBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(BloodActivity.this, mEndDateSetListener, mYear, mMonth, mDay).show();
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
