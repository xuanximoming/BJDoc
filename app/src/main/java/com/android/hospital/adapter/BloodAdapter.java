package com.android.hospital.adapter;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.hospital.entity.BloodEntity;
import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.entity.OperationEntity;
import com.android.hospital.ui.activity.R;
/**
 * 
* @ClassName: BloodAdapter 
* @Description: TODO(用血Adatper) 
* @author lll 
* @date 2013-02-17 
*
 */
public class BloodAdapter extends BaseAdapter{
	
	private ArrayList<BloodEntity> mList;
	
	private Context mContext;
	
	public BloodAdapter(Context context,ArrayList<BloodEntity> entities){
		this.mList=entities;
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
	/**
	 * 
	* @Title: getList 
	* @Description: TODO(得到所有的备血集合) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public ArrayList<BloodEntity> getList(){
		return mList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.activity_blood_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.blood_item_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.blood_item_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.blood_item_tev_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.blood_item_tev_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.blood_item_tev_5);
			viewHolder.tev6=(TextView) convertView.findViewById(R.id.blood_item_tev_6);
			viewHolder.tev7=(TextView) convertView.findViewById(R.id.blood_item_tev_7);
			viewHolder.tev8=(TextView) convertView.findViewById(R.id.blood_item_tev_8);
			viewHolder.tev9=(TextView) convertView.findViewById(R.id.blood_item_tev_9);
			viewHolder.tev10=(TextView) convertView.findViewById(R.id.blood_item_tev_10);
			viewHolder.tev11=(TextView) convertView.findViewById(R.id.blood_item_tev_11);
			viewHolder.tev12=(TextView) convertView.findViewById(R.id.blood_item_tev_12);
			viewHolder.tev13=(TextView) convertView.findViewById(R.id.blood_item_tev_13);
			viewHolder.tevDivider=(View)convertView.findViewById(R.id.listview_divider);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		BloodEntity item=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));
		viewHolder.tev2.setText(item.dept_name);
		viewHolder.tev3.setText(item.pat_name);
		viewHolder.tev4.setText(item.patient_id);
		viewHolder.tev5.setText(item.apply_num);
		viewHolder.tev6.setText(item.blood_inuse);
		viewHolder.tev7.setText(item.blood_diagnose);
		viewHolder.tev8.setText(item.pat_blood_group);
		viewHolder.tev9.setText(item.rh);
		viewHolder.tev10.setText(item.fast_slow);
		viewHolder.tev11.setText(item.trans_date);
		viewHolder.tev12.setText(item.trans_capacity);
		viewHolder.tev13.setText(item.blood_type_name);
		if (position>0) {
			BloodEntity last_item=mList.get(position-1);//获得上一条信息
			if(null!=last_item){
				if (last_item.dept_name.equals(item.dept_name)) {
					viewHolder.tev2.setText("");
				}
				if (last_item.pat_name.equals(item.pat_name)) {
					viewHolder.tev3.setText("");
					viewHolder.tev4.setText("");
				}
			}
		}
		//根据执行状态，设置不同颜色
		if("AB".equals(item.pat_blood_group)){ //血型AB黑色 A黄色 B蓝色 O红色
			viewHolder.tev8.setTextColor( Color.BLACK);		
		}
		if ("A".equals(item.pat_blood_group)) {
			viewHolder.tev8.setTextColor( Color.YELLOW);
		}
		if ("B".equals(item.pat_blood_group)) {
			viewHolder.tev8.setTextColor( Color.BLUE);
		}
		if ("O".equals(item.pat_blood_group)) {
			viewHolder.tev8.setTextColor( Color.RED);
		}
		
		return convertView;
	}
	
//根据不同的状态，改变医嘱的显示颜色
   public void setTextColor(ViewHolder viewHolder,int color){
	   viewHolder.tev1.setTextColor(color);
   	   viewHolder.tev2.setTextColor(color);
   	   viewHolder.tev3.setTextColor(color);
   	   viewHolder.tev4.setTextColor(color);
   	   viewHolder.tev5.setTextColor(color);
   	   viewHolder.tev6.setTextColor(color);
   	   viewHolder.tev7.setTextColor(color);
   	   viewHolder.tev8.setTextColor(color);
   	   viewHolder.tev9.setTextColor(color);
   	   viewHolder.tev10.setTextColor(color);
   	   viewHolder.tev11.setTextColor(color);
   	   viewHolder.tev12.setTextColor(color);
   	   viewHolder.tev13.setTextColor(color);
	
   }
	
   private class ViewHolder{
	public TextView tev1,tev2,tev3,tev4,tev5,tev6,tev7,tev8,tev9,tev10,tev11,tev12,tev13;
	public View tevDivider;
   }
}
