package com.android.hospital.adapter;

import java.util.List;
import java.util.Map;

import com.android.hospital.entity.DrugEntity;
import com.android.hospital.util.DebugUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * 
* @ClassName: FreqSpinnerAdapter 
* @Description: TODO(频次下拉列表adapter) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-22 下午4:02:35 
*
 */
public class FreqSpinnerAdapter extends ArrayAdapter<Map<String, String>> {

	private Context mContext;
	
	private List<Map<String, String>> mList;
	
	private int mFieldId=android.R.layout.simple_spinner_item;
	
	private LayoutInflater mInflater;

	public FreqSpinnerAdapter(Context context,int textViewResourceId,List<Map<String, String>> aList){
		super(context, textViewResourceId);
		this.mContext=context;
		this.mList=aList;
		this.mInflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList==null?0:mList.size();
	}
	
	@Override
	public Map<String, String> getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView==null) {
			convertView=mInflater.inflate(mFieldId, null);
		}
		Map<String, String> item=mList.get(position);
		TextView textView=(TextView) convertView.findViewById(android.R.id.text1);
		textView.setText(item.get("freq_desc"));
		return convertView;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView=mInflater.inflate(android.R.layout.simple_dropdown_item_1line, null);	
		Map<String, String> item=mList.get(position);
		DebugUtil.debug("test---getDropDownView");
		TextView textView=(TextView) convertView.findViewById(android.R.id.text1);
		textView.setText(item.get("freq_desc"));
		return convertView;
	}
	
}
