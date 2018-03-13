package com.example.bassam.sporstincmanger.CustomView;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ListView;

/**
 * Created by Bassam on 2/14/2018.
 */
public abstract class myCustomExpandableListViewListener implements AbsListView.OnScrollListener {

    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentTotalItemCount;
    private int currentScrollState;
    private int currentLastVisibleItem;
    private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
    private boolean isLoading; // True if we are still waiting for the last set of data to load.

    private ExpandableListView expandableListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean scrollLoadMore;

    public myCustomExpandableListViewListener(ExpandableListView expandableListView , SwipeRefreshLayout mSwipeRefreshLayout){
        this.expandableListView = expandableListView;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        isLoading = false;
    }

    public myCustomExpandableListViewListener(ExpandableListView expandableListView ){
        this.expandableListView = expandableListView;
        this.mSwipeRefreshLayout = null;
        isLoading = false;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.currentLastVisibleItem = expandableListView.getLastVisiblePosition();
        this.currentTotalItemCount = totalItemCount;
        int topRowVerticalPosition =
                (expandableListView == null || expandableListView.getChildCount() == 0) ?
                        0 : expandableListView.getChildAt(0).getTop();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

        if (expandableListView.getChildAt(0) == null)
            return;
        int listViewHeight = expandableListView.getMeasuredHeight();
        int itemCount = totalItemCount;
        int itemHeight = expandableListView.getChildAt(0).getMeasuredHeight();
        int dividerHeight = expandableListView.getDividerHeight();
        int totalDividerHeight = (itemCount - 1) * dividerHeight;
        int totalItemHeight = itemCount * itemHeight;
        int filledHeight = totalItemHeight + totalDividerHeight;
        scrollLoadMore = filledHeight >= listViewHeight;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.currentScrollState = scrollState;
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
