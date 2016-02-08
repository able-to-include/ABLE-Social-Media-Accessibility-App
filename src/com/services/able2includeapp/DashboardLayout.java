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
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
 
/**
 * Custom layout that arranges children in a grid-like manner, optimizing for even horizontal and
 * vertical whitespace.
 */
public class DashboardLayout extends ViewGroup {
 
    private static final int UNEVEN_GRID_PENALTY_MULTIPLIER = 10;
    private Context mContext;
    private int mMaxChildWidth = 0;
    private int mMaxChildHeight = 0;
 
    public DashboardLayout(Context context) {
        super(context, null);
        mContext = context;
        
    }
 
    public DashboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mContext = context;
    }
 
    public DashboardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxChildWidth = 0;
        mMaxChildHeight = 0;
 
        // Measure once to find the maximum child size.
 
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
 
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
 
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
 
            mMaxChildWidth = Math.max(mMaxChildWidth, child.getMeasuredWidth());
            mMaxChildHeight = Math.max(mMaxChildHeight, child.getMeasuredHeight());
        }
 
        // Measure again for each child to be exactly the same size.
 
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                mMaxChildWidth, MeasureSpec.EXACTLY);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                mMaxChildHeight, MeasureSpec.EXACTLY);
 
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
 
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
 
        setMeasuredDimension(
                resolveSize(mMaxChildWidth, widthMeasureSpec),
                resolveSize(mMaxChildHeight, heightMeasureSpec));
    }
 
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;
        boolean breverse = true;
        final int count = getChildCount();
        if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
        	breverse = false;
        }

        // Calculate the number of visible children.
        int visibleCount = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            ++visibleCount;
        }
 
        if (visibleCount == 0) {
            return;
        }
 
        // Calculate what number of rows and columns will optimize for even horizontal and
        // vertical whitespace between items. Start with a 1 x N grid, then try 2 x N, and so on.
        int bestSpaceDifference = Integer.MAX_VALUE;
        int spaceDifference;
 
        // Horizontal and vertical space between items
        int hSpace = 0;
        int vSpace = 0;
 
        int cols = 1;
        int rows = 0;
        if(breverse)
        {
        	rows = 1;
        }
        while (true) {
        	if(breverse)
        	{
        		cols = (visibleCount - 1) / rows + 1;
        	}
        	else
        	{
        		rows = (visibleCount - 1) / cols + 1;
        	}
    
            hSpace = ((width - mMaxChildWidth * cols) / (cols + 1));
            vSpace = ((height - mMaxChildHeight * rows) / (rows + 1));
         //	Toast.makeText(mContext,"rows "+ Integer.toString(rows), Toast.LENGTH_SHORT).show();
         //	Toast.makeText(mContext,"rows "+ Integer.toString(rows), Toast.LENGTH_SHORT).show();

            
            spaceDifference = Math.abs(vSpace - hSpace);
            if (rows * cols != visibleCount) {
                spaceDifference *= UNEVEN_GRID_PENALTY_MULTIPLIER;
            }
 
            if (spaceDifference < bestSpaceDifference) {
                // Found a better whitespace squareness/ratio
                bestSpaceDifference = spaceDifference;
 
                // If we found a better whitespace squareness and there's only 1 row, this is
                // the best we can do.
                if (rows == 1) {
                    break;
                }
            } else {
                // This is a worse whitespace ratio, use the previous value of cols and exit.
                --cols;
                rows = (visibleCount - 1) / cols + 1;
                hSpace = ((width - mMaxChildWidth * cols) / (cols + 1));
                vSpace = ((height - mMaxChildHeight * rows) / (rows + 1));
                break;
            }
 
            ++cols;
        }
 
        // Lay out children based on calculated best-fit number of rows and cols.
 
        // If we chose a layout that has negative horizontal or vertical space, force it to zero.
        hSpace = Math.max(0, hSpace);
        vSpace = Math.max(0, vSpace);
 
        // Re-use width/height variables to be child width/height.
        width = (width - hSpace * (cols + 1)) / cols;
        height = (height - vSpace * (rows + 1)) / rows;
 
        int left, top;
        int col, row;
        int visibleIndex = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
 
            row = visibleIndex / cols;
            col = visibleIndex % cols;
 
            left = hSpace * (col + 1) + width * col;
            top = vSpace * (row + 1) + height * row;
 
            child.layout(left, top,
                    (hSpace == 0 && col == cols - 1) ? r : (left + width),
                    (vSpace == 0 && row == rows - 1) ? b : (top + height));
            ++visibleIndex;
        }
    }
}