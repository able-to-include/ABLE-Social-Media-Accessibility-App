package com.smart.network;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class ABLEClient extends AsyncTask<String, Integer, String> {
	 public AsyncResponse delegate = null;//Call back interface
	  public ArrayList<String> mTextArray;
	  public ABLEClient(AsyncResponse asyncResponse,String focustext) {
	        delegate = asyncResponse;//Assigning call back interfacethrough constructor
	        String [] items = focustext.split(" ");
	        mTextArray = new ArrayList(Arrays.asList(focustext.split(" ")));
	        for(int i = 0 ; i < mTextArray.size(); i++)
	        {
	       		// Log.i("EDSTAG", mTextArray.get(i));
	        }

	   }
	  public ABLEClient() {
	   }
		@SuppressWarnings("finally")
		@Override
		protected String doInBackground(String... params) {
		    StringBuffer response = new StringBuffer();
		    String url = params[0];
		    try {
	            url += "?" + params[1];

	            URL obj = new URL(url);

	            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	            con.setConnectTimeout(30000);
	            con.setReadTimeout(30000);
	            // optional default is GET
	            con.setRequestMethod("GET");
	            

	            //add request header
	            //con.setRequestProperty("User-Agent", USER_AGENT);

	            int responseCode = con.getResponseCode();
	            BufferedReader in = new BufferedReader(
	                    new InputStreamReader(con.getInputStream()));
	            String inputLine;


	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();

		    	
		    }catch (Exception e) {
		        e.printStackTrace();

		    } finally {
		    	return response.toString();
		    }
		}
	    @Override
	    protected void onPostExecute(String result) {
	        delegate.processFinish(result,mTextArray);
	    }
}
