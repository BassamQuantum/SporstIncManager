package com.example.bassam.sporstincmanger.CustomView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Bassam on 2/13/2018.
 */

public abstract class myCustomListViewListener implements AbsListView.OnScrollListener {

    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentTotalItemCount;
    private int currentScrollState;
    private int currentLastVisibleItem;
    private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
    private boolean isLoading; // True if we are still waiting for the last set of data to load.

    private ListView listView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean scrollLoadMore;

    public myCustomListViewListener(ListView listView , SwipeRefreshLayout mSwipeRefreshLayout){
        this.listView = listView;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        isLoading = false;
    }

    public myCustomListViewListener(ListView listView ){
        this.listView = listView;
        this.mSwipeRefreshLayout = null;
        isLoading = false;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.currentLastVisibleItem = listView.getLastVisiblePosition();
        this.currentTotalItemCount = totalItemCount;
        int topRowVerticalPosition =
                (listView == null || listView.getChildCount() == 0) ?
                        0 : listView.getChildAt(0).getTop();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);


        if (listView.getChildAt(0) == null)
            return;
        int listViewHeight = listView.getMeasuredHeight();
        int itemCount = totalItemCount;
        int itemHeight = listView.getChildAt(0).getMeasuredHeight();
        int dividerHeight = listView.getDividerHeight();
        int totalDividerHeight = (itemCount - 1) * dividerHeight;
        int totalItemHeight = itemCount * itemHeight;
        int filledHeight = totalItemHeight + totalDividerHeight;
        scrollLoadMore = filledHeight >= listViewHeight;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.currentScrollState = scrollState;

        Log.d("ListViewListener","listView: "+listView.getHeight()+" ,Divider: "+ listView.getDividerHeight());
        Log.d("ListViewListener","Boolean value: "+scrollLoadMore);
        synchronized (this) {
            if (scrollLoadMore)
                this.isScrollCompleted();
        }
    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > 0
                && (currentTotalItemCount - currentVisibleItemCount) <= (currentFirstVisibleItem+visibleThreshold)
                && this.currentScrollState == SCROLL_STATE_IDLE) {
            /*** In this way I detect if there's been a scroll which has completed ***/
            /*** do the work for load more date! ***/
            if (!isLoading) {
                isLoading = true;
                loadMoreData();
            }
        }
    }

    public void setLoading(boolean isLoading){
        this.isLoading = isLoading;
    }

    public abstract void loadMoreData();
}
