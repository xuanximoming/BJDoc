package com.android.hospital.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.hospital.entity.DrugEntity;
import com.android.hospital.entity.InspectionItemEntity;
import com.android.hospital.entity.OperationItemEntity;
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
* @ClassName: OperationItemAdapter
* @Description: TODO(手术项目的adapter，含检索功能) 
* @author lll 
* @date 2013-02-25 
*
 */
public class OperationItemAdapter extends ArrayAdapter<OperationItemEntity> implements Filterable{

	private Context mContext;
	
	private ArrayList<OperationItemEntity> mList;
	
	private LayoutInflater mInflater;
	
	private int resourceId;
	
	private ArrayFilter mFilter;
	
	private ArrayList<OperationItemEntity> mOriginalValues;
	
    private final Object mLock = new Object();
	
	public OperationItemAdapter(Context context,int textViewResourceId,ArrayList<OperationItemEntity> arrayList){
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
	public OperationItemEntity getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.fragment_add_operationitem_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.search_item_oper_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.search_item_oper_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.search_item_oper_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.search_item_oper_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.search_item_oper_5);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		OperationItemEntity item=mList.get(position);
		int id=position+1;
		viewHolder.tev1.setText(String.valueOf(id));
		viewHolder.tev2.setText(item.operation_code);
		viewHolder.tev3.setText(item.operation_name);
		viewHolder.tev4.setText(item.operation_scale);
		viewHolder.tev5.setText(item.input_code);	
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
                    mOriginalValues = new ArrayList<OperationItemEntity>(mList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<OperationItemEntity> list = new ArrayList<OperationItemEntity>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final ArrayList<OperationItemEntity> values = mOriginalValues;
                final int count = values.size();

                final ArrayList<OperationItemEntity> newValues = new ArrayList<OperationItemEntity>(count);

                for (int i = 0; i < count; i++) {
                    final OperationItemEntity value = values.get(i);
                    final String valueText = value.input_code.toUpperCase();
                    
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
            mList = (ArrayList<OperationItemEntity>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
