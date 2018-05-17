package com.android.hospital.adapter;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.hospital.entity.BloodEntity;
import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.entity.DiagnosisEntity;
import com.android.hospital.entity.OperationEntity;
import com.android.hospital.ui.activity.R;
/**
 * 
* @ClassName: DiagnosisAdapter 
* @Description: TODO(诊断Adatper) 
* @author lll 
* @date 2014-01-17 
*
 */
public class DiagnosisAdapter extends BaseAdapter{
	
	private ArrayList<DiagnosisEntity> mList;
	
	private Context mContext;
	
	public DiagnosisAdapter(Context context,ArrayList<DiagnosisEntity> entities){
		this.mList=entities;
		this.mContext=context;
		
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
	* @Description: TODO(得到所有的诊断集合) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public ArrayList<DiagnosisEntity> getList(){
		return mList;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.activity_patient_information_list, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.patientInfor_Diag_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.patientInfor_Diag_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.patientInfor_Diag_tev_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.patientInfor_Diag_tev_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.patientInfor_Diag_tev_5);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		DiagnosisEntity item=mList.get(position);
		//int id=position+1;
		viewHolder.tev1.setText(item.admission_date_time);
		viewHolder.tev2.setText(item.diagnosis_type);
		viewHolder.tev3.setText(item.diagnosis_type_name);
		viewHolder.tev4.setText(item.diagnosis_no);
		viewHolder.tev5.setText(item.diagnosis_desc);
		if (position>0) {
			DiagnosisEntity last_item=mList.get(position-1);//获得上一条信息
			if(null!=last_item){
				if (last_item.admission_date_time.equals(item.admission_date_time)) {
					viewHolder.tev1.setText("");
				}
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
   	  
	
   }
	
   private class ViewHolder{
	public TextView tev1,tev2,tev3,tev4,tev5;
	public View tevDivider;
   }
}
