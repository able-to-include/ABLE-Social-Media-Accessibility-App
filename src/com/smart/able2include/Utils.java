package com.smart.able2include;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
        	
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              //Read byte from input stream
            	
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              
              //Write byte from output stream
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    public static boolean stringContainsItemFromList(String inputString, String[] items)
    {
        for(int i =0; i < items.length; i++)
        {
            if(inputString.contains(items[i]))
            {
                return true;
            }
        }
        return false;
    }
	public static ArrayList<PictoBaseCategory> GetParentCollection(ArrayList<PictoBaseCategory> categories,String parentId)
	{
		ArrayList<PictoBaseCategory> pictoBaseList = new ArrayList<>();
		String parentZero = "0";
		for (PictoBaseCategory p: categories) {
		    try {
		        // here you could evaluate you property or field
		    	if(parentId.equalsIgnoreCase(parentZero))
		    	{
		    		if(p.parentId.equalsIgnoreCase(parentId))
		    		{
		    			pictoBaseList.add(p);
		    		}	
		    	}
		    	else
		    	{
		    		if(p.id.equalsIgnoreCase(parentId))
		    		{
		    			pictoBaseList.add(p);
		    		}
		    	}
		    } catch (Exception ignored) {
		    }
		}
		return pictoBaseList;
	}
}