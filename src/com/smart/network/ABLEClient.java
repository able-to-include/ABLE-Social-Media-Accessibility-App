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
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class ABLEClient extends AsyncTask<String, Integer, String> {
	 public AsyncResponse delegate = null;//Call back interface
	  public ArrayList<String> mTextArray;
	  public ABLEClient(AsyncResponse asyncResponse,String focustext) {
	        delegate = asyncResponse;//Assigning call back interfacethrough constructor
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
