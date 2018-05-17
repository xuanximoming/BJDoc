package com.android.hospital.adapter;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.ui.activity.R;
/**
 * 
* @ClassName: DcAdviceAdapter 
* @Description: TODO(医嘱Adatper) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:39:14 
*
 */
public class DcAdviceAdapter extends BaseAdapter{
	
	private ArrayList<DcAdviceEntity> mList;
	
	private Context mContext;
	
	private ArrayList<DcAdviceEntity> allList;
	private ArrayList<DcAdviceEntity> longList;
	private ArrayList<DcAdviceEntity> shortList;

	
	public DcAdviceAdapter(Context context,ArrayList<DcAdviceEntity> entities){
		this.mList=entities;
		this.mContext=context;
		getAllList();
		getLongList();
		getShortList();
	}
	/**
	 * getShortList() 获得临时医嘱
	 */
	private void getShortList() {
		// TODO Auto-generated method stub
		shortList=new ArrayList<DcAdviceEntity>();
		for (int i = 0; i < mList.size(); i++) {
			if("0".equals(mList.get(i).repeat_indicator)){
				shortList.add(mList.get(i));
			}
		}
		
	}
   /**
    * getLongList() 获得长期医嘱
    * @return
    */
	private ArrayList<DcAdviceEntity> getLongList() {
		// TODO Auto-generated method stub
		longList=new ArrayList<DcAdviceEntity>();
		for (int i = 0; i < mList.size(); i++) {
			if ("1".equals(mList.get(i).repeat_indicator)) {		
				longList.add(mList.get(i));
			}
		}
		return longList;
	}
   /**
    * getAllList()获得所有医嘱
    * @return
    */
	private ArrayList<DcAdviceEntity> getAllList() {
		// TODO Auto-generated method stub
        allList=mList;
		return allList;
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
	/**
	 * 
	* @Title: getList 
	* @Description: TODO(得到所有的医嘱集合) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public ArrayList<DcAdviceEntity> getList(){
		return mList;
	}
    //设置长期
	public void setLongList(){
		
		mList=longList;
		notifyDataSetChanged();
	}
	//设置临时
	public void setShortList(){
		mList=shortList;
		notifyDataSetChanged();
	}
	//设置全部
	public void setAllList(){
		mList=allList;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.fragment_dcadvice_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_5);
			viewHolder.tev6=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_6);
			viewHolder.tev7=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_7);
			viewHolder.tev8=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_8);
			viewHolder.tev9=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_9);
			viewHolder.tev10=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_10);
			viewHolder.tev11=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_11);
			viewHolder.tev12=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_12);
			viewHolder.tevDivider=(View)convertView.findViewById(R.id.listview_divider);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		DcAdviceEntity item=mList.get(position);
		viewHolder.tev1.setText(item.order_no);
		String repeat_indicator = "长期";
		if (item.repeat_indicator.equals("0")) {
			repeat_indicator="临时";
		}
		viewHolder.tev2.setText(repeat_indicator);
		viewHolder.tev3.setText(item.start_date_time);
		viewHolder.tev4.setText(item.order_text);
		viewHolder.tev5.setText(item.dosage);
		viewHolder.tev6.setText(item.dosage_units);
		viewHolder.tev7.setText(item.administration);
		viewHolder.tev8.setText(item.frequency);
		viewHolder.tev9.setText(item.perform_schedule);
		viewHolder.tev10.setText(item.stop_date_time);
		viewHolder.tev11.setText(item.freq_detail);
		viewHolder.tev12.setText(item.doctor);
		if(position>1){
			DcAdviceEntity last_item=mList.get(position-1);//获得上一条医嘱信息
			if(null!=last_item){
				if(last_item.order_no.equals(item.order_no)){//当该遗嘱为上一条的子医嘱时
					//子医嘱部分信息部显示，置空
					viewHolder.tev1.setText(""); //order_no
					viewHolder.tev2.setText("");
					viewHolder.tev3.setText("");
					viewHolder.tev7.setText("");
					viewHolder.tev8.setText("");
					viewHolder.tev9.setText("");
					viewHolder.tev10.setText("");
					viewHolder.tev12.setText("");
				}
			}
		}
		//根据执行状态，设置不同颜色
		if("3".equals(item.order_status)){ //停止医嘱
			setTextColor(viewHolder, mContext.getResources().getColor(R.color.lightpink));
			
		}else{	
			if("8".equals(item.order_status)||"4".equals(item.order_status)){ //作废医嘱
				setTextColor(viewHolder, Color.RED);
				
			}else{
				setTextColor(viewHolder, mContext.getResources().getColor(R.color.cornflowerblue));
			}
			//根据是否基药，设置不同颜色
			if ("1".equals(item.is_basic)) {
			//	setTextColor(viewHolder, Color.YELLOW);
				setTextColor(viewHolder,mContext.getResources().getColor(R.color.darkviolet));
			}
			if ("1".equals(item.antibiotic)) {
				setTextColor(viewHolder, Color.GREEN);
			}
		}	
		return convertView;
	}
	
//根据不同的状态，改变医嘱的显示颜色
   public void setTextColor(ViewHolder viewHolder,int color){
	   viewHolder.tev1.setTextColor(color);
   	   viewHolder.tev2.setTextColor(color);
   	   viewHolder.tev3.setTextColor(color);
   	   viewHolder.tev4.setTextColor(color);
   	   viewHolder.tev5.setTextColor(color);
   	   viewHolder.tev6.setTextColor(color);
   	   viewHolder.tev7.setTextColor(color);
   	   viewHolder.tev8.setTextColor(color);
   	   viewHolder.tev9.setTextColor(color);
   	   viewHolder.tev10.setTextColor(color);
   	   viewHolder.tev11.setTextColor(color);
   	   viewHolder.tev12.setTextColor(color);
	
   }
	
   private class ViewHolder{
	public TextView tev1,tev2,tev3,tev4,tev5,tev6,tev7,tev8,tev9,tev10,tev11,tev12;
	public View tevDivider;
   }
}
