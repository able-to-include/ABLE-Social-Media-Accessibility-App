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
import java.io.File;
import android.content.Context;

public class FileCache {
    
    private File cacheDir;
    
    public FileCache(Context context){
    	
        //Find the dir at SDCARD to save cached images
    	
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
        	//if SDCARD is mounted (SDCARD is present on device and mounted)
        	cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"Able2Include");
        	 //cacheDir=context.getCacheDir();
        }
        else
        {
        	// if checking on simulator the create cache dir in your application context
            cacheDir=context.getCacheDir();
        }
        
        if(!cacheDir.exists()){
        	// create cache dir in your application context
            cacheDir.mkdirs();
        }
    }
    
    public File getFile(String url){
        //Identify images by hashcode or encode by URLEncoder.encode.
        String filename=String.valueOf(url.hashCode());
        
        File f = new File(cacheDir, filename);
        return f;
        
    }
    
    public void clear(){
    	// list all files inside cache directory
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        //delete all cache directory files
        for(File f:files)
            f.delete();
    }

}