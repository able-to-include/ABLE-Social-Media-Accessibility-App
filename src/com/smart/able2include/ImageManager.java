package com.smart.able2include;
/*
<license>
	<name> Apache License, Version 2.0 </name>
	<url> http://www.apache.org/licenses/LICENSE-2.0 </url>
	<comments> 
		The work represented by this file is partially funded by the ABLE-TO-INCLUDE project through the 
		European Commission's ICT Policy Support Programme as part of the Competitiveness & Innovation 
		Programme (Grant no.: 621055)
		Copyright © 2016, ABLE-TO-INCLUDE Consortium.
		Licensed under the Apache License, Version 2.0 (the "License");
		you may not use this file except in compliance with the License.
		You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
		Unless required by applicable law or agreed to in writing, software distributed under the License 
		is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
		implied.
		See the License for the specific language governing permissions & limitations under the License.
	</comments>
</license>
*/
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageManager {

	private long cacheDuration;
	private SimpleDateFormat mDateFormatter;

	private HashMap<String, SoftReference<Bitmap>> imageMap = new HashMap<String, SoftReference<Bitmap>>();

	private File cacheDir;
	private ImageQueue imageQueue = new ImageQueue();
	private Thread imageLoaderThread = new Thread(new ImageQueueManager());

	public ImageManager(Context context, long _cacheDuration) {

		cacheDuration = _cacheDuration;
		//TODO Make this match the date format of your server
		mDateFormatter = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss zzz");

		// Make background thread low priority, to avoid affecting UI performance
		imageLoaderThread.setPriority(Thread.NORM_PRIORITY-1);

		// Find the dir to save cached images
		String sdState = android.os.Environment.getExternalStorageState();
		if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = android.os.Environment.getExternalStorageDirectory();		
			cacheDir = new File(sdDir,"data/codehenge");

		} else {
			cacheDir = context.getCacheDir();
		}

		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}

	public void displayImage(String url, ImageView imageView, int defaultDrawableId) {

		if(imageMap.containsKey(url)) {
			imageView.setImageBitmap(imageMap.get(url).get());

		} else {
			queueImage(url, imageView, defaultDrawableId);
			imageView.setImageResource(defaultDrawableId);
		}
	}

	private void queueImage(String url, ImageView imageView, int defaultDrawableId) {
		// This ImageView might have been used for other images, so we clear 
		// the queue of old tasks before starting.
		imageQueue.Clean(imageView);
		ImageRef p = new ImageRef(url, imageView, defaultDrawableId);

		synchronized(imageQueue.imageRefs) {
			imageQueue.imageRefs.push(p);
			imageQueue.imageRefs.notifyAll();
		}

		// Start thread if it's not started yet
		if(imageLoaderThread.getState() == Thread.State.NEW) {
			imageLoaderThread.start();
		}
	}

	private Bitmap getBitmap(String url) {

		try {
			URLConnection openConnection = new URL(url).openConnection();

			String filename = String.valueOf(url.hashCode());

			File bitmapFile = new File(cacheDir, filename);
			Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getPath());

			long currentTimeMillis = System.currentTimeMillis();
			
			// Is the bitmap in our cache?
			if (bitmap != null) {
				
				// Has it expired?
				long bitmapTimeMillis = bitmapFile.lastModified();
				if ( (currentTimeMillis - bitmapTimeMillis) < cacheDuration ) {
					return bitmap;
				}
				
				// Check also if it was modified on the server before downloading it
				String lastMod = openConnection.getHeaderField("Last-Modified");
				long lastModTimeMillis = mDateFormatter.parse(lastMod).getTime();
				
				if ( lastModTimeMillis <= bitmapTimeMillis ) {
					
					//Discard the connection and return the cached version
					return bitmap;
				}
			}
				
			// Nope, have to download it
			bitmap = BitmapFactory.decodeStream(openConnection.getInputStream());
			// save bitmap to cache for later
			writeFile(bitmap, bitmapFile);
			
			return bitmap;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private void writeFile(Bitmap bmp, File f) {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally { 
			try { if (out != null ) out.close(); }
			catch(Exception ex) {} 
		}
	}

	/** Classes **/

	private class ImageRef {
		public String url;
		public ImageView imageView;
		public int defDrawableId;

		public ImageRef(String u, ImageView i, int defaultDrawableId) {
			url=u;
			imageView=i;
			defDrawableId = defaultDrawableId;
		}
	}

	//stores list of images to download
	private class ImageQueue {
		private Stack<ImageRef> imageRefs = 
				new Stack<ImageRef>();

		//removes all instances of this ImageView
		public void Clean(ImageView view) {

			for(int i = 0 ;i < imageRefs.size();) {
				if(imageRefs.get(i).imageView == view)
					imageRefs.remove(i);
				else ++i;
			}
		}
	}

	private class ImageQueueManager implements Runnable {
		
		private Context context;
		@Override
		public void run() {
			try {
				while(true) {
					// Thread waits until there are images in the 
					// queue to be retrieved
					if(imageQueue.imageRefs.size() == 0) {
						synchronized(imageQueue.imageRefs) {
							imageQueue.imageRefs.wait();
						}
					}

					// When we have images to be loaded
					if(imageQueue.imageRefs.size() != 0) {
						ImageRef imageToLoad;

						synchronized(imageQueue.imageRefs) {
							imageToLoad = imageQueue.imageRefs.pop();
						}

						Bitmap bmp = getBitmap(imageToLoad.url);
						imageMap.put(imageToLoad.url, new SoftReference<Bitmap>(bmp));
						Object tag = imageToLoad.imageView.getTag();

						// Make sure we have the right view - thread safety defender
						if(tag != null && ((String)tag).equals(imageToLoad.url)) {
							BitmapDisplayer bmpDisplayer = 
									new BitmapDisplayer(bmp, imageToLoad.imageView, imageToLoad.defDrawableId);
						
							bmpDisplayer.run();
						}
					}

					if(Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {}
		}
	}

	//Used to display bitmap in the UI thread
	private class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;
		int defDrawableId;

		public BitmapDisplayer(Bitmap b, ImageView i, int defaultDrawableId) {
			bitmap=b;
			imageView=i;
			defDrawableId = defaultDrawableId;
		}

		public void run() {
			if(bitmap != null)
				imageView.setImageBitmap(bitmap);
			else
				imageView.setImageResource(defDrawableId);
		}
	}
}