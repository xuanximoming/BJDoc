package com.android.hospital.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.hospital.entity.DrugEntity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


/**
 * 
* @ClassName: DrugAdapter 
* @Description: TODO(药品的adapter，含检索功能) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 下午11:46:08 
*
 */
public class DrugAdapter extends ArrayAdapter<DrugEntity> implements Filterable{

	private Context mContext;
	
	private ArrayList<DrugEntity> mList;
	
	private LayoutInflater mInflater;
	
	private int resourceId;
	
	private ArrayFilter mFilter;
	
	private ArrayList<DrugEntity> mOriginalValues;
	
    private final Object mLock = new Object();
	
	public DrugAdapter(Context context,int textViewResourceId,ArrayList<DrugEntity> arrayList){
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
	public DrugEntity getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.fragment_search_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.search_item_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.search_item_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.search_item_tev_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.search_item_tev_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.search_item_tev_5);
			viewHolder.tev6=(TextView) convertView.findViewById(R.id.search_item_tev_6);
			viewHolder.tev7=(TextView) convertView.findViewById(R.id.search_item_tev_7);
			viewHolder.tev8=(TextView) convertView.findViewById(R.id.search_item_tev_8);
			viewHolder.tev9=(TextView) convertView.findViewById(R.id.search_item_tev_9);
			viewHolder.tev10=(TextView) convertView.findViewById(R.id.search_item_tev_10);
			viewHolder.tev11=(TextView) convertView.findViewById(R.id.search_item_tev_11);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		DrugEntity item=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));
		viewHolder.tev2.setText(item.drug_name);
		viewHolder.tev3.setText(item.package_units);
		viewHolder.tev4.setText(item.drug_spec);
		viewHolder.tev5.setText(item.storage);
		viewHolder.tev6.setText(item.storage_name);
		viewHolder.tev7.setText(item.dose_per_unit);
		viewHolder.tev8.setText(item.dose_units);
		viewHolder.tev9.setText(item.input_code);
		viewHolder.tev10.setText(item.drug_code);
		viewHolder.tev11.setText(item.is_basic);

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
		public TextView tev1,tev2,tev3,tev4,tev5,tev6,tev7,tev8,tev9,tev10,tev11;
	}
	
	
	private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<DrugEntity>(mList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<DrugEntity> list = new ArrayList<DrugEntity>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final ArrayList<DrugEntity> values = mOriginalValues;
                final int count = values.size();

                final ArrayList<DrugEntity> newValues = new ArrayList<DrugEntity>(count);

                for (int i = 0; i < count; i++) {
                    final DrugEntity value = values.get(i);
                    final String valueText = value.input_code.toUpperCase();
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
            mList = (ArrayList<DrugEntity>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
