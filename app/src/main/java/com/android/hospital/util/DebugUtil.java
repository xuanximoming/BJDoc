package com.android.hospital.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
/**
 * 
* @ClassName: DebugUtil 
* @Description: TODO(log工具类) 
* @author Ukey zhang yeshentianyue@sina.com
* @date 2012-12-14 上午11:40:01 
*
 */
public class DebugUtil {
    public static final String TAG = "DebugUtil";
    public static final String INFO = "Info";
    public static final boolean DEBUG = true;
     
    public static void toast(Context context,String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
     
    public static void debug(String tag,String msg){
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }
     
    public static void debug(String msg){
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }
     
    public static void error(String tag,String error){
        Log.e(tag, error);
    }
     
    public static void error(String error){
        Log.e(TAG, error);
    }
    
    public static void info(String info){
    	Log.i(INFO, info);
    }
}
