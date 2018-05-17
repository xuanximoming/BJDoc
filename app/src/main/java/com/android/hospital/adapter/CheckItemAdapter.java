package com.android.hospital.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.hospital.entity.DrugEntity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


public class CheckItemAdapter extends ArrayAdapter<Map<String,String>> implements Filterable{

	private Context mContext;
	
	private ArrayList<Map<String, String>> mList;
	
	private LayoutInflater mInflater;
	
	private int resourceId;
	private ArrayFilter mFilter;
	
    private ArrayList<Map<String, String>> mOriginalValues;
	
    private final Object mLock = new Object();
	
/*	public CheckItemAdapter(Context context,List<Map<String, String>> list){
		this.mContext=context;
		this.mList=list;
		this.mInflater=LayoutInflater.from(context);
	}*/
	
	public CheckItemAdapter(Context context,int textViewResourceId,ArrayList<Map<String, String>> arrayList){
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
	public Map<String, String> getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}


	public void clear(){
		mList.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.fragment_add_checkitem_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.search_item_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.search_item_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.search_item_tev_3);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		Map<String, String> map=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));
		viewHolder.tev2.setText(map.get("description"));
		viewHolder.tev3.setText(map.get("input_code"));
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
		public TextView tev1,tev2,tev3;
	}
	
	
	private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<Map<String, String>>(mList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final ArrayList<Map<String, String>> values = mOriginalValues;
                final int count = values.size();

                final ArrayList<Map<String, String>> newValues = new ArrayList<Map<String, String>>(count);

                for (int i = 0; i < count; i++) {
                    final Map<String, String> value = values.get(i);
                    final String valueText = value.get("input_code").toUpperCase();
                    // First match against the whole, non-splitted value
                    if (valueText.indexOf(prefixString.toUpperCase())!=-1) {
                    	DebugUtil.debug("执行次数"+i);
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
            mList = (ArrayList<Map<String, String>>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
	
}
