package com.android.hospital.adapter;

import java.util.ArrayList;

import com.android.hospital.entity.OperationItemEntity;
import com.android.hospital.entity.PatientEntity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
* @ClassName: OperationPatientAdapter 
* @Description: TODO(手术病人列表adapter) 
* @author lll
* @date 2013-02-26
*
 */
public class OperationPatientAdapter extends BaseAdapter{

	private ArrayList<PatientEntity> mList;
	
	private Context mContext;
	
	public OperationPatientAdapter(Context context,ArrayList<PatientEntity> arrayList){
		this.mList=arrayList;
		this.mContext=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList==null?0:mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	/**
	 * 
	* @Title: clearAdapter 
	* @Description: TODO(每次获取，清空之前) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void clearAdapter(){
		mList.clear();
	    notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.fragment_add_operation_patientitem, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.operation_patient_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.operation_patient_2);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		PatientEntity item=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));	
		viewHolder.tev2.setText(item.name);	
		return convertView;
	}

	
	private class ViewHolder{	
		public TextView tev1,tev2;
	  }
}
