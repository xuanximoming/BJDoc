package com.android.hospital.ui.activity;

import java.util.ArrayList;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.BloodAdapter;
import com.android.hospital.adapter.OperationPatientAdapter;
import com.android.hospital.asyntask.add.MaxNumberTask;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.ui.fragment.AddCheckFragment;
import com.android.hospital.ui.fragment.AddInspectionFragment;
import com.android.hospital.ui.fragment.AddOperationFragment;
import com.android.hospital.ui.fragment.LeftListFragment;
import com.android.hospital.ui.fragment.SeachInspectionFragment;
import com.android.hospital.ui.fragment.SeachOperationFragment;
import com.android.hospital.ui.fragment.SearchFragment;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.webservice.WebServiceHelper;
import com.android.hospital.widgets.MyProssDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.Toast;
/**
 * 
* @ClassName: AddOperationActivity 
* @Description: TODO(新增手术显示界面) 
* @author lll 
* @date 2013-02-23  
*
 */
public class AddOperationActivity extends Activity implements OnClickListener{

	private AddOperationFragment leftFm;//手术左侧
	private SearchFragment searchFm; //检索
	private Button mCancleBut,mOkBut,mClearBut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_operation);
		initView();
		//加载fragment
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		if (leftFm==null) {
			leftFm=new AddOperationFragment();		
			ft.add(R.id.common_left_fragment, leftFm,"addfm");
		}
		if (searchFm==null) {
			searchFm=new SeachOperationFragment();
			ft.add(R.id.common_right_fragment, searchFm,"searchfm");
		}
		ft.commit();
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
		mCancleBut=(Button) findViewById(R.id.common_but_cancle);
		mOkBut=(Button) findViewById(R.id.common_but_ok);
		mClearBut=(Button) findViewById(R.id.common_but_clear);
		
		mCancleBut.setOnClickListener(this);
		mOkBut.setOnClickListener(this);
		mClearBut.setOnClickListener(this);
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
			new AlertDialog.Builder(AddOperationActivity.this)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("是否确认提交？")
            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                	//调用手术AddOperationFragment中的函数，判断必填项是否完成，完成则执行插入任务
                	if (leftFm.validate()) {
                		new InsertOperationTask().execute();
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

		case R.id.common_but_clear:
			//leftFm.clear();暂时不用
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
	* @ClassName: InsertOperationTask 
	* @Description: TODO(手术插入任务) 
	* @author lll 
	* @date 2013-02-24 
	*
	 */
	private class InsertOperationTask extends AsyncTask<Void, Void, String>{

        private MyProssDialog mDialog;
		
		@Override
		protected void onPreExecute() {
	        mDialog=new MyProssDialog(AddOperationActivity.this, "提交", "正在提交请求，请稍候...");
		}		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			leftFm.insert();//调用手术AddOperationFragment中的函数insert()
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mDialog.cancel();
			Intent intent=new Intent();
			setResult(11, intent); //返回MainActivity中的onActivityResult（）函数
			finish();
		}
	}
}
