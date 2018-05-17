package com.android.hospital.ui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.android.hospital.HospitalApp;

import com.android.hospital.asyntask.add.MaxNumberTask;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.BloodItemEntity;
import com.android.hospital.entity.BloodTypeEntity;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.ui.fragment.AddBloodFragment;
import com.android.hospital.ui.fragment.LeftListFragment;
import com.android.hospital.ui.fragment.SearchFragment;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;
import com.android.hospital.widgets.MyProssDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
/**
 * 
* @ClassName: AddBloodNewActivity 
* @Description: TODO(用血申请新增项目界面) 
* @author lll 
* @date 2013-03-28  
*
 */
public  class AddBloodNewActivity extends Activity implements OnClickListener{

	private HospitalApp app;	
	private RadioButton bloodmethod1,bloodmethod2,bloodmethod3;//用血方式
	private EditText bloodSum;//用血总量	
	private Button bloodDate,bloodTime;//时间	
	private Spinner bloodxyyq;//用血要求	   bloodUnit,单位、
	private Button mCancleBut,mOkBut;//确定、取消
	private ArrayList<BloodTypeEntity> bloodTypeList;//血液要求列表	
	private BloodItemEntity entity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app=(HospitalApp) getApplication();
		setContentView(R.layout.activity_add_blood_new);
		bloodTypeList=app.getBlood_TypeList(); //获得血液类型列表
		initView();
		setSpinner();
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
		bloodmethod1=(RadioButton) findViewById(R.id.activity_add_blood_new_method1);
		bloodmethod2=(RadioButton) findViewById(R.id.activity_add_blood_new_method2);
		bloodmethod3=(RadioButton) findViewById(R.id.activity_add_blood_new_method3);
		bloodDate=(Button) findViewById(R.id.activity_add_blood_new_bt_date);
		bloodTime=(Button) findViewById(R.id.activity_add_blood_new_bt_time);
		bloodSum=(EditText) findViewById(R.id.activity_add_blood_new_xueliang);
		//bloodUnit=(Spinner) findViewById(R.id.activity_add_blood_new_unit);
		bloodxyyq=(Spinner) findViewById(R.id.activity_add_blood_new_xyyq);
		mCancleBut=(Button) findViewById(R.id.activity_add_blood_new_cancle);
		mOkBut=(Button) findViewById(R.id.activity_add_blood_new_ok);
		
		bloodDate.setOnClickListener(this);
		bloodTime.setOnClickListener(this);
		mCancleBut.setOnClickListener(this);
		mOkBut.setOnClickListener(this);
		bloodmethod1.setOnClickListener(this);
		bloodmethod2.setOnClickListener(this);
		bloodmethod3.setOnClickListener(this);
		showDatePickerDialog(); //日期对话框
		showTimePickerDialog(); //时间对话框
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
		List<String> list=new ArrayList<String>();
		list.add("");
		for (int i = 0; i < bloodTypeList.size(); i++) {
			String item=bloodTypeList.get(i).blood_type_name;
			list.add(item);
		}
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bloodxyyq.setAdapter(adapter); 
	}

	/**
	 * 
	* @Title: getAddData 
	* @Description: TODO(得到) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public boolean getAddData(){
		entity=new BloodItemEntity();
		if (bloodmethod1.isChecked()) {
			entity.fast_slow="1";
		}
		if (bloodmethod2.isChecked()) {
			entity.fast_slow="2";
		}
		if (bloodmethod3.isChecked()) {
			entity.fast_slow="3";
		}
		//entity.unit=bloodUnit.getSelectedItem().toString();
		entity.trans_capacity=bloodSum.getText().toString().trim();
		if ("".equals(entity.trans_capacity)) {
			Toast.makeText(this, "请填写血量!", Toast.LENGTH_SHORT).show();
			return false;
		}
		/*if (bloodUnit.getSelectedItemPosition()==0) {
			Toast.makeText(this, "请选择单位!", Toast.LENGTH_SHORT).show();
			return false;
		}		*/
		entity.trans_date=bloodDate.getText().toString()+" "+bloodTime.getText().toString();
		entity.blood_type_name=bloodxyyq.getSelectedItem().toString();
		if (bloodxyyq.getSelectedItemPosition()>0) {
			int location=bloodxyyq.getSelectedItemPosition();
			//主要是获得的position值，因为添加了一个“”值，所以位置需要-1
			entity.blood_type=bloodTypeList.get(location-1).blood_type;
		}	
		return true;
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
		case R.id.activity_add_blood_new_cancle:
			intent=new Intent();
			setResult(1, intent);
			finish();
			break;
		case R.id.activity_add_blood_new_ok:
			new AlertDialog.Builder(AddBloodNewActivity.this)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("是否确认提交？")
            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                	//把新增加的信息，放到adapter中，显示到用血申请的listview中
                	if(getAddData()){
                		new BloodItemTask().execute();
                	}       	
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked Cancel so do some stuff */
                }
            })
            .create().show();
			break;
		}	
	}
	
	/**
	 * 
	* @ClassName: BloodItemTask 
	* @Description: TODO(获得用血项目任务) 
	* @author lll 
	* @date 2013-04-02 
	*
	 */
	private class BloodItemTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
        	//fm.setListView(entity);
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Intent intent=new Intent();
			intent.putExtra("item", entity);//返回entity实体
			setResult(21, intent);//返回到AddBloodActivity中的onActivityResult（）函数
			finish();
		}
		
	}
	/**
	* @Title: showDatePickerDialog 
	* @Description: TODO(显示日期对话框) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void showDatePickerDialog(){
		final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
	   
        bloodDate.setText(new StringBuilder().append(mYear).append("-")
                // Month is 0 based so add 1
                .append(Util.toQueryTime(mMonth + 1)).append("-")
                .append(Util.toQueryTime(mDay)));
	    
        bloodDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(AddBloodNewActivity.this, mDateSetListener, mYear, mMonth, mDay).show();
			}
		});
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	bloodDate.setText(
                            new StringBuilder().append(year).append("-")
                                    // Month is 0 based so add 1
                                    .append(Util.toQueryTime(monthOfYear + 1)).append("-")
                                    .append(Util.toQueryTime(dayOfMonth))
                                    );
                }
            };
            
    /**
    * @Title: showTimePickerDialog 
    * @Description: TODO(显示时间对话框) 
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
   */
    public void showTimePickerDialog(){
    	final Calendar c = Calendar.getInstance();
    	final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);
        bloodTime.setText(new StringBuilder().append(pad(mHour)).append(":")   
                .append(pad(mMinute)));
        bloodTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new TimePickerDialog(AddBloodNewActivity.this, mTimeSetListener, mHour, mMinute,false).show();
			}
		});
    }
    private TimePickerDialog.OnTimeSetListener mTimeSetListener=
    		new TimePickerDialog.OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					bloodTime.setText(new StringBuilder().append(pad(hourOfDay)).append(":")   
			                .append(pad(minute)));
				}
			};
    private static String pad(int c) {   
        if (c >= 10)   
            return String.valueOf(c);   
        else  
            return "0" + String.valueOf(c);   
    }   

                    
}
