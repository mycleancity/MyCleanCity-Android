package com.sbs.android.cleanmycity.component;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import com.sbs.android.cleanmycity.http.AppHttpDefaultDriver;
import com.sbs.android.cleanmycity.utils.AppCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;


public class AppImageView extends ImageView {

	public DownloadImage downloadImage = new DownloadImage();

	public AppImageView(Context context) {
		super(context);
	}

	public AppImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AppImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable mDrawable = getDrawable();
		if (mDrawable != null) {
			int mDrawableWidth = mDrawable.getIntrinsicWidth();
			int mDrawableHeight = mDrawable.getIntrinsicHeight();
			float actualAspect = (float) mDrawableWidth / (float) mDrawableHeight;

			// Assuming the width is ok, so we calculate the height.
			final int actualWidth = MeasureSpec.getSize(widthMeasureSpec);
			final int height = (int) (actualWidth / actualAspect);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}




	public void loadImage(String url)
	{
		try
		{
			if(downloadImage != null)
			{
				downloadImage.cancel(true);
				downloadImage = null;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		if(AppCache.isCacheImage(url))
		{
			try
			{
				this.setImageBitmap(AppCache.getCacheImage(url));
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}

		}
		else
		{
			downloadImage = new DownloadImage();
			downloadImage.execute(url, this);
		}

	}

	private static class DownloadImage extends AsyncTask<Object, Void, Bitmap> {

		private AppImageView imgView;
		private String url;




		@Override
		protected Bitmap doInBackground(Object... params) {
			url = (String)params[0];
			imgView = (AppImageView)params[1];

			Bitmap data = null;

			
			try {
				HttpUriRequest request = new HttpGet(url);
				HttpResponse response;
				response = AppHttpDefaultDriver.imageClient.execute(request);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) 
				{
					HttpEntity entity = response.getEntity();
					byte[] result = EntityUtils.toByteArray(entity);

					//Fix the missing bytes
					
					data = BitmapFactory.decodeByteArray(result, 0, result.length);
				}

			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} 
			return data;
		}

		@Override
		protected void onPostExecute(Bitmap result) {

			if(result != null)
			{
				try
				{
					imgView.setImageBitmap(result);
					AppCache.setCacheImage(url, result);
				}
				catch(Exception ex){}

			}
		}

	}



}


