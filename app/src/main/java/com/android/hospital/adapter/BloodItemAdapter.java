package com.android.hospital.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.hospital.entity.BloodItemEntity;
import com.android.hospital.entity.DrugEntity;
import com.android.hospital.entity.OperationItemEntity;
import com.android.hospital.ui.activity.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * BloodItemAdapter
 * @author Administrator
 * 新增用血申请项目
 */
public class BloodItemAdapter extends BaseAdapter{

	private Context mContext;
	private List<BloodItemEntity> mList;
	private LayoutInflater mInflater;
	
	public BloodItemAdapter(Context context,List<BloodItemEntity> list){
		this.mContext=context;
		this.mList=list;
		this.mInflater=LayoutInflater.from(context);
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

	public void clear(){
		if (mList!=null) {
			mList.clear();
			notifyDataSetChanged();
		}
    }		
	
	public void addItem(BloodItemEntity entity){
		if (mList==null) {
			mList=new ArrayList<BloodItemEntity>();			
		}
		mList.add(entity);
		notifyDataSetChanged();
	}
		
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.fragment_add_blood_listview_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.add_blood_listview_item_tv1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.add_blood_listview_item_tv2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.add_blood_listview_item_tv3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.add_blood_listview_item_tv4);
			//viewHolder.tev5=(TextView) convertView.findViewById(R.id.add_blood_listview_item_tv5);
			viewHolder.tev6=(TextView) convertView.findViewById(R.id.add_blood_listview_item_tv6);
			//viewHolder.but1=(Button) convertView.findViewById(R.id.operation_search_item_but_delete);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		final BloodItemEntity item=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));
		viewHolder.tev2.setText(item.fast_slow);
		viewHolder.tev3.setText(item.trans_date);
		viewHolder.tev4.setText(item.trans_capacity);
		//viewHolder.tev5.setText(item.unit);
		viewHolder.tev6.setText(item.blood_type_name);
//		viewHolder.but1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mList.remove(item);
//				notifyDataSetChanged();
//			}
//		});
		return convertView;
	}
	
	private class ViewHolder{
		public TextView tev1,tev2,tev3,tev4,tev5,tev6;
		//public Button but1;
	}
}
