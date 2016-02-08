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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class SafeSurfingDialog extends DialogFragment {
	private AlertDialog.Builder builder;
	private String mTitle;
	private ArrayList<String> mitems;
	private Context mctx;
	private String[] mstockArr;
	public SafeSurfingDialog(Context ctx,String title,ArrayList<String> items)
	{
		mTitle = title;
		mctx = ctx;
		//Toast.makeText(ctx,"items = " + Integer.toString(items.size()), Toast.LENGTH_LONG).show();
		mitems = items;
	}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	mstockArr = new String[mitems.size()];
    	mstockArr = mitems.toArray(mstockArr);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(mTitle);
        builder.setItems(mstockArr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	AndroidDashboardDesignActivity callingActivity = (AndroidDashboardDesignActivity) getActivity();
                callingActivity.onUserSelectValue(mstockArr[item]);

            }
        });
        LayoutInflater inflater=LayoutInflater.from(mctx);
		View customTitle=inflater.inflate(R.layout.customtitlebar, null);
		TextView title = (TextView) customTitle.findViewById(R.id.customtitlebar);
		title.setText(mTitle);
		builder.setCustomTitle(customTitle);     
        return builder.create();
    }
    public void setListItems(ArrayList<String> items)
    {
    	String[] stockArr = new String[items.size()];
    	stockArr = items.toArray(stockArr);
    	builder.setItems(stockArr, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int item) {
                   // Do something with the selection
                  // mDoneButton.setText(items[item]);
               }
           });
    }
    public void setDialogTitle(String title)
    {
    	  builder.setTitle(title);	
    }
    public void setCustomTitle(View customTitleView)
    {
    	builder.setCustomTitle(customTitleView);
    }
}