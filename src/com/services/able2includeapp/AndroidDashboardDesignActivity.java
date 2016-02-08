package com.services.able2includeapp;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.services.able2includeapp.R;
import com.services.accessibility.AccessibilityServiceUtil;


public class AndroidDashboardDesignActivity extends Activity {
	TextView test_service_status;
	Animation animScale = null;
	Button btn_anim = null;
	AlertDialog.Builder d=null;
	SaferSurfing youtubeclips = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        /**
         * Creating all buttons instances
         * */
        // Dashboard Facebook button
        Button btn_facebook = (Button) findViewById(R.id.btn_facebook);
        // Dashboard Messenger button
        Button btn_messenger = (Button) findViewById(R.id.btn_messenger);      
        // Dashboard Whatsapp button
        Button btn_whatsapp = (Button) findViewById(R.id.btn_whatsapp);
        
        // Dashboard Messages button
        Button btn_twitter= (Button) findViewById(R.id.btn_twitter);
        
        // Dashboard Settings button
        Button btn_settings = (Button) findViewById(R.id.btn_settings);
          
        Button btn_safesurf = (Button) findViewById(R.id.btn_safesurf);
        btn_anim = btn_settings;
        btn_anim.setVisibility(View.GONE);
        youtubeclips = new SaferSurfing(getApplicationContext());
        /**
         * Handling all button click events
         * */
        TextView projectSite = (TextView)findViewById(R.id.footertext);
        
