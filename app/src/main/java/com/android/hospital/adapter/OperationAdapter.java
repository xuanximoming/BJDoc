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

import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.entity.OperationEntity;
import com.android.hospital.ui.activity.R;
/**
 * 
* @ClassName: OperationAdapter 
* @Description: TODO(手术Adatper) 
* @author lll 
* @date 2013-01-15 
*
 */
public class OperationAdapter extends BaseAdapter{
	
	private ArrayList<OperationEntity> mList;
	
	private Context mContext;
	
	public OperationAdapter(Context context,ArrayList<OperationEntity> entities){
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
	* @Description: TODO(得到所有的医嘱集合) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public ArrayList<OperationEntity> getList(){
		return mList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.activity_operation_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.operation_item_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.operation_item_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.operation_item_tev_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.operation_item_tev_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.operation_item_tev_5);
			viewHolder.tev6=(TextView) convertView.findViewById(R.id.operation_item_tev_6);
			viewHolder.tev7=(TextView) convertView.findViewById(R.id.operation_item_tev_7);
			viewHolder.tev8=(TextView) convertView.findViewById(R.id.operation_item_tev_8);
			viewHolder.tev9=(TextView) convertView.findViewById(R.id.operation_item_tev_9);
			viewHolder.tev10=(TextView) convertView.findViewById(R.id.operation_item_tev_10);
			viewHolder.tev11=(TextView) convertView.findViewById(R.id.operation_item_tev_11);
			viewHolder.tev12=(TextView) convertView.findViewById(R.id.operation_item_tev_12);
			viewHolder.tev13=(TextView) convertView.findViewById(R.id.operation_item_tev_13);
			viewHolder.tev14=(TextView) convertView.findViewById(R.id.operation_item_tev_14);
			viewHolder.tev15=(TextView) convertView.findViewById(R.id.operation_item_tev_15);
			viewHolder.tev16=(TextView) convertView.findViewById(R.id.operation_item_tev_16);
			viewHolder.tevDivider=(View)convertView.findViewById(R.id.listview_divider);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		OperationEntity item=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));
		viewHolder.tev2.setText(item.scheduled_date_time);
		viewHolder.tev3.setText(item.operating_room);
		viewHolder.tev4.setText(item.operating_room_no);
		viewHolder.tev5.setText(item.sequence);
		viewHolder.tev6.setText(item.name);
		viewHolder.tev7.setText(item.sex);
		viewHolder.tev8.setText(item.bed_no);
		viewHolder.tev9.setText(item.diag_before_operation);
		viewHolder.tev10.setText(item.operation);
		viewHolder.tev11.setText(item.surgeon);
		viewHolder.tev12.setText(item.first_assistant+" "+item.second_assistant+" "+item.third_assistant+" "+item.fourth_assistant);
		viewHolder.tev13.setText(item.anesthesia_method);
		viewHolder.tev14.setText(item.anesthesia_doctor);
		viewHolder.tev15.setText(item.blood_tran_doctor);
		viewHolder.tev16.setText(item.notes_on_operation);
		
		//根据执行状态，设置不同颜色    未确认： 0或空      确认：1
		if ("0".equals(item.ack_indicator)) {
			viewHolder.tev10.setTextColor( Color.RED);
		}
		if ("".equals(item.ack_indicator)) {
			viewHolder.tev10.setTextColor( Color.RED);
		}
		if ("1".equals(item.ack_indicator)) {
			viewHolder.tev10.setTextColor( Color.BLUE);
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
	   viewHolder.tev14.setTextColor(color);
	   viewHolder.tev15.setTextColor(color);
	   viewHolder.tev16.setTextColor(color);
	
   }
	
   private class ViewHolder{
	public TextView tev1,tev2,tev3,tev4,tev5,tev6,tev7,tev8,tev9,tev10,tev11,tev12,tev13,tev14,tev15,tev16;
	public View tevDivider;
   }
}
