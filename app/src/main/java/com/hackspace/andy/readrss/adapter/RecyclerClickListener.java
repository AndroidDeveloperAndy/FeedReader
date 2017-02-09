package com.hackspace.andy.readrss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerClickListener implements RecyclerView.OnItemTouchListener{
    private GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    private static GestureDetector.SimpleOnGestureListener sSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    };

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecyclerClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        initGesture(rv.getContext());

        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, rv.getChildPosition(childView));
        }
        return false;
    }

    private void initGesture(Context context) {
        if(mGestureDetector == null) mGestureDetector = new GestureDetector(context, sSimpleOnGestureListener);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
}

