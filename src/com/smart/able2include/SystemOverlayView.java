package com.smart.able2include;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.services.able2includeapp.R;
import com.smart.network.ABLEClient;
import com.smart.network.AsyncResponse;
import com.smart.network.Constants;
import com.smart.network.JsonResponseHelper;
import com.smart.network.PictoResponse;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
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
import android.view.accessibility.AccessibilityNodeInfo;
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
	private String mLanguageTxt2Speech ="english";
	private String mLanguageTxt2Picto = "english";
	private String mLanguageTxt2Simple = "english";
	private String mMaxWordCount = "10";
	private String mMaxWordTime = "10";
	private String mTxt2SpeechEngine = "local";
	
	

	

	private PictogramSingleton mParser = null;
	private String mPictoBase = "beta";
	private SharedPreferences mPrefMng = null;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	private Object mSyncToken = new Object();
	private SimpleTextOverlayView mSimpleTextOverlay = null;
	private PictoOverlayView mPictoOverlay = null;
	private PictoTextOverlayView mTextPictoOverlay = null;
	private AccessibilityNodeInfo msourceEdit = null;
    private TextToSpeech mInternalTTS = null;
    private Timer timerFocus = null;
    private TimerTask tt     = null;
    String languageEnglish[] = {"english","inglés","anglais","engels"};
    String languageSpanish[] = {"spanish","español","espanol","spaans"};
    String languageDutch[] = {"dutch","nederlands","néerlandais","holandés"};
    String languageFrench[] = {"french","francés","frans","français"};  
    
    public SystemOverlayView(Context ctx)
    {
    	mServiceContext =  ctx;
   // 	Toast.makeText(mServiceContext,"mInternalTTS create before", Toast.LENGTH_LONG).show();
    	mParser = PictogramSingleton.getInstance(ctx);
    
   	//Locale current = getResources().getConfiguration().locale;
        // Initializes the Text-To-Speech engine as soon as the service is connected.
    	mInternalTTS = new TextToSpeech(mServiceContext, null);
    	//mInternalTTS.setLanguage(Locale.);
   // Toast.makeText(mServiceContext,"mInternalTTS create after", Toast.LENGTH_LONG).show();

		mPrefMng = PreferenceManager
				    .getDefaultSharedPreferences(mServiceContext);
		//.findPreference("list box preference key").setEnabled(isEnabled);
		try
		{
		if(mPrefMng != null)
		{
			mPictoBase = mPrefMng.getString("listpref", "Beta").toLowerCase();
			mLanguageTxt2Speech = mPrefMng.getString("listextToSpeechLang", "").toLowerCase();
			mLanguageTxt2Speech = selectLanguage(mLanguageTxt2Speech);
			mLanguageTxt2Picto = mPrefMng.getString("listpictoLang", "").toLowerCase();
			mLanguageTxt2Picto = selectLanguage(mLanguageTxt2Picto);
			mLanguageTxt2Simple = mPrefMng.getString("listsimpleTextLang", "").toLowerCase();
			mLanguageTxt2Simple = selectLanguage(mLanguageTxt2Simple);
			mMaxWordCount = mPrefMng.getString("maxWordCount", "20").toLowerCase();
			mMaxWordTime  = mPrefMng.getString("maxWordTime", "20").toLowerCase();
			mTxt2SpeechEngine = mPrefMng.getString("listextToSpeechEngine", "Local").toLowerCase();
			if(mParser != null)
			{
				mParser.setPictoBase(mPictoBase.toLowerCase(),mPrefMng);
			}
		/*	Set<String> selections = mPrefMng.getStringSet("multiselectlistCategoryClothes", null);
			String[] selected= selections.toArray(new String[] {});
	        for (int i = 0; i < selected.length ; i++){
	        	Toast.makeText(mServiceContext, selected[i], Toast.LENGTH_LONG).show();
	        }
			*/
			//Toast.makeText(mServiceContext,mPrefMng.getString("multiselectlistCategoryClothes", "").toLowerCase(), Toast.LENGTH_LONG).show();
			listener =
				    new SharedPreferences.OnSharedPreferenceChangeListener() {
				    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				    // listener implementation
    
					  if(key.contains("listpref"))
					  {
						  mPictoBase = prefs.getString(key, "Beta").toLowerCase(); 
						  if(mParser != null)
						  {
							  mParser.setPictoBase(mPictoBase.toLowerCase(),mPrefMng);
						  }
					  }
					  else if(key.contains("listextToSpeechLang"))
					  {

							  mLanguageTxt2Speech = prefs.getString(key, "").toLowerCase();
							  mLanguageTxt2Speech = selectLanguage(mLanguageTxt2Speech);
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
						  mLanguageTxt2Picto = prefs.getString(key, "").toLowerCase();
						  mLanguageTxt2Picto = selectLanguage(mLanguageTxt2Picto);
				  
					  }
					  else if(key.contains("listextToSpeechLang"))
					  {
						  mLanguageTxt2Simple = prefs.getString(key, "").toLowerCase();
						  mLanguageTxt2Simple = selectLanguage(mLanguageTxt2Simple);
				  
					  }
					  else if(key.contains("maxWordCount"))
					  {
						  mMaxWordCount = prefs.getString(key, "20").toLowerCase();
				  
					  }
					  else if(key.contains("maxWordTime"))
					  {
						  mMaxWordTime = prefs.getString(key, "20").toLowerCase();
				  
					  }
					  else
					  {
						 // everything else category selections
						  if(mParser != null)
						  {
							  
							  
						  }
						  
					  }
					  
				  }
				};
				mPrefMng.registerOnSharedPreferenceChangeListener(listener);
		}
		}catch (ClassCastException ignored) {
        }	
    }
    private String selectLanguage(String lang)
    {
    	
    	
		if(Utils.stringContainsItemFromList(lang, languageEnglish))
		{
			lang = "english";
		}
		else if(Utils.stringContainsItemFromList(lang, languageSpanish))
		{
			lang = "spanish";			
		}
		else if(Utils.stringContainsItemFromList(lang, languageDutch))
		{
			lang = "dutch";
		}
		else if(Utils.stringContainsItemFromList(lang, languageFrench))
		{
			lang = "french";
		}
		else
		{
			lang = "english";
		}
    	
    	
    	return lang;
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
			//Toast.makeText(mServiceContext,"ACTION_DOWN", Toast.LENGTH_SHORT).show();

		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			 //overlayActive = true;
			float x = event.getRawX();
			float y = event.getRawY();
			//Toast.makeText(mServiceContext,"ACTION_MOVE", Toast.LENGTH_SHORT).show();

			WindowManager.LayoutParams params = (LayoutParams) mOverlay.getLayoutParams();

			int newX = (int) (mOffsetX + x);
			int newY = (int) (mOffsetY + y);
			if (Math.abs(newX - mOriginalXPos) < 1 && Math.abs(newY - mOriginalYPos) < 1 && !mMoving) {
				//Toast.makeText(mServiceContext,"ACTION_MOVE FALSE", Toast.LENGTH_SHORT).show();

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
			//Toast.makeText(mServiceContext,"ACTION_Up", Toast.LENGTH_SHORT).show();
			if((event.getEventTime() - mClickTime) < 500)
			{
				cleanUp(false);
			}
		/*if (mMoving == false) {
			cleanUp();
					return true;
			}*/
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
    	String text = getFocusText();
    	if(text != null && !text.isEmpty()) 
    	{
    	synchronized(mSyncToken)
    	{
        if (mOverlay != null) return;

        // Create System overlay video
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
			| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
		/*WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
			| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.START | Gravity.TOP;
*/
		//params.x  = mServiceContext.getResources().getDisplayMetrics().widthPixels;
		//params.y  = (mServiceContext.getResources().getDisplayMetrics().heightPixels)/2;
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
		            	resetFocusText();
		            	removeSpinnerOverlay();
		            	JsonResponseHelper helper = new JsonResponseHelper(output);
              			if(output == "")
              			{
                			Toast.makeText(mServiceContext,R.string.ableConnect, Toast.LENGTH_LONG).show();
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
		            		else
		            		{
		              			Toast.makeText(mServiceContext,R.string.ableErrorSimplify , Toast.LENGTH_LONG).show();
		            			
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
                		//Toast.makeText(mServiceContext,parameters, Toast.LENGTH_LONG);
                		  Log.i("EDSTAG", ">>> parameters <<< " +  parameters);
                		createSpinnerOverlay();
                		asyncTask.execute(url, parameters);
      
                	}
                	else
                	{
                  		Toast.makeText(mServiceContext,R.string.errorNoText, Toast.LENGTH_LONG).show();
                	}
                		

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	  
		
			}
				
		});
		ImageButton pitcoToTextButton = null;
	   // if ((Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP)||(msourceEdit == null)) {

	    if ((Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP)||(msourceEdit == null)) {

		View pitcoToTextView = (View)frameLayout.findViewById(R.id.viewTextToPicto);
		pitcoToTextView.setVisibility(View.GONE);
		 pitcoToTextButton = (ImageButton)frameLayout.findViewById(R.id.buttonPictotoText);
		pitcoToTextButton.setVisibility(View.GONE);
	    }
	    else
	    {
			View pitcoToTextView = (View)frameLayout.findViewById(R.id.viewTextToPicto);
			pitcoToTextView.setVisibility(View.VISIBLE);
		 pitcoToTextButton = (ImageButton)frameLayout.findViewById(R.id.buttonPictotoText);
			pitcoToTextButton.setVisibility(View.VISIBLE);
	    }
		
		ImageButton textToSpeechButton = (ImageButton) frameLayout.findViewById(R.id.buttonTextToSpeech);
	//	textToSpeechButton.setVisibility(View.GONE);

		
		textToSpeechButton.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
	            String url;
	            String parameters;

	            if(mInternalTTS == null)
	            {
	            	//Toast.makeText(mServiceContext,"mInternalTTS 1", Toast.LENGTH_LONG).show();
	            	ABLEClient asyncTask = new ABLEClient(new AsyncResponse() {

		            @Override
		            public void processFinish(String output,ArrayList<String> request) {
		            	resetFocusText();
		            	removeSpinnerOverlay();
		            	
		            	JsonResponseHelper helper = new JsonResponseHelper(output);
              		//	Toast.makeText(mServiceContext,output, Toast.LENGTH_LONG).show();
              			if(output == "")
              			{
                			Toast.makeText(mServiceContext,R.string.ableConnect, Toast.LENGTH_LONG).show();
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
                                            // DO your work here
                                            // get the data
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
		            			error = R.string.errorTxtToSpeech + " " + error;
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
	        		//Toast.makeText(mServiceContext,url + " " + parameters, Toast.LENGTH_LONG).show();

					asyncTask.execute(url, parameters);
                	}
                	else
                	{
                  		Toast.makeText(mServiceContext,R.string.errorNoText, Toast.LENGTH_LONG).show();
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
	                	//Toast.makeText(mServiceContext,"mInternalTTS 2", Toast.LENGTH_LONG).show();
	            		//Toast.makeText(mServiceContext,"speak", Toast.LENGTH_LONG).show();

	                   	if(focusText != null && focusText != "")
	                	{
	                   		if(mInternalTTS != null)
	                   		{
	                   			mInternalTTS.speak(focusText.toString(), TextToSpeech.QUEUE_FLUSH, null);
	                   			resetFocusText();
	                   		}
	                	}
	                	else
	                	{
	                  		Toast.makeText(mServiceContext,R.string.errorNoText, Toast.LENGTH_LONG).show();
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
		            	resetFocusText();
		            	removeSpinnerOverlay();
		            	JsonResponseHelper helper = new JsonResponseHelper(output);
              			if(output == "")
              			{
                			Toast.makeText(mServiceContext,R.string.ableConnect, Toast.LENGTH_LONG).show();
                			 return;
              			}
		            	if(helper.IsStatusOK())
		            	{
			            	/*ArrayList<String> Pictos = new ArrayList<String>();
			            	boolean bstat = helper.GetPictos(Pictos,request);
*/
		            		ArrayList<PictoResponse> Pictos = new ArrayList<PictoResponse>();
			            	ArrayList<String> req = new ArrayList<String>();
			            	req = request;
			            	boolean bstat = helper.GetPictos(Pictos);
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
		        				},mServiceContext,wm,mOrientation,Pictos,req);


	
			            	}
			            	else
			            	{
			            		StringBuilder text = new StringBuilder();
			            		text.append("[");
			            		for (int i = 0; i < req.size(); i++) {
			            			text.append(req.get(i));
			            			text.append(" ");

			            		}
			            		text.append("]");
			            		
		              			Toast.makeText(mServiceContext,R.string.errorPictos  + " " + text.toString(), Toast.LENGTH_LONG).show();

			            	}
		            	}
		            	else
		            	{
		            		// we have an issue
		            		String error = helper.GetErrorDescription();
		            		if(error != null)
		            		{
		            			error = R.string.errorTxtToPicto + error;
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
 
					/*parameters = "text=" + URLEncoder.encode(focusText, "UTF-8") + "&type="
					        + mPictoBase + "&language=" + mLanguageTxt2Picto;
					        old api version 3
					        */
						
						//update the api to allow the return of the words sent to server
					parameters = "text=" + URLEncoder.encode(focusText, "UTF-8") + "&type="
					            + mPictoBase + "&language=" + mLanguageTxt2Picto + "&parallel=" + "true";		
               
					createSpinnerOverlay();
					asyncTask.execute(url, parameters);
					}
					else
					{
						Toast.makeText(mServiceContext,R.string.errorNoText, Toast.LENGTH_LONG).show();
						//PictoTextOverlayView texter  = new PictoTextOverlayView(mServiceContext,wm);
					}
	            	} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		pitcoToTextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
      			//Toast.makeText(mServiceContext,"Hello there", Toast.LENGTH_LONG).show();
      			mTextPictoOverlay =  new PictoTextOverlayView(new OverlayClick(){
					  @Override
					  public void onClick()
					  {

					    	synchronized(mSyncToken)
					    	{
					    		if(msourceEdit != null)
					    		{
				      	    	    Bundle arguments = new Bundle();
			        	    	    arguments.putCharSequence(AccessibilityNodeInfo
			        	    	            .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, mTextPictoOverlay.getWordText());
			        	    	    msourceEdit.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
					    		
			        	    	   /* public void inputData(Context c, String data, AccessibilityNodeInfo source) {
			        	    	        try {

			        	    	                String lastClip = clipboard.getPrimaryClip().getItemAt(0).coerceToText(c)
			        	    	                        .toString();
			        	    	            } catch (Exception e) {
			        	    	                lastClip = "";
			        	    	            }
			        	    	            Log.d("THE NODE INFO", source.toString());

			        	    	            ClipData clip = ClipData.newPlainText("nfc_input", data);
			        	    	            clipboard.setPrimaryClip(clip);

			        	    	            Log.d("SENDING DATA", Boolean.toString(source.refresh()));
			        	    	            Log.d("SENDING DATA", Boolean.toString(source
			        	    	                    .performAction(AccessibilityNodeInfo.ACTION_PASTE)));
			        	    	            ClipData clip = ClipData.newPlainText("nfc_input", lastClip);
			        	    	            clipboard.setPrimaryClip(clip);
			        	    	    }*/

					    		
					    		}
					    		
					    		
					    		mTextPictoOverlay = null;
					    		
					    	}
					  }
				},mServiceContext,wm,mLanguageTxt2Picto);
      			//mTextPictoOverlay.setParent();

			}
			
		});
        wm = (WindowManager) mServiceContext.getSystemService(AccessibilityService.WINDOW_SERVICE);
        wm.addView(mOverlay, params);
        mOverlay.setOnTouchListener(this);
    	}
    	}
    }
    public void cleanUp(Boolean bForce)
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
if((mTextPictoOverlay != null)&&(bForce)){
	mTextPictoOverlay.RemoveOvelay();
	mTextPictoOverlay = null;
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
    		if(mTextPictoOverlay != null)
    		{
    			mTextPictoOverlay.setOrientation(orientate);
    		}
    	}
    	mOrientation = orientate;
    	}
    }
    public String trimEnd( String myString ) {

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
    public void resetFocusText()
    {
    	synchronized(mSyncToken)
    	{
    		mFocusText = "";
    	}
    	cleanUp(false);
    }
    public void setFocusText(String text)
    {
    	setFocusText( text,true);
    }
    public void setFocusInput(AccessibilityNodeInfo source)
    {
		//Toast.makeText(mServiceContext,"1", Toast.LENGTH_LONG).show();


	    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
	    	//if(msourceEdit == null)
	    	//{
				msourceEdit = source;
		    	if(mOverlay != null)
		    	{
		    		cleanUp(false);
		    		createOverlay();
				}
		    	else
		    	{
		    		createOverlay();
		    	}
	    	//}

	
	    }
	    else
	    {
	    	if(msourceEdit != null)
	    	{
		    	msourceEdit = null;
		    	if(mOverlay != null)
		    	{
		    		cleanUp(false);
		    		createOverlay();
				}
		    	else
		    	{
		    		createOverlay();
		    	}
	    	}


			/*View pitcoToTextView = (View)frameLayout.findViewById(R.id.viewTextToPicto);
			pitcoToTextView.setVisibility(View.GONE);
			ImageButton pitcoToTextButton = (ImageButton)frameLayout.findViewById(R.id.buttonPictotoText);
			pitcoToTextButton.setVisibility(View.GONE);*/
	    }
  	
    	
    }
    public void setFocusText(String text,Boolean bshow)
    {
    	int timeout = 20;
    	synchronized(mSyncToken)
    	{
    		if(text != null && !text.isEmpty() && text!="[]")
    		{
    			int max = Integer.parseInt(mMaxWordCount);
    		    timeout = Integer.parseInt(mMaxWordTime);
    		    timeout *= 1000;

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
    			if(mFocusText!= null && mFocusText.equalsIgnoreCase(text))
    			{
    				bshow = false;
    			}
    			mFocusText = text;
    			if(bshow)
    			{
    				Toast.makeText(mServiceContext,mFocusText, Toast.LENGTH_SHORT).show();
    			}
     	
    		}
    	}
   		if(text != null && !text.isEmpty() && text!="[]")
		{
   			if(timerFocus != null)
   			{
   				if (tt != null)
   				{
   					tt.cancel();
   				}
   				timerFocus.cancel();
   			}
   			timerFocus = new Timer();
   			tt = new TimerTask() {
   			    @Override
   			    public void run() {
   			    	resetFocusText();
   			    };
   			};
   			timerFocus.scheduleAtFixedRate(tt,timeout,timeout);
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
