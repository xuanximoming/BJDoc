package com.android.hospital.ui.activity;

import com.android.hospital.constant.AppConstant;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.Util;
import com.github.snowdream.android.util.Log;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class TemperatureActivity extends Activity{
	
	private WebView webView;

	private String temperature;//体温
	
	private String bloodLow;//低血压
	
	private String bloodHigh;//高血压

    private static final String TAG="TemperatureActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temprature);
			
		temperature=getTemperature(AppConstant.temperatureTimeArr, AppConstant.temperatureArr);
/*		bloodLow=getBloodLow(AppConstant.BloodPressureTimeArr, AppConstant.BloodPressureArr);
		bloodHigh=getBloodHigh(AppConstant.BloodPressureTimeArr, AppConstant.BloodPressureHighArr);*/
		webView = (WebView)findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		/*webView.loadUrl("http://"+Util.getServerIP(this)+"/FlotServer/flot/flot.jsp?temperature="+temperature+"&lowpressure="+bloodLow+"&highpressure="+bloodHigh);*/
		webView.loadUrl("http://"+Util.getServerIP(this)+"/FlotServer/flot/flot.jsp?temperature="+temperature);
        Log.d(TAG,""+webView.getUrl());
    }
	
	
    public static String getTemperature(String[] tempDateArr,String[] tempValueArr){
    	StringBuffer buffer=new StringBuffer();
    	for (int i = 0; i < tempValueArr.length; i++) {
    		String utcTime=Util.converToDateUtc(tempDateArr[i]);
			if (i==0) {
				buffer.append("[ ["+utcTime+","+tempValueArr[i]+"],");
			}else if (i==(tempValueArr.length-1)) {
				buffer.append("["+utcTime+","+tempValueArr[i]+"] ]");
			}else {
				buffer.append("["+utcTime+","+tempValueArr[i]+"],");
			}
		}
    	
    	return buffer.toString();
    }
    
    public static String getBloodLow(String[] timeArr,String[] lowArr){
    	StringBuffer buffer=new StringBuffer();
    	for (int i = 0; i < lowArr.length; i++) {
    		String utcTime=Util.converToDateUtc(timeArr[i]);
			if (i==0) {
				buffer.append("[ ["+utcTime+","+lowArr[i]+"],");
			}else if (i==(lowArr.length-1)) {
				buffer.append("["+utcTime+","+lowArr[i]+"] ]");
			}else {
				buffer.append("["+utcTime+","+lowArr[i]+"],");
			}
		}
    	
    	return buffer.toString();
    }
    public static String getBloodHigh(String[] timeArr,String[] highArr){
    	StringBuffer buffer=new StringBuffer();
    	for (int i = 0; i < highArr.length; i++) {
    		String utcTime=Util.converToDateUtc(timeArr[i]);
			if (i==0) {
				buffer.append("[ ["+utcTime+","+highArr[i]+"],");
			}else if (i==(highArr.length-1)) {
				buffer.append("["+utcTime+","+highArr[i]+"] ]");
			}else {
				buffer.append("["+utcTime+","+highArr[i]+"],");
			}
		}
    	return buffer.toString();
    }
    
}
