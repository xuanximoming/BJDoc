package com.android.hospital.ui.fragment;

import com.android.hospital.adapter.GridCheckBoxAdapter;
import com.android.hospital.ui.activity.R;
import com.android.hospital.widgets.MyGridView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
/**
 * 
* @ClassName: DayEstimateFragment 
* @Description: TODO(每日评估) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2013-1-31 下午12:00:18 
*
 */
public class DayEstimateFragment extends Fragment{
	
	
	private View mainView;
	
	//准备数据，可重新替换
	private String[] data1={"WNL","语言障碍","焦虑恐惧","自我认识","老伤","失眠","自杀倾向","有空白性","紧张"};
	private String[] data2={"WNL","神志水平","运动","握力","感知","头晕","肌力分级","抽搐","睁眼反应","言语反应","运动反应"};
	private String[] data3={"WNL","咳嗽","痰性状","胸腔引流","氧气","气管引流","呼吸困难","气管切开"};
	private String[] data4={"WNL","语言障碍","焦虑恐惧","自我认识","老伤","失眠","自杀倾向","有空白性","紧张"};
	private String[] data5={"WNL","语言障碍","焦虑恐惧","自我认识","老伤","失眠","自杀倾向","有空白性","紧张"};
	private String[] data6={"WNL","语言障碍","焦虑恐惧","自我认识","老伤","失眠","自杀倾向","有空白性","紧张"};
	private String[] data7={"WNL","语言障碍","焦虑恐惧","自我认识","老伤","失眠","自杀倾向","有空白性","紧张"};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainView=inflater.inflate(R.layout.fragment_dayestimate, null);
		initGridView();
		return mainView;
	}
	
	/**
	 * 初始化要显示的每项列表
	 */
	private void initGridView(){
		//列表一
		MyGridView gridView1=(MyGridView) mainView.findViewById(R.id.dayestimate_gridview1);
		GridCheckBoxAdapter mAdapter1=new GridCheckBoxAdapter(getActivity(), data1);
		gridView1.setAdapter(mAdapter1);
		
		//列表2
		MyGridView gridView2=(MyGridView) mainView.findViewById(R.id.dayestimate_gridview2);
		GridCheckBoxAdapter mAdapter2=new GridCheckBoxAdapter(getActivity(), data2);
		gridView2.setAdapter(mAdapter2);
		
		//列表3
		MyGridView gridView3=(MyGridView) mainView.findViewById(R.id.dayestimate_gridview3);
		GridCheckBoxAdapter mAdapter3=new GridCheckBoxAdapter(getActivity(), data3);
		gridView3.setAdapter(mAdapter3);
		
		//列表4
		MyGridView gridView4=(MyGridView) mainView.findViewById(R.id.dayestimate_gridview4);
		GridCheckBoxAdapter mAdapter4=new GridCheckBoxAdapter(getActivity(), data4);
		gridView4.setAdapter(mAdapter4);
		
		//列表2
		MyGridView gridView5=(MyGridView) mainView.findViewById(R.id.dayestimate_gridview5);
		GridCheckBoxAdapter mAdapter5=new GridCheckBoxAdapter(getActivity(), data5);
		gridView5.setAdapter(mAdapter5);
		
		//列表3
		MyGridView gridView6=(MyGridView) mainView.findViewById(R.id.dayestimate_gridview6);
		GridCheckBoxAdapter mAdapter6=new GridCheckBoxAdapter(getActivity(), data6);
		gridView6.setAdapter(mAdapter6);
		
		//列表4
		MyGridView gridView7=(MyGridView) mainView.findViewById(R.id.dayestimate_gridview7);
		GridCheckBoxAdapter mAdapter7=new GridCheckBoxAdapter(getActivity(), data7);
		gridView7.setAdapter(mAdapter7);
	}
}
