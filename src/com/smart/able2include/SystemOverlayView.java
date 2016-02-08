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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import com.services.able2includeapp.R;
import com.smart.network.ABLEClient;
import com.smart.network.AsyncResponse;
import com.smart.network.Constants;
import com.smart.network.JsonResponseHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.accessibilityservice.AccessibilityService;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


public class SystemOverlayView implements  OnTouchListener,OnInitListener {

    private LinearLayout mOverlay;
    private RelativeLayout mSpinner;
    private Context mServiceContext;
	private int mOriginalXPos;
	private int mOriginalYPos;
	private float mOffsetX;
	private float mOffsetY;
	private boolean mMoving;
	private long mClickTime;
	private String mFocusText;
	private int mOrientation;
	
	private WindowManager wm;
	
	//default preferences
	private String mLanguageTxt2Speech ="english";
	private String mLanguageTxt2Picto = "english";
	private String mLanguageTxt2Simple = "english";
	private String mMaxWordCount = "10";
	private String mTxt2SpeechEngine = "local";
	
	private String mPictoBase = "beta";
	private SharedPreferences mPrefMng = null;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	private Object mSyncToken = new Object();
	private SimpleTextOverlayView mSimpleTextOverlay = null;
	private PictoOverlayView mPictoOverlay = null;
    private TextToSpeech mInternalTTS = null;

    public SystemOverlayView(Context ctx)
    {
    	mServiceContext =  ctx;
        // Initializes the Text-To-Speech engine as soon as the service is connected.
    	mInternalTTS = new TextToSpeech(mServiceContext, null);
		mPrefMng = PreferenceManager
				    .getDefaultSharedPreferences(mServiceContext);
		if(mPrefMng != null)
		{
			mPictoBase = mPrefMng.getString("listpref", "Beta").toLowerCase();
			mLanguageTxt2Speech = mPrefMng.getString("listextToSpeechLang", "English").toLowerCase();
			mLanguageTxt2Picto = mPrefMng.getString("listpictoLang", "English").toLowerCase();
			mLanguageTxt2Simple = mPrefMng.getString("listsimpleTextLang", "English").toLowerCase();
			mMaxWordCount = mPrefMng.getString("maxWordCount", "20").toLowerCase();
			mTxt2SpeechEngine = mPrefMng.getString("listextToSpeechEngine", "Local").toLowerCase();
			listener =
				    new SharedPreferences.OnSharedPreferenceChangeListener() {
				  public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				    // listener implementation
    
					  if(key.contains("listpref"))
					  {
						  mPictoBase = prefs.getString(key, "Beta").toLowerCase(); 
					  }
					  else if(key.contains("listextToSpeechLang"))
					  {

							  mLanguageTxt2Speech = prefs.getString(key, "English").toLowerCase();
					  }
					  else if(key.contains("listextToSpeechEngine"))
					  {

						  mTxt2SpeechEngine = prefs.getString(key, "Local").toLowerCase();
						  if(mTxt2SpeechEngine.contains("local"))
						  {
							 mInternalTTS = new TextToSpeech(mServiceContext, null);
					  
						  }
						  else
						  {
							   	mInternalTTS = null;

						  }
					  }
					  
					  else if(key.contains("listpictoLang"))
					  {
						  mLanguageTxt2Picto = prefs.getString(key, "English").toLowerCase();
				  
					  }
					  else if(key.contains("listextToSpeechLang"))
					  {
						  mLanguageTxt2Simple = prefs.getString(key, "English").toLowerCase();
				  
					  }
					  else if(key.contains("maxWordCount"))
					  {
						  mMaxWordCount = prefs.getString(key, "20").toLowerCase();
				  
					  }
				  }
				};
				mPrefMng.registerOnSharedPreferenceChangeListener(listener);
		}
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		

			  
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getRawX();
			float y = event.getRawY();

			mMoving = false;
			int[] location = new int[2];
			mOverlay.getLocationOnScreen(location);

			mOriginalXPos = location[0];
			mOriginalYPos = location[1];

			mOffsetX = mOriginalXPos - x;
			mOffsetY = mOriginalYPos - y;
			mClickTime  = event.getEventTime();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			 //overlayActive = true;
			float x = event.getRawX();
			float y = event.getRawY();
			WindowManager.LayoutParams params = (LayoutParams) mOverlay.getLayoutParams();

