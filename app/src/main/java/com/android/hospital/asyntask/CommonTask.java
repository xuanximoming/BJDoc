package com.android.hospital.asyntask;

import java.util.ArrayList;

/**
 * 
* @ClassName: CommonFragmentTask 
* @Description: TODO(共有的) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 下午3:10:23 
*
 */
public interface CommonTask<T> {

	/**
	* @Title: getValues 
	* @Description: TODO(得到数据) 
	 */
	void getValues(DataLoadedCallback<T> callback);
	
	/**
	 * 
	* @Title: SendValues 
	* @Description: TODO(发送数据) 
	 */
	void SendValues(DataLoadedCallback<T> callback);
	
	/** 
	* @Title: isLoading 
	* @Description: TODO(正在加载) 
	 */
	boolean isLoading();
	
	
	/**
	 * 
	* @ClassName: DataLoadedCallback 
	* @Description: TODO(回调接口) 
	* @author Ukey zhang yeshentianyue@sina.com
	* @date 2012-12-14 下午3:13:19 
	* 
	* @param <T>
	 */
	interface DataLoadedCallback<T> {

        void dataLoaded(ArrayList<T> values);
        void showLoading(boolean bool);
        void showReloading(boolean bool);
    }
}
