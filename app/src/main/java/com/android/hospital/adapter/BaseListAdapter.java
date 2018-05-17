package com.android.hospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created with whlong.
 * User: fasf
 * Date: 13-7-27
 * Time: ����7:31
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseListAdapter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List mList;

    private BaseListAdapter(Context context){
        this.mContext=context;
        this.mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BaseListAdapter(Context context, List list){
        this(context);
        mList=list;
    }

    @Override
    public int getCount() {
        if (mList==null){
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
