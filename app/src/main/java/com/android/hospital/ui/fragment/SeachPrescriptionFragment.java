package com.android.hospital.ui.fragment;

import java.util.ArrayList;
import com.android.hospital.adapter.PreMiddleDrugAdapter;
import com.android.hospital.entity.DrugEntity;
import com.android.hospital.ui.activity.R;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

/**
 * 
* @ClassName: SeachPrescriptionFragment 
* @Description: TODO(新增处方搜索界面) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-20 
*
 */
public class SeachPrescriptionFragment extends SearchFragment implements OnItemClickListener{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		show(null);//中药
		getListView().setOnItemClickListener(this);
	}
	
	/**
	 * 默认显示中药集合
	 */
	@Override
	public void show(BaseAdapter adapter) {
		// TODO Auto-generated method stub
	    ArrayList<DrugEntity> arrayList=getApp().getMiddleDrugList();
		PreMiddleDrugAdapter preAdapter = new PreMiddleDrugAdapter(
				getActivity(), R.layout.fragment_add_prescription_list_item,
				arrayList);
		getListView().setAdapter(preAdapter);
	}
	/**
	 * 
	* @Title: showWestDrug 
	* @Description: TODO(显示西药) 
	* @param     设定文件  
	* @return void    返回类型 
	* @throws
	 */
	public void showWestDrug(ArrayList<DrugEntity> arrayList){
		PreMiddleDrugAdapter preAdapter = new PreMiddleDrugAdapter(
				getActivity(), R.layout.fragment_add_prescription_list_item,
				arrayList);
		getListView().setAdapter(preAdapter);
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
		DrugEntity drugEntity=(DrugEntity) parent.getAdapter().getItem(position);
		AddPrescriptionFragment fm=(AddPrescriptionFragment) getActivity().getFragmentManager().findFragmentByTag("addfm");
		fm.setListView(drugEntity);
	}
}
