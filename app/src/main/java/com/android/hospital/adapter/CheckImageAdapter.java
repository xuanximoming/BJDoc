package com.android.hospital.adapter;

import java.util.List;

import com.android.hospital.asyntask.AsyncImageLoader;
import com.android.hospital.asyntask.AsyncImageLoader.ImageCallback;
import com.android.hospital.ui.activity.R;
import com.android.hospital.util.DebugUtil;
import com.android.hospital.util.ImageUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CheckImageAdapter extends BaseAdapter{

	private Context mContext;
	
	private List<String> mList;
	
	private LayoutInflater mInflater;
	
	public CheckImageAdapter(Context context,List<String> list){
		this.mContext=context;
		this.mList=list;
		this.mInflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList==null?0:mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AsyncImageLoader imageLoader=new AsyncImageLoader();
		ViewHolder viewHolder=null;
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.activity_checkdetail_photolist_item, null);
			viewHolder=new ViewHolder();
			viewHolder.tev1=(TextView) convertView.findViewById(R.id.patient_bedno);
			viewHolder.imageView=(ImageView) convertView.findViewById(R.id.patient_title_img);
			viewHolder.progressBar=(ProgressBar) convertView.findViewById(R.id.check_detail_process);
			viewHolder.imageView.setTag(mList.get(position));
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		String path=mList.get(position);
		viewHolder.tev1.setText(path);
		DebugUtil.debug("tag标记--->"+viewHolder.imageView.getTag());
		Drawable cachedImage=imageLoader.loadDrawable(path, viewHolder.imageView, new ImageCallback() {
			
			@Override
			public void imageLoaded(Drawable imageDrawable, ImageView imageView,
					String imageUrl) {
				// TODO Auto-generated method stub
				if (imageDrawable!=null) {
					imageView.setVisibility(View.VISIBLE);
					imageView.setImageDrawable(imageDrawable);
				}else {
					imageView.setVisibility(View.INVISIBLE);
					imageView.setImageResource(R.drawable.photo_man);
				}
			}
		});
		
		if (cachedImage!=null) {
			viewHolder.imageView.setVisibility(View.VISIBLE);
			viewHolder.imageView.setImageDrawable(cachedImage);
		}else {
			viewHolder.imageView.setVisibility(View.INVISIBLE);
			viewHolder.imageView.setImageResource(R.drawable.photo_man);
		}	
		return convertView;
	}

	private class ViewHolder{
		TextView tev1;
		ImageView imageView;
		ProgressBar progressBar;
	}
}