			int newX = (int) (mOffsetX + x);
			int newY = (int) (mOffsetY + y);
			if (Math.abs(newX - mOriginalXPos) < 1 && Math.abs(newY - mOriginalYPos) < 1 && !mMoving) {
				return false;
			}
		    params.x = newX;
			params.y = newY;
			params.gravity = Gravity.START | Gravity.TOP;
			wm.updateViewLayout(mOverlay, params);
			mMoving = true;

		} 
		else if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
		{
			mMoving = false;

		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			if((event.getEventTime() - mClickTime) < 500)
			{
				cleanUp();
			}
		}
		return false;

	}
	
    /**
     * Create Spinner
     * 
     * 
     */
    public void createSpinnerOverlay() {
    	synchronized(mSyncToken)
    	{
    		if (mSpinner!= null) return;
    		WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
    				WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    			| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
    		params.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
    		RelativeLayout frameLayout = new RelativeLayout(mServiceContext);
            LayoutInflater inflater = (LayoutInflater) mServiceContext.getSystemService(AccessibilityService.LAYOUT_INFLATER_SERVICE);
            mSpinner = (RelativeLayout) inflater.inflate(R.layout.spinner_layout, frameLayout);
            wm = (WindowManager) mServiceContext.getSystemService(AccessibilityService.WINDOW_SERVICE);
            wm.addView(mSpinner, params);

    	}
    }
    
    public void removeSpinnerOverlay(){
    	synchronized(mSyncToken)
    	{
            if (mSpinner == null) return;
        	WindowManager wm = (WindowManager) mServiceContext.getSystemService(AccessibilityService.WINDOW_SERVICE);
        	wm.removeView(mSpinner);

        	mSpinner = null; 	
    	}
    }
	
    /**
     * Create button overlay
     * 
     * 
     */
    public void createOverlay() {
    	synchronized(mSyncToken)
    	{
        if (mOverlay != null) return;
        // Create System overlay 
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
			| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
		params.x = 30;
		params.y = 60;
		LinearLayout frameLayout = new LinearLayout(mServiceContext);
        LayoutInflater inflater = (LayoutInflater) mServiceContext.getSystemService(AccessibilityService.LAYOUT_INFLATER_SERVICE);
        mOverlay = (LinearLayout) inflater.inflate(R.layout.main_layout, frameLayout);

        ImageButton simpleTextButton = (ImageButton) frameLayout.findViewById(R.id.buttonSimpleText);
		
		
		simpleTextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	            String url;
	            String parameters;
				ABLEClient asyncTask = new ABLEClient(new AsyncResponse() {

		            @Override
		            public void processFinish(String output,ArrayList<String> request) {
		            	setFocusText("");
		            	removeSpinnerOverlay();
		            	JsonResponseHelper helper = new JsonResponseHelper(output);
              			if(output == "")
              			{
                			Toast.makeText(mServiceContext,"Error accessing service. There may a problem with connectivity.", Toast.LENGTH_LONG).show();
                			 return;
              			}
		            	if(helper.IsStatusOK())
		            	{
		            		String simpleText = helper.GetTextSimplified();
		            		if(simpleText != null)
		            		{
		            			// display the text overlay
    					    	synchronized(mSyncToken)
    					    	{
    					    			mSimpleTextOverlay = null;
    					    		
    					    	}
		        				mSimpleTextOverlay = new SimpleTextOverlayView(new OverlayClick(){
		        					  @Override
		        					  public void onClick()
		        					  {
		        					    	synchronized(mSyncToken)
		        					    	{
		        						  mSimpleTextOverlay = null;
		        					    	}
		        					  }
		        				},mServiceContext,wm,mOrientation,simpleText);
		            		}
		            	}
		            	else
		            	{
		            		// we have an issue
		            		String error = helper.GetErrorDescription();
		            		if(error != null)
		            		{
		            			// display the error message
		            			error = "Simple Text Conversion " + error;
		              			Toast.makeText(mServiceContext,error, Toast.LENGTH_LONG).show();

		            		}
		            	}
		            }
		        },getFocusText());
                url = Constants.SIMPLEXT_URL;
                try {
                	String focusText = getFocusText();
                	if(focusText != null && focusText != "")
                	{
                		parameters = "text=" + URLEncoder.encode(focusText, "UTF-8") + "&language="
					        + mLanguageTxt2Simple;
                		createSpinnerOverlay();
                		asyncTask.execute(url, parameters);
      
                	}
                	else
                	{
                  		Toast.makeText(mServiceContext,"There was no text selected", Toast.LENGTH_LONG).show();
                	}
                		

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	  
		
			}
				
		});
		ImageButton textToSpeechButton = (ImageButton) frameLayout.findViewById(R.id.buttonTextToSpeech);
		
		
		textToSpeechButton.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
	            String url;
	            String parameters;

	            if(mInternalTTS == null)
	            {
	            	ABLEClient asyncTask = new ABLEClient(new AsyncResponse() {
		            @Override
		            public void processFinish(String output,ArrayList<String> request) {
		            	setFocusText("");
		            	removeSpinnerOverlay();
		            	
		            	JsonResponseHelper helper = new JsonResponseHelper(output);
               			if(output == "")
              			{
                			Toast.makeText(mServiceContext,"Error accessing service. There may a problem with connectivity.", Toast.LENGTH_LONG).show();
                			 return;
              			}
              				
		            	if(helper.IsStatusOK())
		            	{
		
		            		String audio = helper.GetAudioSpeech();
		            		if(audio != null)
		            		{
		            			
                                try {
                                	final String passAudio = audio;
                                    new Thread(new Runnable() {
                                    	String audio;
                                        @Override
                                        public void run() {
                                        	try {
                                            MediaPlayer mediaPlayer = new MediaPlayer();
                                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                            mediaPlayer.setDataSource(passAudio);
                                            mediaPlayer.prepare(); // might take long! (for buffering, etc)
                                            mediaPlayer.start();
                                            removeSpinnerOverlay();
                                        	} catch (Exception e1) {
            									// TODO Auto-generated catch block
            					            	Toast.makeText(mServiceContext,"Error playing audio", Toast.LENGTH_LONG).show();
            									e1.printStackTrace();
            							
            								}
                                        }

                                    }).start();

								} catch (Exception e1) {
									// TODO Auto-generated catch block
									removeSpinnerOverlay();
					            	Toast.makeText(mServiceContext,"Error playing audio", Toast.LENGTH_LONG).show();
									e1.printStackTrace();
							
								}
		            		}
		            		else
		            		{
								removeSpinnerOverlay();
				            	Toast.makeText(mServiceContext,"Error receiving audio files", Toast.LENGTH_LONG).show();
 			
		            		}
		            	}
		            	else
		            	{
		            		// we have an issue
		            		removeSpinnerOverlay();
		            		String error = helper.GetErrorDescription();
		            		if(error != null)
		            		{
		            			error = "Text to Speech " + error;
		            			// display the error message
		              			Toast.makeText(mServiceContext,error, Toast.LENGTH_LONG).show();

		            		}
		            		
		            	}
		            }
		        },getFocusText());
                url = Constants.TEXT2SPEECH_URL;
                try {
                	String focusText = getFocusText();
                	
                	if(focusText != null && focusText != "")
                	{
 
					parameters = "text=" + URLEncoder.encode(focusText, "UTF-8") + "&language="
					        + mLanguageTxt2Speech;
					createSpinnerOverlay();
					asyncTask.execute(url, parameters);
                	}
                	else
                	{
                  		Toast.makeText(mServiceContext,"There was no text selected", Toast.LENGTH_LONG).show();
                	}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	            }
	            else
	            {
	                try {
	                	String focusText = getFocusText();
	                   	if(focusText != null && focusText != "")
	                	{
	                   		if(mInternalTTS != null)
	                   		{
	                   			mInternalTTS.speak(focusText.toString(), TextToSpeech.QUEUE_FLUSH, null);
	                   		}
	                	}
	                	else
	                	{
	                  		Toast.makeText(mServiceContext,"There was no text selected", Toast.LENGTH_LONG).show();
	                	}   
	            	} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
			 
			}
		});
		ImageButton textToPictoButton = (ImageButton) frameLayout.findViewById(R.id.buttonTextToPicto);
		
		
		textToPictoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	            String url;
	            String parameters;	
				ABLEClient asyncTask = new ABLEClient(new AsyncResponse() {

		            @Override
		            public void processFinish(String output,ArrayList<String> request) {
		            	setFocusText("");
		            	removeSpinnerOverlay();
		            	JsonResponseHelper helper = new JsonResponseHelper(output);
              			if(output == "")
              			{
                			Toast.makeText(mServiceContext,"Error accessing service. There may a problem with connectivity.", Toast.LENGTH_LONG).show();
                			 return;
              			}
		            	if(helper.IsStatusOK())
		            	{
			            	ArrayList<String> Pictos = new ArrayList<String>();
			            	boolean bstat = helper.GetPictos(Pictos,request);
			            	ArrayList<String> req = new ArrayList<String>();
			            	req = request;
			            	if(bstat == true)
			            	{
    					    	synchronized(mSyncToken)
    					    	{
    					    		mPictoOverlay = null;
    					    	}
			            		//we can now display
		              			mPictoOverlay = new PictoOverlayView(new OverlayClick(){
		        					  @Override
		        					  public void onClick()
		        					  {

		        					    	synchronized(mSyncToken)
		        					    	{
		        						  mPictoOverlay = null;
		        					    	}
		        					  }
		        				},mServiceContext,wm,mOrientation,req,Pictos);


	
			            	}
			            	else
			            	{
		              			Toast.makeText(mServiceContext,"Error Unable to Get Pictos", Toast.LENGTH_LONG).show();

			            	}
		            	}
		            	else
		            	{
		            		// we have an issue
		            		String error = helper.GetErrorDescription();
		            		if(error != null)
		            		{
		            			error = "Text to Pictogram " + error;
		            			// display the error message
		              			Toast.makeText(mServiceContext,error, Toast.LENGTH_LONG).show();

		            		}
		            	}
		            }
		        },getFocusText());
	               url = Constants.TEXT2PICTO_URL;
	                try {
	                	String focusText = getFocusText();
 	                	if(focusText != null && focusText != "")
	                	{
	 
                        parameters = "text=" + URLEncoder.encode(focusText, "UTF-8") + "&type="
                                + mPictoBase + "&language=" + mLanguageTxt2Picto;
                      	Log.i("EDSTAG", ">>> JSON REQ<<< " +  parameters);
                        createSpinnerOverlay();
						asyncTask.execute(url, parameters);
	                	}
	                	else
	                	{
	                  		Toast.makeText(mServiceContext,"There was no text selected", Toast.LENGTH_LONG).show();
	                	}

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	            
				}
			});

        wm = (WindowManager) mServiceContext.getSystemService(AccessibilityService.WINDOW_SERVICE);
        wm.addView(mOverlay, params);
        mOverlay.setOnTouchListener(this);
    	}

    }
    public void cleanUp()
    {
    	synchronized(mSyncToken)
    	{
    		if (mOverlay == null) return;
    		WindowManager wm = (WindowManager) mServiceContext.getSystemService(AccessibilityService.WINDOW_SERVICE);
    		wm.removeView(mOverlay);

    		mOverlay = null;
    		if(mSimpleTextOverlay != null){
    			mSimpleTextOverlay.RemoveOvelay();
    			mSimpleTextOverlay = null;
    		}

    		if(mPictoOverlay != null){
    			mPictoOverlay.RemoveOvelay();
    			mPictoOverlay = null;
    		}
    	}
    }
    public void setOrientation(int orientate )
    {
    	synchronized(mSyncToken)
    	{
    	if(orientate != mOrientation)
    	{
    		// orientation has changed
    		if(mSimpleTextOverlay != null)
    		{
    			mSimpleTextOverlay.setOrientation(orientate);
    		}
    		if(mPictoOverlay != null)
    		{
    			mPictoOverlay.setOrientation(orientate);
    		}
    	}
    	mOrientation = orientate;
    	}
    }
    
    private String trimEnd( String myString ) {

        for ( int i = myString.length() - 1; i >= 0; --i ) {
            if ( myString.charAt(i) == ' ' ) {
                continue;
            } else {
                myString = myString.substring( 0, ( i + 1 ) );
                break;
            }
        }
        return myString;
    }  
    
    public void setFocusText(String text)
    {
    	synchronized(mSyncToken)
    	{
    		if(text != null && !text.isEmpty() && text!="[]")
    		{
    			int max = Integer.parseInt(mMaxWordCount);
    			String[] wordcount = text.split("\\s+");
    			if(wordcount != null && wordcount.length < max)
    			{
    				max = wordcount.length;
    			}
    			StringBuilder strfocus = new StringBuilder();
    			if(wordcount != null)
    			{
    				for(int i = 0; i < max ; i++)
    				{
    					strfocus.append(wordcount[i]);
    					strfocus.append(" ");
    				}
    			}
    			else
    			{
    				strfocus.append("");
    			}
    			
    			text = trimEnd( strfocus.toString());
    			mFocusText = text;
  
     			Toast.makeText(mServiceContext,mFocusText, Toast.LENGTH_SHORT).show();

    		}
    	}
    }
    public String getFocusText()
    {
    	String text = "";
    	synchronized(mSyncToken)
    	{
    		text = mFocusText;
    	}
    	return text;
    }

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
}
