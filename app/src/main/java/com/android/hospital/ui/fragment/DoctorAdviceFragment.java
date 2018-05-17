package com.android.hospital.ui.fragment;

import java.util.ArrayList;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.CheckAdapter;
import com.android.hospital.adapter.DcAdviceAdapter;
import com.android.hospital.asyntask.DcAdviceTask;
import com.android.hospital.asyntask.add.MaxNumberTask;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.db.ServerDao;
import com.android.hospital.entity.DcAdviceEntity;
import com.android.hospital.ui.activity.AddDcAdviceActivity;
import com.android.hospital.ui.activity.GroupDcAdviceActivity;
import com.android.hospital.ui.activity.MainActivity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.Util;
import com.android.hospital.webservice.WebServiceHelper;

import android.R.menu;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
* @ClassName: DoctorAdviceFragment 
* @Description: TODO(医嘱界面) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:40:47 
*
 */
public class DoctorAdviceFragment extends ListFragment {

	private HospitalApp app;

	private int position=0;//点击模板id

	private MainActivity mActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app=(HospitalApp) getActivity().getApplication();
		//ArrayList<DcAdviceEntity> arrayList=new DcAdviceEntity("").list;
//		DcAdviceAdapter adapter=new DcAdviceAdapter(getActivity(),arrayList);
//		setListAdapter(adapter);
		mActivity=(MainActivity) getActivity();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		getListView().setDivider(getResources().getDrawable(R.drawable.main_list_divder));
//		getListView().setDividerHeight(2);
	}
	
	/**
	 * 
	* @Title: onCreateOptionsMenu 
	* @Description: TODO(右上角菜单项) 
	* @param     该项只在医嘱界面时使用，顾在医嘱的fragment中添加，其他项在mainActivity中添加 
	* @return boolean    返回类型 
	* @throws
	 */	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.addSubMenu(1, 11, 1, "全部");
		menu.addSubMenu(1, 12, 1, "长期");
		menu.addSubMenu(1, 13, 1, "临时");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getListView().setFastScrollEnabled(true);
		getListView().setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("请选择");       
				menu.add(0, 0, 0, "停止该条医嘱");  
				menu.add(0, 1, 0, "复制该条医嘱");  
				menu.add(0, 2, 0, "取消");  
			}
		});
		getActivity().findViewById(R.id.listview_common_titlebar).setVisibility(View.VISIBLE);
		setEmptyText("未获取到数据");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		ContextMenuInfo menuInfo = (ContextMenuInfo) item.getMenuInfo();
		AdapterContextMenuInfo id=(AdapterContextMenuInfo) menuInfo; 
		int position=id.position;//第几项
		DebugUtil.debug("id-->"+id.position);
		switch (item.getItemId()) {
		case 0:
			 DcAdviceAdapter adapter=(DcAdviceAdapter) getListAdapter();
			 DcAdviceEntity entity=(DcAdviceEntity) adapter.getItem(position);
			 String req_date_time=Util.toSimpleDate();//获得系统时间
			 //临时医嘱或者还没有处理的医嘱，不能停
			 if("0".equals(entity.repeat_indicator)||"6".equals(entity.order_status)){
				 Toast.makeText(getActivity(), "临时医嘱或护士未处理的医嘱不能停!", Toast.LENGTH_SHORT).show(); 
				 return false;
			 }else{
				 if(!"1".equals(entity.order_sub_no)){ //如果不是主医嘱
					 Toast.makeText(getActivity(), "此为子医嘱，需要停主医嘱!", Toast.LENGTH_SHORT).show(); 
					 return false;
				 }else{
					 ArrayList<DcAdviceEntity> list = adapter.getList();//获的所有的医嘱
					 ArrayList<DcAdviceEntity> list_update = new ArrayList<DcAdviceEntity>();//存放需要停止的医嘱
					 for (int i = 0; i < list.size(); i++) {
						if(entity.order_no.equals(list.get(i).order_no)){
							list_update.add(list.get(i));
						}
					}//for
					for (int i = 0; i < list_update.size(); i++) {
						String sql="UPDATE ORDERS "+
		                         " SET STOP_DOCTOR= '"+app.getDoctor()+"',"+
								 " STOP_DATE_TIME= TO_DATE('"+req_date_time+"','yyyy-MM-dd hh24:mi:ss'),"+
	        		             " ORDER_STATUS='6',BILLING_ATTR='0',DRUG_BILLING_ATTR='0' "+
	                         " WHERE PATIENT_ID  = '"+app.getPatientEntity().patient_id+"'"+
	                         " AND VISIT_ID = '"+app.getPatientEntity().visit_id+"'"+
	                         " AND ORDER_NO = '"+list_update.get(i).order_no+"'"+
	                         " AND ORDER_SUB_NO = '"+list_update.get(i).order_sub_no+"'";
						Log.e("更新语句-->", sql);
						if (WebServiceHelper.updateWebServiceData(sql)) {
							mActivity.putDcAdviceTask(app.getPatientEntity(), "");
							Toast.makeText(getActivity(), "更新成功!", Toast.LENGTH_SHORT).show(); 
						}else {
							Toast.makeText(getActivity(), "更新失败!", Toast.LENGTH_SHORT).show(); 
						}						
					} 
				 }
			 }//else1
			return true;
		default:
			break;
		}
		return false;
	}		

	/**
	 * 
	* @Title: getArrayItem 
	* @Description: TODO(得到模板数组) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public String[] getArrayItem(){
		int size=app.getGroupOrderList().size();
		String[] arr=new String[size];
		for (int i = 0; i < size; i++) {
			arr[i]=app.getGroupOrderList().get(i).title;
		}
		return arr;
	}
}

