package com.android.hospital.ui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.DiagnosisAdapter;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.DiagnosisEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
* @ClassName: PatientInforActivity 
* @Description: TODO(病人属性显示界面) 
* @author lll
* @date 2014-01-16  
*
 */
public class PatientInforActivity extends Activity implements OnClickListener{
	
	private Button mCancleBut;
    private ListView mListView;
    private TextView tev1,tev2,tev3,tev4,tev5,tev6;
    private HospitalApp app;
    private ArrayList<DiagnosisEntity> arrayList;
    private DiagnosisAdapter adapter;
    private PatientEntity patEntity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_information);
		app=(HospitalApp) getApplication();
		patEntity=app.getPatientEntity();
		initView();
		new PatientDiagnosisTask().execute();
		setDate();
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
		tev1=(TextView) findViewById(R.id.patient_infor_tev_1);
		tev2=(TextView) findViewById(R.id.patient_infor_tev_2);
		tev3=(TextView) findViewById(R.id.patient_infor_tev_3);
		tev4=(TextView) findViewById(R.id.patient_infor_tev_4);
		tev5=(TextView) findViewById(R.id.patient_infor_tev_5);
		tev6=(TextView) findViewById(R.id.patient_infor_tev_6);
		mListView=(ListView) findViewById(R.id.patient_infor_listview);
		mCancleBut=(Button) findViewById(R.id.common_but_cancle);
		mCancleBut.setOnClickListener(this);	
	}	
	
	/**
	 * 
	* @Title: setDate 
	* @Description: TODO(初始化数据显示) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void setDate(){
		String age=String.valueOf(Util.userBirthdayGetAge(patEntity.date_of_birth));
		String birthday=String.valueOf(Util.toDateOfBirth(patEntity.date_of_birth));
		tev1.setText("病人ID："+patEntity.patient_id+"       住院号："+patEntity.inp_no+ "      姓名："+patEntity.name );
		tev2.setText("性别："+patEntity.sex+"     出生："+birthday+"       年龄: "+age+"     费别: " +patEntity.charge_type);
		tev3.setText("身份："+patEntity.identity+"       民族："+patEntity.nation+"       出生地："+patEntity.birth_place);
		tev4.setText("证件号："+patEntity.id_no+"     通讯地址："+patEntity.mailing_address);
		tev5.setText("邮编: "+patEntity.zip_code +"       电话："+patEntity.phone_number_home);
		tev6.setText("入院时间："+patEntity.admission_date_time+"     入院诊断："+patEntity.diagnosis+"     主治医生："+patEntity.doctor_in_charge);
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
	* @Description: TODO(得到获取诊断的sql语句) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public  String getsql(){
		String sqlString="select b.admission_date_time,a.diagnosis_type,c.diagnosis_type_name,"+
                         " a.diagnosis_no, a.diagnosis_desc  "+     
                         " from diagnosis a, pat_visit b,diagnosis_type_dict c "+
                         " where a.patient_id = b.patient_id "+
                         "  and a.visit_id = b.visit_id"+
                         "  and a.diagnosis_type=c.diagnosis_type_code"+
                         "  and a.PATIENT_ID =  '"+app.getPatientEntity().patient_id+"'"+
                         "  order by  ADMISSION_DATE_TIME";
		Log.e("诊断查询时的语句---->", sqlString);
		return sqlString;	
	}
			
	/**
	 * 
	* @ClassName: PatientDiagnosisTask 
	* @Description: TODO(诊断查询数据任务) 
	* @author lll 
	* @date 2014-01-17 
	*
	 */
	private class PatientDiagnosisTask extends  AsyncTask<Void, Void, Object>{

		@Override
		protected Object doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(getsql());
			arrayList=DiagnosisEntity.init(dataList);
			return arrayList;
		}
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub		
			if (arrayList.size()!=0) {
				adapter=new DiagnosisAdapter(PatientInforActivity.this, arrayList);
				mListView.setAdapter(adapter);
			}else{
				Toast.makeText(PatientInforActivity.this, "没有数据!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	

}
