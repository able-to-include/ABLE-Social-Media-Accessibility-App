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
import java.util.ArrayList;

import com.services.able2includeapp.R;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PictoOverlayView extends View{
	private Context mServiceContext;
	private WindowManager wm;
    private boolean mOnTop = false;
    private int mLastOrientation = 0;
    private RelativeLayout frameLayout;
    private Button dialogButton;
    private GridView mGridview;
    private AsyncTaskLoadImages myAsyncTaskLoadImages;
    @SuppressWarnings("deprecation")
    private ArrayList<String> mPictoArray;
    private ArrayList<String> mPictoReq;
    private Object mSyncToken = new Object(); 
    public OverlayClick delegate = null;//Call back interface
    
	ImageAdapter myImageAdapter;
	public PictoOverlayView(OverlayClick click,Context context,WindowManager mng,int Orientation,ArrayList<String> pictoReq,ArrayList<String> pictoArray) {
		super(context);
		mServiceContext = context;
		mPictoArray = pictoArray;
		mPictoReq = pictoReq;
		wm = mng;
		delegate = click;
		myImageAdapter = new ImageAdapter(mServiceContext);
		createView();
		// TODO Auto-generated constructor stub
	}
    /**
     * Creates head view and adds it to the window manager.
     */

	private void createView() {
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
		WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
		
		params.gravity =  Gravity.START | Gravity.TOP;
		params.x =0;
		params.y = 30;
		params.width = wm.getDefaultDisplay().getWidth();
		params.height  = wm.getDefaultDisplay().getHeight()-30;
         
		frameLayout = new RelativeLayout(mServiceContext);

		LayoutInflater layoutInflater = (LayoutInflater) mServiceContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Here is the place where you can inject whatever layout you want.
		try
		{
			layoutInflater.inflate(R.layout.pictos_layout, frameLayout);
			StringBuilder str = new StringBuilder();
			PictoListAdapter adapter = new PictoListAdapter(mServiceContext, R.layout.pictos_list_item, mPictoArray,mPictoReq);
			mGridview = (GridView) frameLayout.findViewById(R.id.gridview);
			mGridview.setAdapter(adapter);
			mGridview.setOnItemClickListener(myOnItemClickListener);
			TextView txt = (TextView) frameLayout.findViewById(R.id.pictoSummary);
			for(int i = 0; i < mPictoReq.size();i++)
			{
				str.append(mPictoReq.get(i));
				str.append(" ");
			}
			txt.setText(str.toString());
			dialogButton = (Button) frameLayout.findViewById(R.id.picButtonOK);
			dialogButton.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				RemoveOvelay();
 				delegate.onClick();
  				}
			});
			synchronized(mSyncToken)
			{
				wm.addView(frameLayout, params);
				mOnTop = true;
			}
		}
		catch (Exception e)
		{
			Toast.makeText(mServiceContext,e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	OnItemClickListener myOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

		}
	};

	public void RemoveOvelay()
	{
		synchronized(mSyncToken)
		{
			if(mOnTop == true)
			{
				wm.removeView(frameLayout);
				mOnTop = false;
			}
		}
	}
	
	public void setOrientation(int Orientation)
	{
		boolean onTop = false;
		synchronized(mSyncToken)
		{
			onTop =mOnTop;
		}
		if(onTop == true)
		{
			RemoveOvelay();
			createView();
		}
		mLastOrientation = Orientation;
	}
	public void CreateOverlay()
	{
		setOrientation(mLastOrientation);
	}
}
