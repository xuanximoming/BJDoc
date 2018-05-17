package com.android.hospital.asyntask;

import java.util.ArrayList;

import com.android.hospital.adapter.InspectionAdapter;
import com.android.hospital.adapter.PrescriptionAdapter;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.InspectionEntity;
import com.android.hospital.entity.PrescriptionEntity;
import com.android.hospital.ui.fragment.InspectionFragment;
import com.android.hospital.ui.fragment.PrescriptionFragment;
import com.android.hospital.webservice.WebServiceHelper;

/**
 * 
* @ClassName: PrescriptionTask 
* @Description: TODO(获取处方实体) 
* @author lll 
* @date 2012-12-19 
*
 */
public class PrescriptionTask extends  BaseAsyncTask{
	
	private PrescriptionFragment fragment;
	
	private String sql;
	
	private ArrayList<PrescriptionEntity> arrayList;
	
	public PrescriptionTask(PrescriptionFragment fragment,String sql){
		this.fragment=fragment;
		this.sql=sql;
	}
	@Override
	protected Object doInBackground(Object... params) {
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
		arrayList=PrescriptionEntity.init(dataList);
		return arrayList;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if (arrayList.size()!=0) {
			PrescriptionAdapter adapter=new PrescriptionAdapter(fragment.getActivity(), arrayList);
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
