package com.android.hospital.widgets;

import android.app.ProgressDialog;
import android.content.Context;
/**
 *
 * @ClassName: MyProssDialog
 * @Description: TODO(自定义的dialog)
 * @author Ukey zhang yeshentianyue@sina.com
 * @date 2012-12-18 下午11:20:07
 *
 */
public class MyProssDialog {

	ProgressDialog mDialog;

	public MyProssDialog(Context context,String title,String content){

		mDialog=new ProgressDialog(context);
		mDialog.setTitle(title);
		mDialog.setMessage(content);
		mDialog.show();
	}

	public void cancel(){
		mDialog.cancel();
	}

	public void setCancelable(){
		mDialog.setCancelable(false);
	}
}
