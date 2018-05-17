package com.android.hospital.asyntask;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.PatientAdapter;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.ui.activity.MainActivity;
import com.android.hospital.ui.fragment.LeftListFragment;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.webservice.WebServiceHelper;

import java.util.ArrayList;

/**
 * 
* @ClassName: PatientTask 
* @Description: TODO(获取病人实体) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 上午11:06:44 
*
 */
public class PatientTask extends BaseAsyncTask{

	private MainActivity mActivity;
	
	private String sql;
	
	private ArrayList<PatientEntity> arrayList;
	
	private AsyncTaskCallback<PatientEntity> mTaskCallback;

	private LeftListFragment fm;
	
	private HospitalApp app;
	
	public PatientTask(Activity activity,String sql){
		this.mActivity=(MainActivity) activity;
		this.sql=sql;
		app=(HospitalApp) mActivity.getApplication();
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
		arrayList=PatientEntity.init(dataList);
		DebugUtil.debug("arraylist"+arrayList.size());
		return arrayList;
	}

	@Override
	protected void onPostExecute(Object result) {
		DebugUtil.debug("listsiize"+arrayList.size());
		//回调接口
		if (!(mActivity instanceof AsyncTaskCallback)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
		mTaskCallback=(AsyncTaskCallback<PatientEntity>) mActivity;
		fm=(LeftListFragment) mActivity.getFragmentManager().findFragmentByTag("leftfm");
		if (arrayList.size()!=0) {
			final PatientAdapter adapter=new PatientAdapter(mActivity, arrayList);
			fm.setListAdapter(adapter);
			fm.setListShown(true);
			fm.getListView().setItemChecked(0, true);//默认选中
			mActivity.setTitleTev(arrayList.get(0));
			mActivity.putDcAdviceTask(arrayList.get(0), "");
			mActivity.putCheckTask(arrayList.get(0), "");
			mActivity.putInspectionTask(arrayList.get(0), "");
			mActivity.putPrescriptionTask(arrayList.get(0), "");
			app.setPatientEntity(arrayList.get(0));
			fm.getListView().setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					PatientEntity patientEntity=(PatientEntity) adapter.getItem(position);
					app.setPatientEntity(patientEntity);
					mTaskCallback.getSingle(patientEntity);
				}
			});
		}else {
			fm.setListShown(true);
			fm.setEmptyText("未获取到数据");
		}
	}
}
