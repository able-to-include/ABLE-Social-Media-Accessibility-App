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

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;




public class AsyncTaskLoadImages extends AsyncTask<Void, String, Void> {
	
    List<String> objects;
	ImageAdapter myTaskAdapter;

	public AsyncTaskLoadImages(ImageAdapter adapter,List<String> objects) {
		myTaskAdapter = adapter;
		 this.objects = objects;
		
	}

	@Override
	protected void onPreExecute() {
		myTaskAdapter.clear();
		
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		//File[] files = targetDirector.listFiles();
		//Arrays.sort(files);
		for (String path : objects) {
			//Log.i("EDSTAG",path);
			publishProgress(path);
			if (isCancelled()) break;
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		
		myTaskAdapter.add(values[0]);
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		myTaskAdapter.notifyDataSetChanged();
		super.onPostExecute(result);
	}

}

