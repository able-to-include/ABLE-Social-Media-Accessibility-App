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
import org.json.JSONException;
import org.json.JSONObject;

import com.services.able2includeapp.R;
import com.smart.network.AsyncResponse;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SimpleTextOverlayView extends View implements OnInitListener{
	private LinearLayout mLandscapeView;
	private LinearLayout myPortraitView;
	private Context mServiceContext;
	private WindowManager wm;
    private boolean mOnTop = false;
    private int mLastOrientation = 0;
    private RelativeLayout frameLayout;
    private Button dialogButton;
    private String mSimpleText;
    private TextView text;
    private Object mSyncToken = new Object();
    public OverlayClick delegate = null;//Call back interface
    private TextToSpeech mInternalTTS;
    private Boolean btoggle;
	public SimpleTextOverlayView(OverlayClick click,Context context,WindowManager mng,int Orientation,String texttoDisplay) {
		super(context);
		mServiceContext = context;
		wm = mng;
		mSimpleText = texttoDisplay;
		  this.mInternalTTS = new TextToSpeech(this.mServiceContext, null);
		delegate = click;
		btoggle = false;
		createView();

	}
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
	  private void createView() {
			WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
							| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
			
			params.gravity =  Gravity.START | Gravity.TOP;
			params.x =0;
			params.y = 30;
			params.width = wm.getDefaultDisplay().getWidth();
			params.height  = wm.getDefaultDisplay().getHeight()-30;
		

	        frameLayout = new RelativeLayout(mServiceContext);
	        LayoutInflater layoutInflater = (LayoutInflater) mServiceContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        // Here is the place where you can inject whatever layout you want.
	        layoutInflater.inflate(R.layout.simpletxt_layout, frameLayout);
		
		 text = (TextView) frameLayout.findViewById(R.id.textViewSimplifiedText);
		    text.setText(mSimpleText);
		    text.setOnClickListener(new OnClickListener(){
	 			@SuppressWarnings("deprecation")
				@Override
	 			public void onClick(View v) {
	 		
	 			    
	 				mInternalTTS.speak(mSimpleText, TextToSpeech.QUEUE_FLUSH, null);
	 				if(btoggle == false)
	 				{
	 			     text.setTextColor(getResources().getColor(R.color.colorPrimary));
	 				}
	 				else
	 				{
	 					text.setTextColor(Color.WHITE);	
	 				}
	 				btoggle = !btoggle;
	  				}	
			});
			 dialogButton = (Button) frameLayout.findViewById(R.id.simpleButtonOK);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RemoveOvelay();
					delegate.onClick();
				}
			});
	    	synchronized(mSyncToken)
	    	{
			wm.addView(frameLayout, params);
			mOnTop = true;
	    	}
	    }
	public void setOrientation(int Orientation)
	{
		boolean onTop = false;
    	synchronized(mSyncToken)
    	{
    		onTop =mOnTop;
    	}
		if(onTop == true)
		{
			RemoveOvelay();
			createView();
		}
		mLastOrientation = Orientation;

	}
	
	public void RemoveOvelay()
	{
    	synchronized(mSyncToken)
    	{
    		if(mOnTop == true)
    		{
    			wm.removeView(frameLayout);
    			mOnTop = false;
    		}
    	}
	}
	public void CreateOverlay()
	{
		setOrientation(mLastOrientation);
	}
}
