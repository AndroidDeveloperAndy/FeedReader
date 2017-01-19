package com.hackspace.andy.readrss.refresh;

public interface OnPullingAction {
	void handlePull(boolean down, int height);
}
