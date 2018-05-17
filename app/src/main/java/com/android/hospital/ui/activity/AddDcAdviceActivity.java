package com.android.hospital.ui.activity;

import java.util.ArrayList;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.DrugAdapter;
import com.android.hospital.asyntask.add.DrugOrNonDrugTask;
import com.android.hospital.asyntask.add.FreqAndWayTask;
import com.android.hospital.asyntask.add.MaxNumberTask;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.ui.fragment.AddDcAdviceFragment;
import com.android.hospital.ui.fragment.LeftListFragment;
import com.android.hospital.ui.fragment.SearchAddDcAdviceFragment;
import com.android.hospital.ui.fragment.SearchFragment;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.webservice.WebServiceHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
 * @ClassName: AddDcAdviceActivity
 * @Description: TODO(添加医嘱显示界面)
 * @author Ukey zhang yeshentianyue@sina.com
 * @date 2012-12-14 上午11:37:25
 *
 */
public class AddDcAdviceActivity extends Activity implements OnClickListener{

    private AddDcAdviceFragment leftFm;
    private SearchAddDcAdviceFragment searchFm;	//检索
    private Button mCancleBut,mOkBut;
    private HospitalApp mApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dcadvice);
        mApp=(HospitalApp) getApplication();
        initView();

        //加载fragment
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        if (leftFm==null) {
            leftFm=new AddDcAdviceFragment();
            ft.add(R.id.common_left_fragment, leftFm,"addfm");
        }
        if (searchFm==null) {
            searchFm=new SearchAddDcAdviceFragment();
            ft.add(R.id.common_right_fragment, searchFm,"searchfm");
        }
        ft.commit();
        new MaxNumberTask(this).execute("");//获取最大序号任务
    }

    private void initView(){
        mCancleBut=(Button) findViewById(R.id.common_but_cancle);
        mOkBut=(Button) findViewById(R.id.common_but_ok);
        findViewById(R.id.common_but_clear).setVisibility(View.GONE);//清空按钮不用，隐藏

        mCancleBut.setOnClickListener(this);
        mOkBut.setOnClickListener(this);
    }

    /**
     *
     * @Title: onClick
     * @Description: TODO(button按钮点击事件)
     * @param
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

                if (leftFm.validate()) {
                    final String orderCode=leftFm.getOrderCode();
                    new AsyncTask<Void, Void, String>(){

                        private ProgressDialog mDialog;


                        protected void onPreExecute() {
                            mDialog=new ProgressDialog(AddDcAdviceActivity.this);
                            mDialog.setMessage("正在请求...");
                            mDialog.show();
                        };
                        @Override
                        protected String doInBackground(Void... params) {
                            // TODO Auto-generated method stub
                            String title = mApp.getUserEntity().title.toString();
                            String sqlkss="select limit_class from drug_dict where drug_code = '"+orderCode+"'";
                            String resString="";
                            ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sqlkss);

                            if(dataList.size()==0){
                                return "0";
                            }
                            else{
                                resString=dataList.get(0).get("limit_class");
                            }

                            if (resString.equals("0")){
                                return "0";
                            }

                            if(title.equals("主任医师")||title.equals("副主任医师"))
                            {
                                return "0";
                            }
                            if(title.equals("主治医师")){
                                if(resString.equals("2")||resString.equals("1")){
                                    return "0";
                                }
                            }
                            if(title.equals("医师"))
                            {
                                if(resString.equals("1")){
                                    return "0";
                                }
                            }
                            Log.d("抗生素获得的等级值----->", resString);
                            Log.d("医生级别----->", title);
                            return "1";
                        }

                        protected void onPostExecute(String result) {
                            mDialog.cancel();
                            Log.d("这里的是多少---》", result);
                            if (result.equals("1")) {//
                                Toast.makeText(AddDcAdviceActivity.this, "用药级别不够！ 保存失败", Toast.LENGTH_SHORT).show();
                            }else{
                                new AlertDialog.Builder(AddDcAdviceActivity.this)
                                        .setIconAttribute(android.R.attr.alertDialogIcon)
                                        .setTitle("是否确认提交？")
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

			                        /* User clicked OK so do some stuff */
                                                leftFm.getAddData();//获取基本数据
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

			                        /* User clicked Cancel so do some stuff */
                                            }
                                        })
                                        .create().show();
                            }
                        };
                    }.execute();
                }else {
                    Toast.makeText(AddDcAdviceActivity.this, "医嘱或频次，途径不能为空!", Toast.LENGTH_SHORT).show();
                }
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
}
