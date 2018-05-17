package com.android.hospital.asyntask;

import com.android.hospital.adapter.CheckAdapter;
import com.android.hospital.entity.CheckEntity;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.ui.fragment.CheckFragment;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.webservice.WebServiceHelper;

import java.util.ArrayList;

/**
 * 
* @ClassName: CheckTask 
* @Description: TODO(检查任务) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 下午8:16:13 
*
 */
public class CheckTask extends BaseAsyncTask{

	private CheckFragment mFragment;
	
	private String sql;
	
	private ArrayList<CheckEntity> arrayList;
	
	public CheckTask(CheckFragment fragment,String sql){
		this.mFragment=fragment;
		this.sql=sql;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sql);
		arrayList=CheckEntity.init(dataList);
		return arrayList;
	}

	@Override
	protected void onPostExecute(Object result) {
        
		if (arrayList.size()!=0) {
			DebugUtil.debug("listsize==>"+arrayList.size());
			CheckAdapter adapter=new CheckAdapter(mFragment.getActivity(), arrayList);
			mFragment.setListAdapter(adapter);			
			if (mFragment.isAdded()) {
				mFragment.setSelection(adapter.getCount()-1);
				mFragment.setListShown(true);
			}
		}else {
			if (mFragment.isAdded()) {
				mFragment.setListShown(true);
				mFragment.setEmptyText("未获取到数据!");
			}
            
		}
	}
}
