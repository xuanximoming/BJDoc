package com.android.hospital.ui.activity;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class StartActivity extends Activity{

	private ImageView startImg = null;
	private int onShowTime = 3000;
	private Class<?> nextUI = LoginActivity.class;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		startImg=(ImageView) findViewById(R.id.login_start);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		int sIndex = 0;
		showImg(0.2f,R.drawable.img_login_background,500);
		//showImg(0.5f,R.drawable.lurencun_logo_320dpi,onShowTime * ++sIndex);
		//showImg(0.2f,R.drawable.splash,onShowTime * ++sIndex);
		new Handler().postDelayed(new Runnable(){
			public void run() {
				startActivity(new Intent(StartActivity.this,nextUI));
				StartActivity.this.finish();
			}
		}, onShowTime * ++sIndex);
     }
	
	private void showImg(final float startAlpha,final int drawableId,int delay){
    	new Handler().postDelayed(new Runnable(){
			public void run() {
				startImg.setImageResource(drawableId);
				AlphaAnimation animation = new AlphaAnimation(startAlpha, 1.0f);
				animation.setDuration(2000);
				startImg.startAnimation(animation);
			}
		},delay);
	}
}
