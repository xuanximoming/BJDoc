package com.android.hospital.adapter;

import java.util.ArrayList;

import com.android.hospital.entity.SignsLifeEntity;
import com.android.hospital.ui.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SignsLifeDetailAdapter extends BaseAdapter {

	private Context  mContext;
	
	private ArrayList<SignsLifeEntity> mList;
	
	public SignsLifeDetailAdapter(Context context,ArrayList<SignsLifeEntity> list){
		this.mContext=context;
		this.mList=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList==null?0:mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList==null?0:mList.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.fragment_signslife_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_5);
			viewHolder.tev6=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_6);
			viewHolder.tev7=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_7);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		SignsLifeEntity item=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));
		viewHolder.tev2.setText(item.recording_date);
		viewHolder.tev3.setText(item.time_point);
		viewHolder.tev4.setText(item.vital_signs);
		viewHolder.tev5.setText(item.vital_signs_cvalues);
		viewHolder.tev6.setText(item.units);
		viewHolder.tev7.setText(item.nurse);
		return convertView;
	}

	
	private class ViewHolder{
		public TextView tev1,tev2,tev3,tev4,tev5,tev6,tev7;
	   }
}
