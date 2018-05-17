package com.android.hospital.asyntask;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
/**
 * 
* @ClassName: BaseAsyncTask 
* @Description: TODO(任务基类) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-18 上午8:51:33 
*
 */
public abstract class BaseAsyncTask extends AsyncTask{

	public BaseAsyncTask(){
		
	}
	
	public BaseAsyncTask(Context context){
		
	}
	
	public BaseAsyncTask(Fragment fragment){
		
	}
	public BaseAsyncTask(Activity activity,String sql){
		
	}
	
	/**
	 * 
	* @ClassName: AsyncTaskCallback 
	* @Description: TODO(异步任务回调接口) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2012-12-18 上午8:59:56 
	* 
	* @param <T>
	 */
	public interface AsyncTaskCallback<T> {
		/**
		 * 
		* @Title: dataLoaded 
		* @Description: TODO(回调方法) 
		* @param @param values    设定文件 
		* @return void    返回类型 
		* @throws
		 */
		void getSingle(T value);
		void getList(ArrayList<T> values);
	}
}
