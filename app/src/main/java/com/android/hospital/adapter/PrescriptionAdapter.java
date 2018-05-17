package com.android.hospital.adapter;

import java.util.ArrayList;

import com.android.hospital.entity.PrescriptionEntity;
import com.android.hospital.ui.activity.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
* @ClassName: PrescriptionAdapter 
* @Description: TODO(处方Adatper) 
* @author lll 
* @date 2012-12-19  
*
 */
public class PrescriptionAdapter extends BaseAdapter{
	
	private ArrayList<PrescriptionEntity> mList;
	
	private Context mContext;
	
	public PrescriptionAdapter(Context context,ArrayList<PrescriptionEntity> entities){
		this.mContext=context;
		this.mList=entities;
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
		if (mList!=null) {
			mList.clear();
			notifyDataSetChanged();
		}	
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.fragment_prescription_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.prescription_list_item1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.prescription_list_item2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.prescription_list_item3);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		PrescriptionEntity item=mList.get(position);
		viewHolder.tev1.setText("处方号："+item.presc_no+"  申请日期："+item.presc_date
				                +"  开单医生："+item.prescribed_by);
		viewHolder.tev2.setText("处方类型:"+item.presc_type+"  剂数:"+item.repetition+"  总花费:"+item.costs);
		viewHolder.tev3.setText("状态："+item.presc_status+"  发药药房："+item.dept_name);
		return convertView;
	}
	private class ViewHolder{
		public TextView tev1,tev2,tev3;
	   }

}
