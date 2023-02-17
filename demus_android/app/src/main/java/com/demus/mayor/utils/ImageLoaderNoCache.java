package com.demus.mayor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.demus.mayor.R;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ImageLoaderNoCache {

	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;
	
	// Handler to display images in UI thread
	Handler handler = new Handler();
	
	Context context;

	public ImageLoaderNoCache(Context context) {
		executorService = Executors.newFixedThreadPool(5);
		this.context = context;
	}

	public void DisplayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		
		queuePhoto(url, imageView);
	}

	public void DisplayImage(String url, ImageView imageView, ProgressBar progressBar) {
		imageViews.put(imageView, url);
		
		queuePhoto(url, imageView, progressBar);
		progressBar.setVisibility(View.VISIBLE);
	}
	
	public boolean storeImage(String url, Context context) {
		Bitmap bitmap = getBitmap(url);
		
		if(bitmap != null) {
			try {
				String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dakinda";
				File dir = new File(file_path);
				if(!dir.exists())
					dir.mkdirs();
				
				File file = new File(dir, UUID.randomUUID().toString() + ".png");
				FileOutputStream fOut = new FileOutputStream(file);
				
				bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
				fOut.flush();
				fOut.close();
				
				MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
				
				return true;
			}
			catch (Exception e) {
				return false;
			}
		}
		
		return false;
	}
	
	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private void queuePhoto(String url, ImageView imageView, ProgressBar progressBar) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, progressBar);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = null;
		try {
			f = File.createTempFile(url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.')), url.substring(url.lastIndexOf('.')));
		} catch (IOException e) {
			//e.printStackTrace();
		}

		// Download Images from the Internet
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			conn.setInstanceFollowRedirects(true);
			
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 206) {
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				Utility.CopyStream(is, os);
				
				os.close();
				conn.disconnect();
				bitmap = decodeFile(f);
				
				return bitmap;
			}
			else
				return null;
		} catch (Throwable ex) {
			//ex.printStackTrace();
			
			return null;
		}
	}

	// Decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			int scale = 1;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			
			stream2.close();
			
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public ProgressBar progressBar;
		
		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}

		public PhotoToLoad(String u, ImageView i, ProgressBar p) {
			url = u;
			imageView = i;
			progressBar = p;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				
				Bitmap bmp = getBitmap(photoToLoad.url);
				
				if (imageViewReused(photoToLoad))
					return;
				
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			
			if(photoToLoad.progressBar != null)
				photoToLoad.progressBar.setVisibility(View.GONE);
			
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				Toast.makeText(context, R.string.no_image, Toast.LENGTH_LONG).show();
		}
	}
}