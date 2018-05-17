package com.android.hospital.adapter;

import java.util.ArrayList;

import com.android.hospital.entity.CheckEntity;
import com.android.hospital.ui.activity.R;
import com.github.snowdream.android.util.Log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
* @ClassName: CheckAdapter 
* @Description: TODO(检查adapter) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 下午7:51:19 
*
 */
public class CheckAdapter extends BaseAdapter{
	
	private Context mContext;
	
	private ArrayList<CheckEntity> mList;

    private static final String TAG="CheckAdapter";

	public CheckAdapter(Context context,ArrayList<CheckEntity> arrayList){
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

	/**
	 * 
	* @Title: clearAdapter 
	* @Description: TODO(每次获取，清空之前) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void clearAdapter(){
		mList.clear();
	    notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.fragment_check_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.check_item_tev1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.check_item_tev2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.check_item_tev3);
			//viewHolder.tev4=(TextView) convertView.findViewById(R.id.check_item_tev4);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		CheckEntity item=mList.get(position);
		viewHolder.tev1.setText("状态:"+item.result_status+"  类别："+item.exam_class+"  子类："+item.exam_sub_class+"  项目："+item.exam_item
				+ "  申请时间："+item.req_date_time+"  申请医生："+item.req_physician+"  报告时间："+item.report_date_time);

        Log.d(TAG,"item.result_status="+item.result_status+",item.description="+item.description+",item.impression="+item.impression);

/*		if("已报告".equals(item.result_status))
		{
			viewHolder.tev2.setText("检查所见："+item.description);
			viewHolder.tev3.setText("印象："+item.impression);
		}
		if("提交".equals(item.result_status))
		{
			viewHolder.tev2.setText("");
			viewHolder.tev3.setText("");
		}*/

        viewHolder.tev2.setText("检查所见："+item.description);
        viewHolder.tev3.setText("印象："+item.impression);
		
		//viewHolder.tev4.setText();		
		return convertView;
	}

	
	private class ViewHolder{
		public TextView tev1,tev2,tev3,tev4;
	  
	}
}
