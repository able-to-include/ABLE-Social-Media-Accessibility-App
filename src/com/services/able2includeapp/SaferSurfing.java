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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;


public class SaferSurfing {
	private XmlPullParserFactory pullParserFactory;
	private  ArrayList<Videos> youtubeclips = null;
	private Context mctx;
	public SaferSurfing(Context ctx)
	{
		try {
			mctx = ctx;
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();
			InputStream in_s = ctx.getAssets().open("safesurfing.xml");	
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	        parser.setInput(in_s, "UTF_8");
	        parseXML(parser);

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
	{
       int eventType = parser.getEventType();
        Videos currentvideo = null;
        Video elementvideo = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	youtubeclips = new ArrayList<Videos>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("language")){
                    	currentvideo = new Videos();
                    	currentvideo.videos = new ArrayList<Video>();
                      } else if (currentvideo != null){
                        if (name.equalsIgnoreCase("source")){
                        	currentvideo.source = parser.nextText();    
                         }else if(name.equalsIgnoreCase("video")){
                          	elementvideo = new Video();
                         }
                        else if (name.equalsIgnoreCase("title")){
                            if(elementvideo != null)
                        	{
                        		elementvideo.title = parser.nextText();
                        	}
                        } else if (name.equalsIgnoreCase("uri")){
                           	if(elementvideo != null)
                        	{
                        		elementvideo.uri= parser.nextText();
                        	}
                        }  
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                     if (name.equalsIgnoreCase("video") && elementvideo != null){
                    	if(currentvideo != null)
                    	{
                    		currentvideo.videos.add(elementvideo);
                    	}
                    	
                    } 
                    else if (name.equalsIgnoreCase("language") && currentvideo != null){
                      	if(youtubeclips != null)
                    	{
                     		youtubeclips.add(currentvideo);
                    	}
                    	
                    } 
            }
            eventType = parser.next();
        }
	}


	public ArrayList<Videos> getYoutubeClips()
	{
		return youtubeclips;
	}

}
