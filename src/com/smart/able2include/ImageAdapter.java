package com.smart.able2include;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	ArrayList<String> itemList = new ArrayList<String>();

	public ImageAdapter(Context c) {
		mContext = c;
	}

	void add(String path) {
		itemList.add(path);
	}
	
	void clear() {
		itemList.clear();
	}
	
	void remove(int index){
		itemList.remove(index);
	}

	@Override
	public int getCount() {
		return itemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	//getView load bitmap in AsyncTask
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
			
			convertView = imageView;
			
			holder = new ViewHolder();
			holder.image = imageView;
			holder.position = position;
			convertView.setTag(holder);
		} else {
			//imageView = (ImageView) convertView;
			holder = (ViewHolder) convertView.getTag();
			((ImageView)convertView).setImageBitmap(null);
		}
		
		//Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
		// Using an AsyncTask to load the slow images in a background thread
		new AsyncTask<ViewHolder, Void, Bitmap>() {
		    private ViewHolder v;

		    @Override
		    protected Bitmap doInBackground(ViewHolder... params) {
		        v = params[0];
		        Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
		        
		        //Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 100, 100);
		        return bm;
		    }

		    @Override
		    protected void onPostExecute(Bitmap result) {
		        super.onPostExecute(result);
		        //Not work for me!
		        /*
		        if (v.position == position) {
		            // If this item hasn't been recycled already, 
		        	// show the image
		            v.image.setImageBitmap(result);
		        }
		        */

		        v.image.setImageBitmap(result);

		    }
		}.execute(holder);
		return convertView;
	}

	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
			int reqHeight) {

		Bitmap bm = null;
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;
	}

	public int calculateInSampleSize(

	BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height
						/ (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}

		return inSampleSize;
	}
	
	class ViewHolder {
        ImageView image;
        int position;
    }

}

