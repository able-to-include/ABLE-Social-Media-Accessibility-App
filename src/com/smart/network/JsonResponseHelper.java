package com.smart.network;

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
		      Log.i("EDSTAG", ">>> JSON RESULT <<< " +  mJsonResult);
		      Log.i("EDSTAG", ">>> JSON RESULT <<< " +  objectABLE);
		     //JSONObject respDetails = objectABLE.getJSONObject("textSimplified"); 
		     // textSimplified = respDetails.getString("simplifiedText");
		      textSimplified = objectABLE.getString("textSimplified");
		      Log.i("EDSTAG", ">>> textSimplified <<< " +  textSimplified);
		      /*String[] tokens = textSimplified.split(",", -1);
		      
		      tokens = tokens[1].split(":", -1);
		    //  tokens = tokens[1].split(".", -1);
		      textSimplified = tokens[1].substring(1);
		      textSimplified = textSimplified.substring(0, textSimplified.length() - 4);*/
		      Log.i("EDSTAG", ">>> textSimplified <<< " +  "End");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return  textSimplified;
	}
	public String GetTextFromPicto(){
		String textPicto = null;
		try {
		      JSONObject objectABLE = new JSONObject(mJsonResult);
		      Log.i("EDSTAG", ">>> JSON RESULT <<< " +  mJsonResult);
		      textPicto = objectABLE.getString("textFromPictos");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return  textPicto;
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

	/* Version 3 GetPictos
	public boolean GetPictos( ArrayList<String> pictoArray,ArrayList<String> request){
		boolean bret = false;
		try {
		      JSONObject objectABLE = new JSONObject(mJsonResult);
          	Log.i("EDSTAG", ">>> JSON RESULT <<< " +  mJsonResult);
        	
		      JSONArray pictos = objectABLE.getJSONArray("pictos");

		     for(int i = 0; i < pictos.length(); i++) {
		            String pictoURL = pictos.getString(i);
		            pictoURL = trimEnd(pictoURL);
		            if(i < request.size())
		            {
		            	Log.i("EDSTAG", ">>> Request <<<" + request.get(i) + "<<< Response >>>" + pictoURL );
		            }
		            else
		            {
		            	Log.i("EDSTAG", "<<< Response >>>" + pictoURL);
	            	
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
	*/
	/*
	 *   List<Note> notes;
    JSONArray notesJSON = response.getJSONArray("notes");
    notes = new ArrayList<Note>(notesJSON.length());
    for (int i = 0; i < notesJSON.length(); i++) {
        Note n = new Note(notesJSON.getJSONObject(i));
        notes.add(n);
    }

	 */
	public boolean GetPictos( ArrayList<PictoResponse> pictoArray){
		boolean bret = false;
		try {
		      JSONObject objectABLE = new JSONObject(mJsonResult);
          	Log.i("EDSTAG", ">>> JSON RESULT <<< " +  mJsonResult);
         	Log.i("EDSTAG", ">>> 1 <<< " );

		    //  JSONArray pictoJSON = objectABLE.getJSONArray("output");
         	JSONArray pictoJSON = objectABLE.getJSONArray("pictos");
		      Log.i("EDSTAG", ">>> 2 <<< " );
		     for(int i = 0; i < pictoJSON.length(); i++) {
		    	 Log.i("EDSTAG", ">>> 3 <<<  i " + i);
		    	 try {
		    	 		PictoResponse pic = new PictoResponse(pictoJSON.getJSONArray(i));
		    	 		Log.i("EDSTAG", ">>> 4 <<< " );
		    	 		pictoArray.add(pic);
		    	 }
		    	 catch(Exception e)
		    	 {
		    		 Log.i("EDSTAG", ">>> Error <<< " + e.getMessage());
		    	 }
		    	 

		            /*String pictoURL = pictos.getString(i);
		            pictoURL = trimEnd(pictoURL);
		            if(i < request.size())
		            {
		            	Log.i("EDSTAG", ">>> Request <<<" + request.get(i) + "<<< Response >>>" + pictoURL );
		            }
		            else
		            {
		            	Log.i("EDSTAG", "<<< Response >>>" + pictoURL);
	            	
		            }
		            if((pictoURL.length() > 0) )
   	       		 	{
		            	if(pictoURL.charAt(0) != '\n')
		            	{
   	       		 			pictoArray.add(pictoURL);
		            	}
   	       		 	}*/
		     }
		     bret = true;

		} catch ( Exception e) {
			// TODO Auto-generated catch block
		      Log.i("EDSTAG", ">>> Error <<< " + e.getMessage());

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

      	//Toast.makeText(applicationContext,description, Toast.LENGTH_LONG).show();
        
        return description;
    }

}
