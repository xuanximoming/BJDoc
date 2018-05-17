package com.android.hospital.ui.activity;

import java.util.ArrayList;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.BloodAdapter;
import com.android.hospital.adapter.OperationPatientAdapter;
import com.android.hospital.asyntask.add.MaxNumberTask;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.BloodItemEntity;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.ui.fragment.AddBloodFragment;
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
* @ClassName: AddBloodActivity 
* @Description: TODO(新增用血申请显示界面) 
* @author lll 
* @date 2013-03-27  
*
 */
public class AddBloodActivity extends Activity implements OnClickListener{

	private AddBloodFragment addmainfragment;	
	private Button mCancleBut,mOkBut,mAddBut,mDeleteBut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_blood_main);
		initView();
		//加载fragment
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		if (addmainfragment==null) {
			addmainfragment=new AddBloodFragment();		
			ft.add(R.id.common_blood_main_fragment, addmainfragment,"addfm");
		}
		ft.commit();
		new MaxNumberTask(this).execute("apply_num.nextval");//获取最大序号任务
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
		mAddBut=(Button) findViewById(R.id.common_but_add);
		mDeleteBut=(Button) findViewById(R.id.common_but_delete);
		
		mCancleBut.setOnClickListener(this);
		mOkBut.setOnClickListener(this);
		mAddBut.setOnClickListener(this);
		mDeleteBut.setOnClickListener(this);
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
			new AlertDialog.Builder(AddBloodActivity.this)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("是否确认提交？")
            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                	if (addmainfragment.validate()) {
                		new InsertBloodTask().execute();
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
		case R.id.common_but_add:
			//新增，弹出新的界面
			intent =new Intent();
			intent.setClass(this, AddBloodNewActivity.class);
			startActivityForResult(intent, 21);
			break;
		case R.id.common_but_delete:
			//清空list中的信息
			addmainfragment.clear();
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
	* @Title: onActivityResult 
	* @Description: TODO(获得AddBloodNewActivity界面的返回值) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case 21:
			BloodItemEntity itemEntity=(BloodItemEntity) data.getSerializableExtra("item");
			if ("1".equals(itemEntity.fast_slow)) {
				itemEntity.fast_slow="急诊";
			}
			if ("2".equals(itemEntity.fast_slow)) {
				itemEntity.fast_slow="计划";
			}
			if ("3".equals(itemEntity.fast_slow)) {
				itemEntity.fast_slow="备血";
			}
			addmainfragment.setListView(itemEntity);//将新增的用血项目显示到listview中
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	* @ClassName: InsertBloodTask 
	* @Description: TODO(用血插入任务) 
	* @author lll 
	* @date 2013-02-24 
	*
	 */
	private class InsertBloodTask extends AsyncTask<Void, Void, String>{

        private MyProssDialog mDialog;
		
		@Override
		protected void onPreExecute() {
	        mDialog=new MyProssDialog(AddBloodActivity.this, "提交", "正在提交请求，请稍候...");
		}		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			addmainfragment.insert();//可加个boolean值，做判断
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mDialog.cancel();
			Intent intent=new Intent();
			setResult(11, intent);
			finish();
		}
	}
}
