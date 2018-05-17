package com.android.hospital.ui.fragment;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.CheckAdapter;
import com.android.hospital.constant.AppConstant;
import com.android.hospital.entity.CheckEntity;
import com.android.hospital.ui.activity.AddCheckActivity;
import com.android.hospital.ui.activity.AddDcAdviceActivity;
import com.android.hospital.ui.activity.CheckdetailActivity;
import com.android.hospital.ui.activity.MainActivity;
import com.android.hospital.ui.activity.MonovoStudyListActivity;
import com.android.hospital.ui.activity.MonovoTempImagesActivity;
import com.android.hospital.ui.activity.PcsPhotoViewActivity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.ui.activity.TempPhotoActivity;
import com.android.hospital.util.DebugUtil;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
* @ClassName: CheckFragment 
* @Description: TODO(检查界面显示) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:40:23 
*
 */
public class CheckFragment extends ListFragment {
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getListView().setFastScrollEnabled(true);
		getActivity().findViewById(R.id.listview_common_titlebar).setVisibility(View.GONE);
		setEmptyText("未获取到数据");
	}
	
	/**
	 * 
	* @Title: clearAdapter 
	* @Description: TODO(清空adapter) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void clearAdapter(){
	    CheckAdapter adapter=(CheckAdapter) getListAdapter();
		if (null!=adapter&&adapter.getCount()!=0) {
			adapter.clearAdapter();
			if (isAdded()) {
				setListShown(false);
				setEmptyText("");
			}			
		}
	}
	
	/**
	 *
	* @Title: onListItemClick 
	* @Description: TODO(选中检查列表中的项目) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		CheckAdapter adapter=(CheckAdapter) getListAdapter();
		CheckEntity entity=(CheckEntity) adapter.getItem(position);
		//跳转到检查明细项目界面
        Intent intent=new Intent(getActivity(), MonovoStudyListActivity.class);
        intent.putExtra("exam_no",entity.exam_no);
        startActivity(intent);

        /*startActivity(new Intent(getActivity(), MonovoTempImagesActivity.class));*/
	}
	

}
