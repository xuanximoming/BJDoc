package com.android.hospital.adapter;

import java.util.ArrayList;

import com.android.hospital.entity.DoctorEntity;
import com.android.hospital.ui.activity.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


public class DoctorAdapter extends ArrayAdapter<DoctorEntity> implements Filterable{

    private Context mContext;
	
	private ArrayList<DoctorEntity> mList;
	
	private LayoutInflater mInflater;
	
	private int resourceId;
	
	private ArrayFilter mFilter;
	
	private ArrayList<DoctorEntity> mOriginalValues;
	
    private final Object mLock = new Object();
	
	public DoctorAdapter(Context context,int textViewResourceId,ArrayList<DoctorEntity> arrayList){
		super(context, textViewResourceId);
		this.mContext=context;
		this.mInflater=LayoutInflater.from(context);
		this.mList=arrayList;
		this.resourceId=textViewResourceId;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList==null?0:mList.size();
	}
	@Override
	public DoctorEntity getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.doctor_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.textView1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.textView2);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		DoctorEntity item=mList.get(position);
		viewHolder.tev1.setText(item.name);
		viewHolder.tev2.setText(item.input_code);
		return convertView;
	}
	
	
	@Override
	public Filter getFilter() {
		if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
	}
	
	private class ViewHolder{
		TextView tev1,tev2;
	}
	
	private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<DoctorEntity>(mList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<DoctorEntity> list = new ArrayList<DoctorEntity>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final ArrayList<DoctorEntity> values = mOriginalValues;
                final int count = values.size();

                final ArrayList<DoctorEntity> newValues = new ArrayList<DoctorEntity>(count);

                for (int i = 0; i < count; i++) {
                    final DoctorEntity value = values.get(i);
                    final String valueText = value.input_code.toUpperCase();
                    // First match against the whole, non-splitted value
                    if (valueText.indexOf(prefixString.toUpperCase())!=-1) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mList = (ArrayList<DoctorEntity>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
	
}
