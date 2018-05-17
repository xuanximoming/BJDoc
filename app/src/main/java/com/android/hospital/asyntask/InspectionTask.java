package com.android.hospital.asyntask;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.DcAdviceAdapter;
import com.android.hospital.adapter.InspectionAdapter;
import com.android.hospital.adapter.PatientAdapter;
import com.android.hospital.asyntask.BaseAsyncTask.AsyncTaskCallback;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.InspectionEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.ui.fragment.InspectionFragment;
import com.android.hospital.ui.fragment.LeftListFragment;
import com.android.hospital.webservice.WebServiceHelper;

/**
 * 
* @ClassName: InspectionTask 
* @Description: TODO(获取检查实体) 
* @author lll 
* @date 2012-12-18 
*
 */
public class InspectionTask extends BaseAsyncTask{

	private InspectionFragment fragment;
	
	private String sql;
	
	private ArrayList<InspectionEntity> arrayList;
	
	public InspectionTask(InspectionFragment fragment,String sql){
		this.fragment=fragment;
		this.sql=sql;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
		arrayList=InspectionEntity.init(dataList);
		return arrayList;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if (arrayList.size()!=0) {
			InspectionAdapter adapter=new InspectionAdapter(fragment.getActivity(), arrayList);
			fragment.setListAdapter(adapter);
			if (fragment.isAdded()) {
				fragment.setListShown(true);
				fragment.setSelection(adapter.getCount()-1);			
			}
		}else {
			if (fragment.isAdded()) {
				fragment.setListShown(true);
				fragment.setEmptyText("未获取到数据!");
			}
		}
	}

}
