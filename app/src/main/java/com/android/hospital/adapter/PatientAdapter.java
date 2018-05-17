package com.android.hospital.adapter;

import java.util.ArrayList;

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
* @ClassName: PatientAdapter 
* @Description: TODO(病人列表adapter) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-19 上午12:13:49 
*
 */
public class PatientAdapter extends BaseAdapter{

	private ArrayList<PatientEntity> mList;
	
	private Context mContext;
	
	public PatientAdapter(Context context,ArrayList<PatientEntity> arrayList){
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.fragment_left_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.img=(ImageView) convertView.findViewById(R.id.patient_title_img);
			viewHolder.name=(TextView) convertView.findViewById(R.id.patient_name);
			viewHolder.bed_no=(TextView) convertView.findViewById(R.id.patient_bedno);
			viewHolder.doctor_in_charge=(TextView) convertView.findViewById(R.id.patient_doctor);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		PatientEntity item=mList.get(position);
		viewHolder.name.setText("姓名："+item.name);
		viewHolder.bed_no.setText("床号："+item.bed_no);
		if (item.sex.equals("男")) {
			viewHolder.img.setBackgroundResource(R.drawable.photo_man);
		}else {
			viewHolder.img.setBackgroundResource(R.drawable.photo_woman);
		}
		viewHolder.doctor_in_charge.setText("主治医生："+item.doctor_in_charge);
		return convertView;
	}

	
	private class ViewHolder{
		public ImageView img;
		public TextView name;
		public TextView bed_no;
		public TextView doctor_in_charge;
	  }
}
