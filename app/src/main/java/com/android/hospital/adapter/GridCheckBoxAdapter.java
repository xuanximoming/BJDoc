package com.android.hospital.adapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.commons.logging.Log;

import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;

import android.R.array;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
/**
 * 
* @ClassName: GridCheckBoxAdapter 
* @Description: TODO(每日评估的checkbox) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2013-1-31 上午11:28:17 
*
 */
public class GridCheckBoxAdapter extends BaseAdapter{
	
	private Context mContext;

	private String[] dataArr;//传过来的需要显示文本内容数组
	
	private LayoutInflater mInflater;
	
	/**
	 * 在构造函数中把数组传过来
	 */
	public GridCheckBoxAdapter(Context context,String[] data){
		this.mContext=context;
		this.dataArr=data;
		mInflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataArr==null?0:dataArr.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataArr[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.fragment_dayestimate_gridview_item, null);
		}
		final CheckBox box=(CheckBox) convertView.findViewById(R.id.gridview_item_checkbox);
		final String text=dataArr[position];//填充每个checkbox的显示内容
		box.setText(text);
		box.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//在这里判断是否弹出对话框（如果该项checkbox的显示内容等于需要显示弹出框的内容，则弹出对话框）
				if (text.equals("语言障碍")||text.equals("神志水平")) {//用或来依次判断每个需要弹出的对话框，再后面继续添加既可以,这里暂时写两个
					showDialogs(box,true);
				}else {
					showDialogs(box,false);
				}
			}
		});
		return convertView;
	}

	/**
	 * 
	* @Title: showDialogs 
	* @Description: TODO(显示对话框的方法) 
	* @param @param text
	* @param @param isShow    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void showDialogs(CheckBox box,boolean isShow){
		final ArrayList<Integer> morechecked=new ArrayList<Integer>();
		if (isShow) {
			AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
			dialog.setTitle("请选择");
			dialog.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked Yes so do some stuff */
                }
            });
			dialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked No so do some stuff */
                }
            });
			int showTextArr=0;//对话框中需要显示的内容数组，通过判断对应的内容来显示不同的数组
			String[] showText=null;
			boolean[] bool=null;
			int length=0;
			Resources res=mContext.getResources();
			if (box.getText().equals("语言障碍")) {
				showText=res.getStringArray(R.array.yuyanzhangai);
				length=showText.length;
				DebugUtil.debug("选中的是哪个array", showText[1]);
				//showTextArr=R.array.yuyanzhangai;
			}else if(box.getText().equals("神志水平")){
				//showTextArr=R.array.shenzhishuiping;
				//DebugUtil.debug("选中的是哪个array", ""+showTextArr);
			}	
			for (int i = 0; i < length; i++) {
				bool[i]=false;
			}
			dialog.setMultiChoiceItems(showText, bool,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton,
                                boolean isChecked) {
                            /* User clicked on a check box do some stuff */
                        	//这个方法里可以获得checkbox是第几个,通过whickButton，通过isChecked参数来判断有哪几个被选中
                        //getResources().getStringArray(R.array.***)[i];  得到选中的值
                        	if (isChecked) {
							morechecked.add(whichButton);
							DebugUtil.debug("选中的子项为", "");
						    }else {
							morechecked.remove(whichButton);
						    }
                        	String mmString="";
                        	for (int j = 0; j < morechecked.size(); j++) {
                        		//mmString = showText[j];
							}
                        }
                    });
			dialog.create();
			dialog.show();
		}
	}
	
	
	
}
