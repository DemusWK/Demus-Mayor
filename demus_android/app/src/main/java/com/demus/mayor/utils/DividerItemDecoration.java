package com.demus.mayor.utils;

import com.demus.mayor.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    //private static final int[] ATTRS = new int[]{ android.R.attr.listDivider };

    private Drawable mDivider;

    /**
     * Default divider will be used
     */
    @SuppressWarnings("deprecation")
	public DividerItemDecoration(Context context) {
        //final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        mDivider = context.getResources().getDrawable(R.drawable.divider_def);//styledAttributes.getDrawable(0);
        //styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public DividerItemDecoration(Context context, int resId) {
        mDivider = ContextCompat.getDrawable(context, resId);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            
            if(i == childCount - 1) {
            	top = 0;
        		bottom = mDivider.getIntrinsicHeight();
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}