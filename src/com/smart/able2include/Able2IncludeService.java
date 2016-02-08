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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Toast;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.speech.tts.TextToSpeech;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.VideoView;
import com.services.able2includeapp.R;

@SuppressWarnings("unused")
public class Able2IncludeService extends AccessibilityService {
	private static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";
	private Object mSyncToken = new Object();
	private SystemOverlayView mButtonOverlay = null; 
	private boolean bActive = false;
	private ActivityManager am;
	private String toppackage;
	private BroadcastReceiver mBroadcastReceiver; 

	private Timer timer;

  
    /** Command to the service to display a message */
    /** Bundle Strings */



    /** ****************** Private Functions ****************** **/


	 private String getProcess() throws Exception {
		    if (Build.VERSION.SDK_INT >= 21) {
	    		String [] test = new String[100];
	    		test = getLollipop();
	    		if(test.length > 0)
	    		{
	    			return test[0];
	    		}
	    		else
	    		{
	    			return "";
	    		}
		    } else {
		        return getProcessOld();
		    }
		}
	//API 21 and above
	    private String[] getLollipop() {
	        final int PROCESS_STATE_TOP = 2;

	        try {
	            Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");

	            List<ActivityManager.RunningAppProcessInfo> processes =
	                activityManager().getRunningAppProcesses();
	            for (ActivityManager.RunningAppProcessInfo process : processes) {
	                if (
	                    // Filters out most non-activity processes
	                    process.importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
	                    &&
	                    // Filters out processes that are just being
	                    // _used_ by the process with the activity
	                    process.importanceReasonCode == 0
	                ) {
	                    int state = processStateField.getInt(process);

	                    if (state == PROCESS_STATE_TOP)
	                        /*
	                         If multiple candidate processes can get here,
	                         it's most likely that apps are being switched.
	                         The first one provided by the OS seems to be
	                         the one being switched to, so we stop here.
	                         */
	                        return process.pkgList; 
	                }
	            }
	        } catch (NoSuchFieldException | IllegalAccessException e) {
	            throw new RuntimeException(e);
	        }

	        return new String[] { };
	    }

	    private ActivityManager activityManager() {
	        return (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
	    }
	 //API below 21
	 @SuppressWarnings("deprecation")
	 private String getProcessOld() throws Exception {
	     String topPackageName = null;
	     ActivityManager activity = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
	     List<RunningTaskInfo> runningTask = activity.getRunningTasks(1);
	     if (runningTask != null) {
	         RunningTaskInfo taskTop = runningTask.get(0);
	         ComponentName componentTop = taskTop.topActivity;
	         topPackageName = componentTop.getPackageName();
	     }
	
	     return topPackageName;
	 }

	@Override
	public void onCreate() {
		super.onCreate();
		if(mButtonOverlay == null)
		{
			mButtonOverlay = new SystemOverlayView(this);
		}
		mButtonOverlay.setOrientation(this.getResources().getConfiguration().orientation);

		am= (ActivityManager) this.getSystemService(ACTIVITY_SERVICE); 
		timer = new Timer();
		//Set the schedule function and rate
		timer.scheduleAtFixedRate(new TimerTask() {
			private Context mServiceContext;
		    @Override
		    public void run() {
		        //Called each time when 1000 milliseconds (1 second) (the period parameter)
		       
				try
				{
					toppackage = getProcess();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}

		    	if(toppackage.equals("com.facebook.katana")||
		    			toppackage.equals("com.whatsapp")||  
		    			toppackage.equals("com.viber.voip")||
		    			toppackage.equals("com.twitter.android")||
		    			toppackage.equals("com.facebook.orca"))
			       {
			        //Do your work here

			       }
			       else
			       {
			   	    	if(mButtonOverlay != null)
			   	    	{
			   	    		mButtonOverlay.cleanUp();
			   	    	} 	   
			       }
		    
		    }

		},
		//Set how long before to start calling the TimerTask (in milliseconds)
		5000,
		//Set the amount of time between each execution (in milliseconds)
		500);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BCAST_CONFIGCHANGED);
        mBroadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent myIntent) {

	            if ( myIntent.getAction().equals( BCAST_CONFIGCHANGED ) ) {
	                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
	                    // it's Landscape
	                	if(mButtonOverlay != null)
	                	{
	                		mButtonOverlay.setOrientation(Configuration.ORIENTATION_LANDSCAPE);
	                	}
	                }
	                else {
	                	if(mButtonOverlay != null)
	                	{
	                  	mButtonOverlay.setOrientation(Configuration.ORIENTATION_PORTRAIT);
	                	}
	                }
	            }
	        }
	    };
	    getApplicationContext().registerReceiver(mBroadcastReceiver, filter);
 		Toast.makeText(getApplicationContext(),"AbletoInclude Services Created", Toast.LENGTH_LONG).show();

	}
	@Override
	public void onDestroy() {
 		Toast.makeText(getApplicationContext(),"AbletoInclude Services Stopped", Toast.LENGTH_LONG).show();
		timer.cancel();
		if(mButtonOverlay != null)
		{
			mButtonOverlay.cleanUp();
		}
		getApplicationContext().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}
    @Override
    public void onServiceConnected() {
    	//do nothing for now
    }
    @Override
    public boolean onGesture(int gestureId) {
    	return false;
    }
	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		super.onKeyEvent(event);
 		return true;
	}



	@SuppressWarnings("deprecation")
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
	  	final int eventType = event.getEventType();
	  	Context context = getApplicationContext();
	    AccessibilityNodeInfo nodeInfo = event.getSource();
	    if(mButtonOverlay != null)
	    {
	    	mButtonOverlay.createOverlay();
	    }
	    int fromindex = event.getFromIndex();
	   // event.
	    // remove the text surrounding braces
	    String result = event.getText().toString().replaceFirst(Pattern.quote("["), "");
	    String eventText = "";
	    try
	    {
			StringBuilder b = new StringBuilder(result);
			b.replace(result.lastIndexOf("]"), result.lastIndexOf("]") + 1, "" );
			eventText = b.toString();
	    }
		catch(Exception ex)
		{
			eventText = result;
		}
 	    switch(eventType) {
        	case AccessibilityEvent.TYPE_VIEW_CLICKED:
        	{
        	    if(mButtonOverlay != null)
        	    {
        	    	mButtonOverlay.setFocusText(eventText);
        	    }
        	}
        	break;
        	case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
        	{
        		//do nothing for now
        	}
        	break;
        	case AccessibilityEvent.TYPE_VIEW_FOCUSED:
        	{
        	    if(mButtonOverlay != null)
        	    {
        	    	mButtonOverlay.setFocusText(eventText);
        	    }  	
        	}
        	break;
        	case AccessibilityEvent.TYPE_VIEW_SELECTED:
        	{
        	    if(mButtonOverlay != null)
        	    {
        	    	mButtonOverlay.setFocusText(eventText);
        	    } 
        	}  		
        	break;
        	case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
        	{
        		//do nothing for now
        	}
        	break;
        	case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
        	{
        		//do nothing for now
        	}
        	break;
        	case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
        	{
        		//do nothing for now
        	}   
        	break;
        	default:
        	break;
	    }
	    }
	}

