package com.android.hospital.adapter;

import java.util.ArrayList;

import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.entity.InspectiondetailEntity;
import com.android.hospital.entity.PrescriptiondetailEntity;
import com.android.hospital.ui.activity.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PrescriptionDetailAdapter extends BaseAdapter{
	
	private Context mContext;
	
	private ArrayList<PrescriptiondetailEntity> mList;

	public PrescriptionDetailAdapter(Context context,ArrayList<PrescriptiondetailEntity> arrayList){
		
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.fragment_prescription_list_detail_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_5);
			viewHolder.tev6=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_6);
			viewHolder.tev7=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_7);
			viewHolder.tev8=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_8);
			viewHolder.tev9=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_9);
			viewHolder.tev10=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_10);
			viewHolder.tev11=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_11);
			viewHolder.tev12=(TextView) convertView.findViewById(R.id.prescription_detail_list_item_tev_12);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		PrescriptiondetailEntity item=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));
		viewHolder.tev2.setText(item.DRUG_NAME);
		viewHolder.tev3.setText(item.PACKAGE_SPEC);
		viewHolder.tev4.setText(item.FIRM_ID);
		viewHolder.tev5.setText(item.DOSAGE_EACH);
		viewHolder.tev6.setText(item.DOSAGE_UNITS);
		viewHolder.tev7.setText(item.ADMINISTRATION);
		viewHolder.tev8.setText(item.FREQUENCY);
		viewHolder.tev9.setText(item.FREQ_DETAIL);
		viewHolder.tev10.setText(item.QUANTITY);
		viewHolder.tev11.setText(item.PACKAGE_UNITS);
		viewHolder.tev12.setText(item.COSTS);
		
		return convertView;
	}

	private class ViewHolder{
		public TextView tev1,tev2,tev3,tev4,tev5,tev6,tev7,tev8,tev9,tev10,tev11,tev12;
		
    }
	
}
