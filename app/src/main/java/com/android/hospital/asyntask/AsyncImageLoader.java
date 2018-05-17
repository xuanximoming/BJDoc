package com.android.hospital.asyntask;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.android.hospital.util.ImageUtil;
import com.android.hospital.webservice.WebServiceHelper;


import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncImageLoader {
	
	 //SoftReference鏄蒋寮曠敤锛屾槸涓轰簡鏇村ソ鐨勪负浜嗙郴缁熷洖鏀跺彉閲� 
	private static HashMap<String, SoftReference<Drawable>> imageCache;  
   
	static {  
		imageCache = new HashMap<String, SoftReference<Drawable>>();  
	}
	
	public AsyncImageLoader(){
		
	}
	
	public Drawable loadDrawable(final String imageUrl,final ImageView imageView, final ImageCallback imageCallback){
		if (imageCache.containsKey(imageUrl)) {  
			//浠庣紦瀛樹腑鑾峰彇  
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);  
			Drawable drawable = softReference.get();  
			System.out.println("111111111111111111111111111111");  
			 if (drawable != null) {  
				 System.out.println("11111111111122222222211111111111111111");  
				 return drawable;  
			 }
		}
		final Handler handler = new Handler() {  
			@Override
			public void handleMessage(Message msg) {
				imageCallback.imageLoaded((Drawable) msg.obj, imageView,imageUrl);
			}
		};
		//寤虹珛鏂颁竴涓柊鐨勭嚎绋嬩笅杞藉浘鐗� 
		new Thread(){
			public void run() {
				 System.out.println("11111111111133333333333211111111111111111");
				 Drawable drawable = null;  
				 try {
					 drawable = WebServiceHelper.getWebServiceImage(imageUrl, imageUrl);//5琛ㄧず鍦嗚瑙掑害澶у皬 
				} catch (Exception e) {
					// TODO: handle exception
				}
				 imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));  
				 Message message = handler.obtainMessage(0, drawable);  
				 handler.sendMessage(message);  
			}
		}.start();
		return null; 
	}
		
	
	//鍥炶皟鎺ュ彛  
	public interface ImageCallback {  
		public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl);
	}
}
