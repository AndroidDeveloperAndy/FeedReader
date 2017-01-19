package com.hackspace.andy.readrss.refresh;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hackspace.andy.readrss.R;

public class PullToRefreshListView extends RelativeLayout {

	private PullToRefreshComponent pullToRefresh;
	private ViewGroup upperButton;
	private ViewGroup lowerButton;
	private Handler uiThreadHandler;
	private ListView listView;

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_primary_feed, this);
		this.listView = (ListView) this.findViewById(android.R.id.list);
		this.uiThreadHandler = new Handler();
		this.initializePullToRefreshList();
	}

	private void initializePullToRefreshList() {
		this.initializeRefreshUpperButton();
		this.initializeRefreshLowerButton();
		this.pullToRefresh = new com.hackspace.andy.readrss.refresh.PullToRefreshComponent(
				this.upperButton, this.lowerButton, this.listView,
				this.uiThreadHandler);

		this.pullToRefresh.setOnReleaseUpperReady(ready -> {
            if (ready) {
                ((TextView) PullToRefreshListView.this.upperButton
                        .findViewById(R.id.pull_to_refresh_text))
                        .setText(R.string.pull_to_refresh_release_label);
            } else {
                ((TextView) PullToRefreshListView.this.upperButton
                        .findViewById(R.id.pull_to_refresh_text))
                        .setText(R.string.pull_to_refresh_pull_down_label);
            }
        });

		this.pullToRefresh.setOnReleaseLowerReady(ready -> {
            if (ready) {
                ((TextView) PullToRefreshListView.this.lowerButton
                        .findViewById(R.id.pull_to_refresh_text))
                        .setText(R.string.pull_to_refresh_release_label);
            } else {
                ((TextView) PullToRefreshListView.this.lowerButton
                        .findViewById(R.id.pull_to_refresh_text))
                        .setText(R.string.pull_to_refresh_pull_up_label);
            }
        });
	}

	public void setOnPullDownRefreshAction(final RefreshListener listener) {
		this.pullToRefresh.setOnPullDownRefreshAction(new RefreshListener() {

			@Override
			public void refreshFinished() {
				PullToRefreshListView.this.runOnUIThread(() -> {
                    PullToRefreshListView.this
                            .setPullToRefresh(PullToRefreshListView.this.upperButton);
                    PullToRefreshListView.this.invalidate();
                });
				PullToRefreshListView.this.runOnUIThread(() -> listener.refreshFinished());
			}

			@Override
			public void doRefresh() {
				PullToRefreshListView.this.uiThreadHandler.post(() -> {
                    PullToRefreshListView.this
                            .setRefreshing(PullToRefreshListView.this.upperButton);
                    PullToRefreshListView.this.invalidate();
                });
				listener.doRefresh();
			}
		});
	}

	protected void runOnUIThread(Runnable runnable) {
		this.uiThreadHandler.post(runnable);
	}

	public void setOnPullUpRefreshAction(final RefreshListener listener) {
		this.pullToRefresh.setOnPullUpRefreshAction(new RefreshListener() {

			@Override
			public void refreshFinished() {
				PullToRefreshListView.this
						.setPullToRefresh(PullToRefreshListView.this.lowerButton);
				PullToRefreshListView.this.invalidate();
				listener.refreshFinished();
			}

			@Override
			public void doRefresh() {
				PullToRefreshListView.this.uiThreadHandler.post(() -> {
                    PullToRefreshListView.this
                            .setRefreshing(PullToRefreshListView.this.lowerButton);
                    PullToRefreshListView.this.invalidate();
                });
				listener.doRefresh();
			}
		});
	}

	private void initializeRefreshLowerButton() {
		this.upperButton = (ViewGroup) this
				.findViewById(R.id.refresh_upper_button);
	}

	private void initializeRefreshUpperButton() {
		this.lowerButton = (ViewGroup) this
				.findViewById(R.id.refresh_lower_button);
	}

	protected void setPullToRefresh(ViewGroup refreshView) {
		int text = 0;
		if (refreshView == this.upperButton) {
			text = R.string.pull_to_refresh_pull_down_label;
		} else {
			text = R.string.pull_to_refresh_pull_up_label;
		}
		((TextView) refreshView.findViewById(R.id.pull_to_refresh_text))
				.setText(text);
		refreshView.findViewById(R.id.pull_to_refresh_progress).setVisibility(
				View.INVISIBLE);
		refreshView.findViewById(R.id.pull_to_refresh_image).setVisibility(
				View.VISIBLE);
	}

	protected void setRefreshing(ViewGroup refreshView) {
		((TextView) refreshView.findViewById(R.id.pull_to_refresh_text))
				.setText(R.string.pull_to_refresh_refreshing_label);
		refreshView.findViewById(R.id.pull_to_refresh_progress).setVisibility(
				View.VISIBLE);
		refreshView.findViewById(R.id.pull_to_refresh_image).setVisibility(
				View.INVISIBLE);
	}

	public void disablePullUpToRefresh() {
		this.pullToRefresh.disablePullUpToRefresh();
	}

	public void disablePullDownToRefresh() {
		this.pullToRefresh.disablePullDownToRefresh();
	}
}
