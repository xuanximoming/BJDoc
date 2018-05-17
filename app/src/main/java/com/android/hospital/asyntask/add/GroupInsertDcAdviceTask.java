package com.android.hospital.asyntask.add;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.android.hospital.asyntask.BaseAsyncTask;
import com.android.hospital.webservice.WebServiceHelper;
/**
 * 
* @ClassName: GroupInsertDcAdviceTask 
* @Description: TODO(套餐医嘱插入任务) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-22 下午4:06:54 
*
 */
public class GroupInsertDcAdviceTask extends BaseAsyncTask{

	private Activity mActivity;
	
	private String sql;
	
	public GroupInsertDcAdviceTask(Activity activity,String sql){
		this.mActivity=activity;
		this.sql=sql;
	}
	
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		return WebServiceHelper.insertWebServiceData(sql);
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub

		boolean flag=(Boolean) result;
		if (flag) {
			Intent intent=new Intent();
			mActivity.setResult(11, intent);
			mActivity.finish();
		}else {
			Toast.makeText(mActivity, "提交失败,请检查网络!", Toast.LENGTH_SHORT).show();
		}
	}
}
