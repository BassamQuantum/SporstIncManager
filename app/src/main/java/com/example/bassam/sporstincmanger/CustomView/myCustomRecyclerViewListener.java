package com.example.bassam.sporstincmanger.CustomView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by Bassam on 2/14/2018.
 */

public abstract class myCustomRecyclerViewListener extends RecyclerView.OnScrollListener {
    public static String TAG = myCustomRecyclerViewListener.class.getSimpleName();

    //private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = false; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem ,lastVisibleItem, prevLastVisibleItem, visibleItemCount, totalItemCount;
    int currentScrollState;

    // private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public myCustomRecyclerViewListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        this.currentScrollState = newState;
        // to make sure only one onLoadMore is triggered
        synchronized (this) {
            isScrollCompleted();
        }

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

        if(dy < 0) {
            if (loading)
                onUpWhileLoading();
            return;
        }
    }

    private void isScrollCompleted() {
        if (this.totalItemCount > 0
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem+visibleThreshold)
                && this.currentScrollState == SCROLL_STATE_IDLE) {
            /*** In this way I detect if there's been a scroll which has completed ***/
            /*** do the work for load more date! ***/
            if (!loading) {
                loading = true;
                onLoadMore();
            }
            else if(loading && ((totalItemCount-1) == lastVisibleItem))
                onDownWhileLoading();
        }
    }

    protected abstract void onDownWhileLoading();

    protected abstract void onUpWhileLoading();


    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public abstract void onLoadMore();

}