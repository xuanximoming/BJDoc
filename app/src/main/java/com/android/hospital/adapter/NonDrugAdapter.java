package com.android.hospital.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.hospital.entity.DrugEntity;
import com.android.hospital.entity.NonDrugEntity;
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
* @Description: TODO(非药品的adapter，含检索功能) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 下午11:46:08 
*
 */
public class NonDrugAdapter extends ArrayAdapter<NonDrugEntity> implements Filterable{

	private Context mContext;
	
	private ArrayList<NonDrugEntity> mList;
	
	private int resourceId;
	
	private LayoutInflater mInflater;
	
	private ArrayFilter mFilter;
	
	private ArrayList<NonDrugEntity> mOriginalValues;
	
    private final Object mLock = new Object();
	
	public NonDrugAdapter(Context context,int textViewResourceId,ArrayList<NonDrugEntity> arrayList){
		super(context, textViewResourceId);
		this.mList=arrayList;
		this.mContext=context;
		this.mInflater=LayoutInflater.from(context);
		this.resourceId=textViewResourceId;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList==null?0:mList.size();
	}
	
	@Override
	public NonDrugEntity getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.fragment_add_dcadvice_nondrug_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.search_item_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.search_item_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.search_item_tev_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.search_item_tev_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.search_item_tev_5);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		NonDrugEntity item=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));
		viewHolder.tev2.setText(item.item_name);
		viewHolder.tev3.setText(item.input_code);
		viewHolder.tev4.setText(item.item_code);
		viewHolder.tev5.setText(item.item_class);
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
		public TextView tev1,tev2,tev3,tev4,tev5;
	}
	
	
	private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<NonDrugEntity>(mList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<NonDrugEntity> list = new ArrayList<NonDrugEntity>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final ArrayList<NonDrugEntity> values = mOriginalValues;
                final int count = values.size();

                final ArrayList<NonDrugEntity> newValues = new ArrayList<NonDrugEntity>(count);

                for (int i = 0; i < count; i++) {
                    final NonDrugEntity value = values.get(i);
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
            mList = (ArrayList<NonDrugEntity>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
