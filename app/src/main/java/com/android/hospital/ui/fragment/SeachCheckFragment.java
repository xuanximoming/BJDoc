package com.android.hospital.ui.fragment;

import java.util.Map;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.CheckItemAdapter;
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
* @ClassName: SeachCheckFragment 
* @Description: TODO(新增检查搜索界面) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-10 
*
 */
public class SeachCheckFragment extends SearchFragment implements OnItemClickListener{

	private AddCheckFragment fm;
	
	@Override
	public void show(BaseAdapter adapter) {
		// TODO Auto-generated method stub
		CheckItemAdapter itemAdapter=(CheckItemAdapter) adapter;
		getListView().setAdapter(itemAdapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getListView().setOnItemClickListener(this);
		fm=(AddCheckFragment) getActivity().getFragmentManager().findFragmentByTag("addfm");
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
		Map<String, String> map=(Map<String, String>) parent.getAdapter().getItem(position);
		fm.setListView(map);
	}
	
}
