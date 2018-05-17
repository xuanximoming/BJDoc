package com.android.hospital.ui.fragment;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.OperationItemAdapter;
import com.android.hospital.entity.OperationItemEntity;
import com.android.hospital.ui.activity.R;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

/**
 * 
* @ClassName: SeachOperationFragment 
* @Description: TODO(新增手术搜索界面) 
* @author lll 
* @date 2013-03-02 
*
 */
public class SeachOperationFragment extends SearchFragment implements OnItemClickListener{
	
	private OperationItemAdapter mAdapter;

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
		mAdapter=new OperationItemAdapter(getActivity(), R.layout.fragment_add_operationitem_list_item, app.getOperationItemList());
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
		OperationItemEntity entity=(OperationItemEntity) parent.getAdapter().getItem(position);
		AddOperationFragment fm=(AddOperationFragment) getActivity().getFragmentManager().findFragmentByTag("addfm");
		fm.setListView(entity);
	}
}
