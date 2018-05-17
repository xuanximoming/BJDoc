package com.android.hospital.ui.fragment;

import com.android.hospital.adapter.GridButtonAdapter;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;

import android.R.anim;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * 
* @ClassName: SignsLifeFragment 
* @Description: TODO(生命体征) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-30 下午10:12:31 
*
 */
public class SignsLifeFragment extends Fragment{

	private GridView mGridView=null;
	
	private GridButtonAdapter mAdapter=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAdapter=new GridButtonAdapter(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//界面布局
		View view=inflater.inflate(R.layout.fragment_signslife, null);
		mGridView=(GridView) view.findViewById(R.id.signslife_gridview);	
		return view;
	}	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//隐藏主界面的titlebar信息（即病人的基本信息栏）
		getActivity().findViewById(R.id.listview_common_titlebar).setVisibility(View.GONE);
		//给mGridView中的button赋值
		mGridView.setAdapter(mAdapter);	
	}
}
