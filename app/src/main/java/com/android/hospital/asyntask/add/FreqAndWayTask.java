package com.android.hospital.asyntask.add;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.android.hospital.HospitalApp;
import com.android.hospital.asyntask.BaseAsyncTask;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.webservice.WebServiceHelper;
/**
 * 
* @ClassName: FreqAndWayTask 
* @Description: TODO(获取途径和频次任务) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-22 下午4:06:54 
*
 */
public class FreqAndWayTask extends BaseAsyncTask{
	
	private Context mContext;
	
	private String[] sqlArr;
	
	private List<Map<String, String>> wayList;
	
	private List<Map<String, String>> freqList;

	public FreqAndWayTask(Context context,String[] sqlArr){
		this.mContext=context;
		this.sqlArr=sqlArr;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		ArrayList<DataEntity> dataList=WebServiceHelper.getWebServiceData(sqlArr[0]);
		wayList=new ArrayList<Map<String,String>>();
		Map<String, String> mapNull=new HashMap<String, String>();
		mapNull.put("administration_name", "");
		wayList.add(mapNull);
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, String> map=new HashMap<String, String>();
			map.put("administration_name", dataList.get(i).get("administration_name"));
			wayList.add(map);
		}
		ArrayList<DataEntity> dataList2=WebServiceHelper.getWebServiceData(sqlArr[1]);
		freqList=new ArrayList<Map<String,String>>();
		Map<String, String> mapNull2=new HashMap<String, String>();
		mapNull2.put("freq_desc", "");
		mapNull2.put("freq_counter", "");
		mapNull2.put("freq_interval", "");
		mapNull2.put("freq_interval_unit", "");
		freqList.add(mapNull2);
		for (int i = 0; i < dataList2.size(); i++) {
			Map<String, String> map=new HashMap<String, String>();
			map.put("freq_desc", dataList2.get(i).get("freq_desc").trim());
			map.put("freq_counter", dataList2.get(i).get("freq_counter").trim());
			map.put("freq_interval", dataList2.get(i).get("freq_interval").trim());
			map.put("freq_interval_unit", dataList2.get(i).get("freq_interval_unit").trim());
			freqList.add(map);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		HospitalApp app=(HospitalApp) mContext.getApplicationContext();
		app.setFreqList(freqList);
		app.setWayList(wayList);
	}
}
