package com.android.hospital.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.R.array;
import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.hospital.HospitalApp;
import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.ui.activity.GroupDcAdviceActivity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.ui.fragment.AddDcAdviceFragment;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.Util;
/**
 * 
* @ClassName: GroupDcAdviceAdapter 
* @Description: TODO(医嘱Adatper) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:39:14 
*
 */
public class GroupDcAdviceAdapter extends BaseAdapter{
	private ArrayList<DcAdviceEntity> mList;
	private Context mContext;
	private List<Map<String, String>> mFreqList;//频次list
	private List<Map<String, String>> mWayList;//途径list
	private HospitalApp app;
	
	public GroupDcAdviceAdapter(Context context,ArrayList<DcAdviceEntity> entities){
		this.mList=entities;
		this.mContext=context;
		this.app=(HospitalApp) mContext.getApplicationContext();
		mFreqList=app.getFreqList();
		mWayList=app.getWayList();
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.activity_group_dcadvice_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_1);
			viewHolder.tev2=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_2);
			viewHolder.tev3=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_3);
			viewHolder.tev4=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_4);
			viewHolder.tev5=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_5);
			viewHolder.tev6=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_6);
			viewHolder.tev7=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_7);
			viewHolder.tev8=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_8);
			//viewHolder.tev9=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_9);
			viewHolder.tev10=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_10);
			viewHolder.tev11=(TextView) convertView.findViewById(R.id.dcadvice_item_tev_11);
			viewHolder.mDeleteBut=(Button) convertView.findViewById(R.id.dcadvice_item_tev_12);
			viewHolder.tevDivider=(View)convertView.findViewById(R.id.listview_divider);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		final DcAdviceEntity item=mList.get(position);
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
		/*String perform_schdule=item.perform_schedule==null?"":item.perform_schedule;
		if (perform_schdule.equals("无")) {
			viewHolder.tev9.setText("");
		}else if(perform_schdule.equals("")){
			if (!item.administration.equals("")||!item.frequency.equals("")) {
				setPerform_schedule(item, position);
			}else {
				viewHolder.tev9.setText("");
			}
		}else {
			viewHolder.tev9.setText(item.perform_schedule);
		}		*/	
		viewHolder.tev10.setText(item.doctor);
		viewHolder.tev11.setText(item.freq_detail);
		viewHolder.mDeleteBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mList.remove(item);
				notifyDataSetChanged();
			}
		});
		
		viewHolder.tev5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DebugUtil.debug("测试点击--->");
				showEditDialog(position);
			}
		});
		viewHolder.tev7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSingleChoiceDialog(position, 0);
			}
		});
		viewHolder.tev8.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSingleChoiceDialog(position, 1);
			}
		});
		if(!"1".equals(item.order_sub_no)){ //子医嘱显示，不必要的项目填空
			viewHolder.tev1.setText("");
			viewHolder.tev2.setText("");
			viewHolder.tev3.setText("");
			viewHolder.tev7.setText("");
			viewHolder.tev8.setText("");
			viewHolder.tev9.setText("");
		}
		return convertView;
	}
	
   private class ViewHolder{
	public TextView tev1,tev2,tev3,tev4,tev5,tev6,tev7,tev8,tev9,tev10,tev11;
	public View tevDivider;
	public Button mDeleteBut;
   }
   
   /**
    * 
   * @Title: showEditDialog 
   * @Description: TODO(弹出编辑对话框) 
   * @param     设定文件 
   * @return void    返回类型 
   * @throws
    */
   private void showEditDialog(final int position){
       LayoutInflater factory = LayoutInflater.from(mContext);
       final View textEntryView = factory.inflate(R.layout.activity_group_editdialog, null);
       final EditText editText=(EditText) textEntryView.findViewById(R.id.activity_group_editdialog_edit);
       final DcAdviceEntity item=(DcAdviceEntity) getItem(position);
       if (item.dosage!=null) {
		  editText.setText(item.dosage);
	   }
       new AlertDialog.Builder(mContext)
           .setIconAttribute(android.R.attr.alertDialogIcon)
           .setTitle("请编辑")
           .setView(textEntryView)
           .setPositiveButton("确认", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int whichButton) {

                   /* User clicked OK so do some stuff */
            	   item.dosage=editText.getText().toString();
            	   mList.set(position, item);
            	   notifyDataSetChanged();
               }
           })
           .setNegativeButton("取消", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int whichButton) {

                   /* User clicked cancel so do some stuff */
               }
           }).create().show();
   }
   
   private int whichChoose=0;//showSingleChoiceDialog选中项id
   /**
    * 
   * @Title: showSingleChoiceDialog 
   * @Description: TODO(途径或频次的对话框) 
   * @param     设定文件 
   * @return void    返回类型 
   * @throws
    */
   private void showSingleChoiceDialog(final int position,final int which){
	   final String[] array=getArrayItem(which); 
	   new AlertDialog.Builder(mContext)
       .setIconAttribute(android.R.attr.alertDialogIcon)
       .setTitle("请选择")
       .setSingleChoiceItems(array, whichChoose, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int whichButton) {

               /* User clicked on a radio button do some stuff */
        	   whichChoose=whichButton;
           }
       })
       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int whichButton) {

               /* User clicked Yes so do some stuff */
        	   final DcAdviceEntity item=(DcAdviceEntity) getItem(position);
        	   if (which==0) {
        		   item.administration=array[whichChoose];
			   }else {
				   item.frequency=array[whichChoose];
				   item.freq_counter=mFreqList.get(whichChoose).get("freq_counter");
				   item.freq_interval=mFreqList.get(whichChoose).get("freq_interval");
				   item.freq_interval_unit=mFreqList.get(whichChoose).get("freq_interval_unit");
				   DebugUtil.debug("频次中的值--->"+mFreqList.get(whichChoose).get("freq_interval_unit"));
			   }
        	  // setPerform_schedule(item, position);
           }
       })
       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int whichButton) {

               /* User clicked No so do some stuff */
           }
       })
      .create().show();
   }

   /**
    * 
   * @Title: getArrayItem 
   * @Description: TODO(获取需要显示的数组) 0为途径，否则为频次
   * @param @return    设定文件 
   * @return String[]    返回类型 
   * @throws
    */
  private String[] getArrayItem(int which) {
	// TODO Auto-generated method stub
	  String[] arr;
	  if (which==0) {
		  arr=new String[mWayList.size()];
	 	for (int i = 0; i < mWayList.size(); i++) {
	 		arr[i]=mWayList.get(i).get("administration_name");
		}
	  }else {
		  arr=new String[mFreqList.size()];
		  for (int i = 0; i < mFreqList.size(); i++) {
			arr[i]=mFreqList.get(i).get("freq_desc");
		  }
	  }
	  return arr;
  }
  /**
   * 
  * @Title: setPerform_schedule 
  * @Description: TODO(根据途径和频次获取执行时间) 
  * @param     设定文件 
  * @return void    返回类型 
  * @throws
   */
  /*
  private void setPerform_schedule(final DcAdviceEntity item,final int position){
	  new TimeTask(null, new TimeTaskCallback() {
			
			@Override
			public void callback(String result) {
				// TODO Auto-generated method stub
				   if (result==null||result.equals("")) {
					   item.perform_schedule="无";
				   }else {
					   item.perform_schedule=result;
				   }	   
				   mList.set(position, item);
	        	   notifyDataSetChanged();
			}
		}).execute(item.administration,item.frequency);    	  
  }*/
}
