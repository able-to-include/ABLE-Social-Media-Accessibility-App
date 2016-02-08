package com.smart.network;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;








import android.util.Log;

public class JsonResponseHelper {
	
	String mJsonResult;
	boolean mValidStatus = false;
	public JsonResponseHelper(String jsonResult)
	{
		mJsonResult = jsonResult;
		IsStatusOK();
	}
	
	public boolean IsStatusOK()
	{
        String status;
		try {
			status = new JSONObject(mJsonResult).getString("status");
	        if (status.compareTo("200") == 0) {
	        	mValidStatus = true;
	        }
	        else
	        {
	        	mValidStatus = false;
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			mValidStatus = false;
			e.printStackTrace();
		}

        return mValidStatus;
	}
	public String GetAudioSpeech(){
		String audioSpeechAddress = null;
		try {
		      JSONObject objectABLE = new JSONObject(mJsonResult);
			audioSpeechAddress = objectABLE.getString("audioSpeech");;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return  audioSpeechAddress;
	}
	public String GetTextSimplified(){
		String textSimplified = null;
		try {
		      JSONObject objectABLE = new JSONObject(mJsonResult);
		      textSimplified = objectABLE.getString("textSimplified");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return  textSimplified;
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
	public boolean GetPictos( ArrayList<String> pictoArray,ArrayList<String> request){
		boolean bret = false;
		try {
		      JSONObject objectABLE = new JSONObject(mJsonResult);
 		      JSONArray pictos = objectABLE.getJSONArray("pictos");

		     for(int i = 0; i < pictos.length(); i++) {
		            String pictoURL = pictos.getString(i);
		            pictoURL = trimEnd(pictoURL);
		            if(i < request.size())
		            {
		            	//Log.i("EDSTAG", ">>> Request <<<" + request.get(i) + "<<< Response >>>" + pictoURL );
		            }
		            else
		            {
		            	//Log.i("EDSTAG", "<<< Response >>>" + pictoURL);
	            	
		            }
		            if((pictoURL.length() > 0) )
   	       		 	{
		            	if(pictoURL.charAt(0) != '\n')
		            	{
   	       		 			pictoArray.add(pictoURL);
		            	}
   	       		 	}
		     }
		     bret = true;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return  bret;
	}
    /**
     * Returns the error description associated with the given error code.
     *
     * @param result The error code
     * @param context The <code>Activity</code>
     * @return The error description associated with the code
     */

	public String GetErrorDescription() {
        String description = null;
        String result;
		try {
			result = new JSONObject(mJsonResult).getString("status");
			if (result.compareTo("408") == 0) {
	            description = Constants.error408;
	        } else if (result.compareTo("451") == 0) {
	            description = Constants.error451;
	        } else if (result.compareTo("452") == 0) {
	            description = Constants.error452;
	        } else if (result.compareTo("453") == 0) {
	            description = Constants.error453;
	        } else if (result.compareTo("454") == 0) {
	            description = Constants.error454;
	        } else if (result.compareTo("455") == 0) {
	            description = Constants.error455;
	        } else if (result.compareTo("456") == 0) {
	            description = Constants.error456;
	        } else if (result.compareTo("457") == 0) {
	            description = Constants.error457;
	        } else if (result.compareTo("458") == 0) {
	            description = Constants.error458;
	        }
	        else if (result.compareTo("459") == 0) {
	            description = Constants.error459;
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return description;
    }

}
