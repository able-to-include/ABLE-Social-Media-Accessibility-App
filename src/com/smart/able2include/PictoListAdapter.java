package com.smart.able2include;
import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.services.able2includeapp.R;
import com.smart.able2include.ImageLoader;
import com.smart.network.PictoResponse;


public class PictoListAdapter extends ArrayAdapter<PictoResponse>implements OnInitListener {
    private TextToSpeech mInternalTTS = null;

    Context mContext;
    List<PictoResponse> objects;
    public ImageLoader imageLoader; 
     public PictoListAdapter(Context context, int textViewResourceId,
                              List<PictoResponse> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.objects = objects;
        this.mInternalTTS = new TextToSpeech(this.mContext, null);
        imageLoader = new ImageLoader(context);
        //this.mInternalTTS.speak("this is a long time to wait", TextToSpeech.QUEUE_FLUSH, null);
    }
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;


        if(convertView==null){

            // inflate the layout
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pictos_list_item, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();

            viewHolder.pictoImage = (ImageView) convertView.findViewById(R.id.pictoImage);
            viewHolder.pictoName = (TextView) convertView.findViewById(R.id.pictoText);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        // object item based on the position
        Log.i("EDSTAG", ">>> getView position<<< " + position);
        Log.i("EDSTAG", ">>> getView objects<<< " + objects);
        String text = objects.get(position).text;
        String uri = objects.get(position).uri;
        Log.i("EDSTAG", ">>> getView uri<<< " + uri);
        Log.i("EDSTAG", ">>> getView text<<< " + text);
 

        // assign values if the object is not null
        if((uri != null)){
            //if (text.startsWith("http://")) {
            // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
              if (uri.startsWith("http://")) {
                viewHolder.pictoName.setText(text);
               //viewHolder.pictoName.setText(req);
            } else {
            	
                viewHolder.pictoName.setText(text);
            }           
            //DisplayImage function from ImageLoader Class
            imageLoader.DisplayImage(uri, viewHolder.pictoImage);
            viewHolder.pictoImage.setOnClickListener(new MyOnClickListener(text,this.mContext,this.mInternalTTS));
        }

        return convertView;

    }

    static class ViewHolderItem {
        ImageView pictoImage;
        TextView pictoName;
    }


    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String data;
        private Context context;

        public BitmapWorkerTask(ImageView imageView,Context ctx) {
            // Use a WeakReference to ensure the ImageView can be garbage
            // collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            context = ctx;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            // Initialize transparent Bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.transparent);
            // Get pictogram URL from params (if any)
            data = params[0];
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(data)
                        .getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Return bitmap (transparent if the pictogram does not exist)
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
               	 // Toast.makeText(context,"Finished", Toast.LENGTH_SHORT).show();
               	            
                }
            }
        }
    }
    class MyOnClickListener implements OnClickListener
    {
     private final String mText;
     private Context mContext;
     private TextToSpeech mTTS;


     public MyOnClickListener(String text,  Context ctx,TextToSpeech tts)
     {
      this.mText = text;
      this.mContext = ctx;
      this.mTTS = tts;
     }

     @SuppressWarnings("deprecation")
	public void onClick(View v)
     {


    	 this.mTTS.speak(this.mText, TextToSpeech.QUEUE_FLUSH, null);
    	 
     	// Toast.makeText(mContext,this.mText, Toast.LENGTH_SHORT).show();
     }
    }
}

