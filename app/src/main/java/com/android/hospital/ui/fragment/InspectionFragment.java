package com.android.hospital.ui.fragment;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.InspectionAdapter;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.entity.InspectionEntity;
import com.android.hospital.ui.activity.InspectiondetailActivity;
import com.android.hospital.ui.activity.MainActivity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 
* @ClassName: InspectionFragment 
* @Description: TODO(检验界面) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:41:04 
*
 */
public class InspectionFragment extends ListFragment{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getListView().setFastScrollEnabled(true);
		getActivity().findViewById(R.id.listview_common_titlebar).setVisibility(View.GONE);
		setEmptyText("未获取到数据");
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		InspectionAdapter adapter=(InspectionAdapter) l.getAdapter();
		InspectionEntity item=(InspectionEntity) adapter.getItem(position);
		//跳转到新界面，检验的明细项目界面
		Intent intent=new Intent();
		intent.putExtra("inspection", item);
		intent.setClass(getActivity(), InspectiondetailActivity.class);
		startActivity(intent);
	}

}