        projectSite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				try
				{
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://abletoInclude.eu"));
					startActivity(browserIntent);
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

				}
			}
		});

        btn_facebook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				try
				{
				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
				startActivity( LaunchIntent );
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(),"Error Please ensure FaceBook is installed!", Toast.LENGTH_SHORT).show();

				}
			}
		});

        btn_messenger.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				try
				{
				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.facebook.orca");
				startActivity( LaunchIntent );
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(),"Error Please ensure Messenger is installed!", Toast.LENGTH_SHORT).show();

				}
			}
		});    
        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
			try
			{
				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
				startActivity( LaunchIntent );
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(),"Error Please ensure WhatsApp is installed!", Toast.LENGTH_SHORT).show();

			}
			}
		});
        btn_twitter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				try
				{
				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.twitter.android");
				startActivity( LaunchIntent );
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(),"Error Please ensure Twitter is installed!", Toast.LENGTH_SHORT).show();

			}
			}
		});
        
        btn_settings.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View view) {
				try
				{
				Intent accessibilityServiceIntent 
				= new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
				startActivity(accessibilityServiceIntent);
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

				}
			}
		});
        btn_safesurf.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				try
				{
					FragmentManager fm = getFragmentManager();
					String localsetting = Locale.getDefault().getLanguage();
					String lang = "english";
					String title = "Select your video to load.";
				    ArrayList<Videos> options = null;
				    ArrayList<String> listsel = new ArrayList<String>();
					if(localsetting.contains("fr"))
					{
						lang = "french";
						title = "Sélectionnez votre vidéo";
					}
					else if(localsetting.contains("pl"))
					{
						lang="polish";
						title = "Wybierz swój film";
					}
					else if(localsetting.contains("es"))
					{
						lang="spanish";
						title = "Seleccione el video";
					}					
					else if(localsetting.contains("it"))
					{
						lang="italian";
						title = "Seleziona il video";
					}		
					options = youtubeclips.getYoutubeClips();
					for(Iterator<Videos> vid = options.iterator(); vid.hasNext(); ) {
					    Videos item = vid.next();
						    if(item != null)
					    {
					    	if(item.source.contains(lang))
					    	{
					    		for(Iterator<Video> clip = item.videos.iterator(); clip.hasNext();)
					    		{
					    			Video vidclip = clip.next();
					    			listsel.add(vidclip.title);
					    		}
					    	}
					    }
					  
					}
					SafeSurfingDialog dlg = new SafeSurfingDialog(getApplicationContext(),title,listsel);
					LayoutInflater inflater=LayoutInflater.from(getApplicationContext());
					View customTitle=inflater.inflate(R.layout.customtitlebar, null);
					dlg.show(fm, "Dialog Fragment");																						
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

				}
			}
		});

    }
    /**
     * callback method from QuantityDialogFragment, returning the value of user
     * input.
     * 
     * @param selectedValue
     */
    public void onUserSelectValue(String selectedValue) {
	    ArrayList<Videos> options = null;
		String localsetting = Locale.getDefault().getLanguage();

		String lang = "english";
		options = youtubeclips.getYoutubeClips();
		if(localsetting.contains("fr"))
		{
			lang = "french";
	
		}
		else if(localsetting.contains("pl"))
		{
			lang="polish";

		}
		else if(localsetting.contains("es"))
		{
			lang="spanish";

		}					
		else if(localsetting.contains("it"))
		{
			lang="italian";

		}	
        // TODO add your implementation.
		for(Iterator<Videos> vid = options.iterator(); vid.hasNext(); ) {
		    Videos item = vid.next();
		    if(item != null)
		    {
		    	if(item.source.contains(lang))
		    	{
		    		for(Iterator<Video> clip = item.videos.iterator(); clip.hasNext();)
		    		{
		    			Video vidclip = clip.next();
		    			if(vidclip.title.equalsIgnoreCase(selectedValue))
		    			{
							try {

							     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(vidclip.uri));
							     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							     startActivity(intent);

							  }catch(ActivityNotFoundException e) {

							    // youtube is not installed.Will be opened in other available apps

							    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(vidclip.uri));
							    startActivity(i);
							}		    				
		    			}
		    		}
		    	}
		    }
		}
    }
	@Override
	protected void onResume() {
		boolean isSet = AccessibilityServiceUtil.isAccessibilityServiceOn(
				getApplicationContext(),
				"com.services.able2includeapp",
				"com.smart.able2include.Able2IncludeService");
			
			if (isSet) {
				if(animScale != null)
				{
					animScale.cancel();
					animScale.reset();
					animScale = null;
	        		setContentView(R.layout.dashboard_layout); 
	                Button btn_settings = (Button) findViewById(R.id.btn_settings);
	            	btn_settings.setVisibility(View.GONE);
	                Button btn_facebook = (Button) findViewById(R.id.btn_facebook);
	                Button btn_messenger = (Button) findViewById(R.id.btn_messenger);              
	                Button btn_whatsapp = (Button) findViewById(R.id.btn_whatsapp);                               
	                Button btn_twitter= (Button) findViewById(R.id.btn_twitter);
	                Button btn_safesurf = (Button) findViewById(R.id.btn_safesurf);
	                TextView projectSite = (TextView)findViewById(R.id.footertext);             
	                projectSite.setOnClickListener(new View.OnClickListener() {
	        			
	        			@Override
	        			public void onClick(View view) {
	        				try
	        				{
	        					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://abletoInclude.eu"));
	        					startActivity(browserIntent);
	        				}
	        				catch(Exception e)
	        				{
	        					Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

	        				}
	        			}
	        		});   
	                btn_facebook.setOnClickListener(new View.OnClickListener() {        			
	        			@Override
	        			public void onClick(View view) {
	        				try
	        				{
	        				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
	        				startActivity( LaunchIntent );
	    				}
	    				catch(Exception e)
	    				{
	    					Toast.makeText(getApplicationContext(),"Error Please ensure FaceBook is installed!", Toast.LENGTH_SHORT).show();

	    				}
	        			}
	        		});
	                btn_messenger.setOnClickListener(new View.OnClickListener() {
	        			
	        			@Override
	        			public void onClick(View view) {
	        				try
	        				{
	        				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.facebook.orca");
	        				startActivity( LaunchIntent );
	        				}
	        				catch(Exception e)
	        				{
	        					Toast.makeText(getApplicationContext(),"Error Please ensure Messenger is installed!", Toast.LENGTH_SHORT).show();

	        				}
	        			}
	        		});          
	                btn_whatsapp.setOnClickListener(new View.OnClickListener() {
	        			
	        			@Override
	        			public void onClick(View view) {
	        				try
	        				{
	        				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
	        				startActivity( LaunchIntent );
	    				}
	    				catch(Exception e)
	    				{
	    					Toast.makeText(getApplicationContext(),"Error Please ensure WhatsApp is installed!", Toast.LENGTH_SHORT).show();

	    				}
	        			}
	        		});
	                
		                btn_twitter.setOnClickListener(new View.OnClickListener() {
	        			
	        			@Override
	        			public void onClick(View view) {
	        				try
	        				{
	        				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.twitter.android");
	        				startActivity( LaunchIntent );
	    				}
	    				catch(Exception e)
	    				{
	    					Toast.makeText(getApplicationContext(),"Error Please ensure Twitter is installed!", Toast.LENGTH_SHORT).show();

	    				}
	        			}
	        		});
	                btn_safesurf.setOnClickListener(new View.OnClickListener() {
	        			
	        			@Override
	        			public void onClick(View view) {
	        				try
	        				{
	        					FragmentManager fm = getFragmentManager();
	        					String localsetting = Locale.getDefault().getLanguage();
	        					String lang = "english";
	        					String title = "Select your video to load.";
	        				    ArrayList<Videos> options = null;
	        				    ArrayList<String> listsel = new ArrayList<String>();
	        					if(localsetting.contains("fr"))
	        					{
	        						lang = "french";
	        						title = "Sélectionnez votre vidéo";
	        					}
	        					else if(localsetting.contains("pl"))
	        					{
	        						lang="polish";
	        						title = "Wybierz swój film";
	        					}
	        					else if(localsetting.contains("es"))
	        					{
	        						lang="spanish";
	        						title = "Seleccione el video";
	        					}					
	        					else if(localsetting.contains("it"))
	        					{
	        						lang="italian";
	        						title = "Seleziona il video";
	        					}		
	        					options = youtubeclips.getYoutubeClips();
	        					for(Iterator<Videos> vid = options.iterator(); vid.hasNext(); ) {
	        					    Videos item = vid.next();
	        					    if(item != null)
	        					    {
	        					    	if(item.source.contains(lang))
	        					    	{
	        					    		for(Iterator<Video> clip = item.videos.iterator(); clip.hasNext();)
	        					    		{
	        					    			Video vidclip = clip.next();
	        					    			listsel.add(vidclip.title);
	        					    		}
	        					    	}
	        					    }
	        					}
	        					SafeSurfingDialog dlg = new SafeSurfingDialog(getApplicationContext(),title,listsel);
	        					LayoutInflater inflater=LayoutInflater.from(getApplicationContext());
	        					View customTitle=inflater.inflate(R.layout.customtitlebar, null);
	        					dlg.show(fm, "Dialog Fragment");

	        				}
	        				catch(Exception e)
	        				{
	        					Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

	        				}
	        			}
	        		});
				}
		} else {
			//test_service_status.setText(R.string.test_service_off);
			//setContentView(R.layout.dashboard_layout); 
			if(d == null)
			{
			 LayoutInflater inflater=LayoutInflater.from(getApplicationContext());
			 View customTitle=inflater.inflate(R.layout.customtitlebar, null);
			 d=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
			 d.setCustomTitle(customTitle);
			 d.setMessage("AbletoInclude services are not running. Activate services by clicking the \"Settings\" button. Then scroll to the \"Services\" section of Accessibility and switch it \"ON\"");
			 d.setNeutralButton("OK",  new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	if(animScale == null)
		            	{
		                animScale = AnimationUtils.loadAnimation(AndroidDashboardDesignActivity.this, R.anim.anim_scale);
		            	}
		               // Button btn_settings = (Button) findViewById(R.id.btn_settings);

		                btn_anim.setVisibility(View.VISIBLE);
		                btn_anim.startAnimation(animScale);

		            }
		        });

			 d.show();
			}
			// AlertDialog.Builder builder = new AlertDialog.Builder(AndroidDashboardDesignActivity.this,AlertDialog.THEME_HOLO_LIGHT);


		        //builder.setTitle("AbletoInclude Services");


		        //builder.setMessage("Able2Include services are not running. Activate servcies by clicking the Settings button");


		        //Button One : Yes
		      /*  builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		                Animation animScale = AnimationUtils.loadAnimation(AndroidDashboardDesignActivity.this, R.anim.anim_scale);

		                Button btn_settings = (Button) findViewById(R.id.btn_settings);

		        		btn_settings.setVisibility(View.VISIBLE);
		    			btn_settings.startAnimation(animScale);

		            }
		        });




		        AlertDialog diag = builder.create();
		        diag.show();*/
			}

		super.onResume();
	}
}