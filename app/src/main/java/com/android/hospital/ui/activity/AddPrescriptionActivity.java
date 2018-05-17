package com.android.hospital.ui.activity;

import com.android.hospital.HospitalApp;
import com.android.hospital.asyntask.add.MaxNumberTask;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.ui.fragment.AddCheckFragment;
import com.android.hospital.ui.fragment.AddPrescriptionFragment;
import com.android.hospital.ui.fragment.LeftListFragment;
import com.android.hospital.ui.fragment.SeachPrescriptionFragment;
import com.android.hospital.ui.fragment.SearchFragment;
import com.android.hospital.util.DebugUtil;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 
* @ClassName: AddDcAdviceActivity 
* @Description: TODO(新增处方显示界面) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:37:25 
*
 */
public class AddPrescriptionActivity extends Activity implements OnClickListener{

	private AddPrescriptionFragment leftFm; //左侧界面
	private SearchFragment searchFm;//检索fragmnet
	private Button mCancleBut,mOkBut,mClearBut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_prescription);
		initView();
		
		//加载左侧界面和检索界面
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		if (leftFm==null) {
			leftFm=new AddPrescriptionFragment();		
			ft.add(R.id.common_left_fragment, leftFm,"addfm");
		}
		if (searchFm==null) {
			searchFm=new SeachPrescriptionFragment();
			ft.add(R.id.common_right_fragment, searchFm,"searchfm");
		}
		ft.commit();
		new MaxNumberTask(this).execute("presc_no_seq.nextval");//获取最大序号任务
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
			new AlertDialog.Builder(AddPrescriptionActivity.this)
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setTitle("是否确认提交？")
            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                	//调用处方AddPrescriptionFragment中的函数，判断必填项是否完成，完成则执行插入任务
                	if (leftFm.validate()) {
                		new InsertPrescriptionTask().execute();
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
			leftFm.clear(); //调用处方AddPrescriptionFragment中的函数，清空列表
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	* @ClassName: InsertPrescriptionTask 
	* @Description: TODO(处方插入任务 ) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2012-12-27 下午4:36:47 
	*
	 */
	private class InsertPrescriptionTask extends AsyncTask<Void, Void, String>{

        private MyProssDialog mDialog;
		
		@Override
		protected void onPreExecute() {
	        mDialog=new MyProssDialog(AddPrescriptionActivity.this, "提交", "正在提交请求，请稍候...");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			leftFm.insert();//调用处方AddPrescriptionFragment中的函数insert()
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mDialog.cancel();
			Intent intent=new Intent();
			setResult(14, intent); //返回MainActivity中的onActivityResult（）函数
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
}
