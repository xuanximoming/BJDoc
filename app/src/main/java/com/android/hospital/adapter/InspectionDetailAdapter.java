package com.android.hospital.adapter;

import java.util.ArrayList;

import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.entity.InspectiondetailEntity;
import com.android.hospital.ui.activity.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InspectionDetailAdapter extends BaseAdapter{
	
	private Context mContext;
	
	private ArrayList<InspectiondetailEntity> mList;

	public InspectionDetailAdapter(Context context,ArrayList<InspectiondetailEntity> arrayList){
		this.mContext=context;
		this.mList=arrayList;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.fragment_inspection_list_detail_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.inspection_detail_list_item_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.inspection_detail_list_item_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.inspection_detail_list_item_tev_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.inspection_detail_list_item_tev_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.inspection_detail_list_item_tev_5);
			viewHolder.tev6=(TextView) convertView.findViewById(R.id.inspection_detail_list_item_tev_6);
			viewHolder.tev7=(TextView) convertView.findViewById(R.id.inspection_detail_list_item_tev_7);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		InspectiondetailEntity item=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));
		viewHolder.tev2.setText(item.result_date_time);
		viewHolder.tev3.setText(item.report_item_name);
		viewHolder.tev4.setText(item.result);
		viewHolder.tev5.setText(item.units);
		viewHolder.tev6.setText(item.print_context);
		viewHolder.tev7.setText(item.abnormal_indicator);
		
        if (!item.abnormal_indicator.trim().equals("")) {
        	viewHolder.tev1.setTextColor(Color.RED);
        	viewHolder.tev2.setTextColor(Color.RED);
        	viewHolder.tev3.setTextColor(Color.RED);
        	viewHolder.tev4.setTextColor(Color.RED);
        	viewHolder.tev5.setTextColor(Color.RED);
        	viewHolder.tev6.setTextColor(Color.RED);
        	viewHolder.tev7.setTextColor(Color.RED);
		}else {
        	viewHolder.tev1.setTextColor(mContext.getResources().getColor(R.color.cornflowerblue));
        	viewHolder.tev2.setTextColor(mContext.getResources().getColor(R.color.cornflowerblue));
        	viewHolder.tev3.setTextColor(mContext.getResources().getColor(R.color.cornflowerblue));
        	viewHolder.tev4.setTextColor(mContext.getResources().getColor(R.color.cornflowerblue));
        	viewHolder.tev5.setTextColor(mContext.getResources().getColor(R.color.cornflowerblue));
        	viewHolder.tev6.setTextColor(mContext.getResources().getColor(R.color.cornflowerblue));
        	viewHolder.tev7.setTextColor(mContext.getResources().getColor(R.color.cornflowerblue));
		}
        
		return convertView;
	}

	private class ViewHolder{
		public TextView tev1,tev2,tev3,tev4,tev5,tev6,tev7,tev8,tev9,tev10,tev11,tev12;
		public View tevDivider;
    }
	
}
