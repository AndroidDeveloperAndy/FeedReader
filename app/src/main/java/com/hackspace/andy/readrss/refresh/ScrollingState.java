package com.hackspace.andy.readrss.refresh;

import android.view.MotionEvent;

public interface ScrollingState {

	boolean touchStopped(MotionEvent event,
			PullToRefreshComponent pullToRefreshComponent);

	boolean handleMovement(MotionEvent event,
			PullToRefreshComponent pullToRefreshComponent);
}
