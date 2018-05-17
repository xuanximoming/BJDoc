package com.android.hospital.ui.fragment;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.InspectionItemAdapter;
import com.android.hospital.entity.InspectionItemEntity;
import com.android.hospital.ui.activity.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
/**
 * 
* @ClassName: SeachInspectionFragment 
* @Description: TODO(新增检验搜索界面) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 
*
 */
public class SeachInspectionFragment extends SearchFragment implements OnItemClickListener{
	
	private InspectionItemAdapter mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		show(null);
		getListView().setOnItemClickListener(this);
	}
	
	@Override
	public void show(BaseAdapter adapter) {
		// TODO Auto-generated method stub
		HospitalApp app=(HospitalApp) getActivity().getApplication();
		mAdapter=new InspectionItemAdapter(getActivity(), R.layout.fragment_add_inspectionitem_list_item, app.getInspectionItemList());
		getListView().setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //隐藏软键盘
        imm.hideSoftInputFromWindow(getSearchView().getWindowToken(), 0);
		getListView().requestFocus();
		getListView().setFocusable(true);
		InspectionItemEntity entity=(InspectionItemEntity) parent.getAdapter().getItem(position);
		AddInspectionFragment fm=(AddInspectionFragment) getActivity().getFragmentManager().findFragmentByTag("addfm");
		fm.setListView(entity);
	}
}
