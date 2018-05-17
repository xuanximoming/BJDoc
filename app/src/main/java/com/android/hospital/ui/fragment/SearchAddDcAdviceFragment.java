package com.android.hospital.ui.fragment;

import com.android.hospital.HospitalApp;
import com.android.hospital.adapter.DrugAdapter;
import com.android.hospital.adapter.NonDrugAdapter;
import com.android.hospital.entity.DataEntity;
import com.android.hospital.entity.DrugEntity;
import com.android.hospital.entity.NonDrugEntity;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
/**
 * 
* @ClassName: SearchAddDcAdviceFragment 
* @Description: TODO(新增医嘱搜索界面) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:41:57 
*
 */
public class SearchAddDcAdviceFragment extends Fragment implements SearchView.OnQueryTextListener,OnItemClickListener{

	private ListView mListView;
	
	private SearchView mSearchView;
	
	private HospitalApp app;
	
	private DrugAdapter drugAdapter;//药品adapter
	
	private NonDrugAdapter nonDrugAdapter;//非药品adapter
	
	private boolean isDrugOrNonDrug=true;//显示的为药品还是非药品标记
	
	private AddDcAdviceFragment addDcAdviceFm;//左边的fragment
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app=(HospitalApp) getActivity().getApplication();
		drugAdapter=new DrugAdapter(getActivity(), R.layout.fragment_search_list_item, app.getDrugList());
		nonDrugAdapter=new NonDrugAdapter(getActivity(), R.layout.fragment_search_list_item, app.getNondrugList());
		addDcAdviceFm=(AddDcAdviceFragment) getActivity().getFragmentManager().findFragmentByTag("addfm");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_search, null);
		mListView=(ListView) view.findViewById(R.id.search_list_view);
		mSearchView=(SearchView) view.findViewById(R.id.search_search_view);
		mListView.setTextFilterEnabled(true);
		mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setQueryHint("请输入");
        showDrug();
        mListView.setOnItemClickListener(this);
		return view;
	}
	
	/**
	 * 
	* @Title: showDrug 
	* @Description: TODO(显示药品) 
	* @param @param adapter    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void showDrug(){
		mListView.setAdapter(drugAdapter);
		isDrugOrNonDrug=true;
	}
	
	/**
	 * 
	* @Title: showNonDrug 
	* @Description: TODO(显示非药品) 
	* @param @param adapter    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void showNonDrug(){
		mListView.setAdapter(nonDrugAdapter);
		isDrugOrNonDrug=false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (TextUtils.isEmpty(newText)) {
        	mListView.clearTextFilter();
        } else {
        	mListView.setFilterText(newText.toString());
        }
        return true;
	}
	
    /**
     *
    * @Title: onItemClick 
	* @Description: TODO(listview中项目的点击事件) 
	* @param     设定文件 
	* @return void    返回类型  
     */  
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //隐藏软键盘
        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

		mListView.requestFocus();
		mListView.setFocusable(true);
		//判断是药品、非药品，获取选中项目的实体属性
		if (isDrugOrNonDrug) {
			DrugEntity drugEntity=(DrugEntity) mListView.getAdapter().getItem(position);
			addDcAdviceFm.initDrug(drugEntity);
		}else {
			NonDrugEntity nonDrugEntity=(NonDrugEntity) mListView.getAdapter().getItem(position);
			addDcAdviceFm.initNonDrug(nonDrugEntity);
		}
	}
}
