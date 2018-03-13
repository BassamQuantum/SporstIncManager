package com.example.bassam.sporstincmanger.CustomView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.R;

/**
 * Created by Bassam on 2/5/2018.
 */

public class myCustomRecyclerView extends RelativeLayout {
    private Context context;
    private RecyclerView recyclerView;
    private ProgressBar mLoadMoreProgressBar;
    private View mEmptyView;
    private View mRetryView;
    private ProgressBar mProgressBar;
    private myCustomRecyclerView.OnRetryClick mOnRetryClick;
    private TextView mRetryView_Button;
    private TextView mEmptyView_Text;
    private ImageView mEmptyView_Image;

    public myCustomRecyclerView(Context context) {
        this(context, null);
    }

    public myCustomRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        initializeUILayout();
        loading();
    }

    public myCustomRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeUILayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.custom_recycler_view, this);
        recyclerView =  mView.findViewById(R.id.recyclerView);
        mEmptyView = mView.findViewById(R.id.layout_empty);
        mEmptyView_Image = mView.findViewById(R.id.empty_layout_image);
        mEmptyView_Text = mView.findViewById(R.id.empty_layout_text);
        mRetryView = mView.findViewById(R.id.layout_retry);
        mRetryView_Button = mView.findViewById(R.id.layout_retry_button);
        mRetryView_Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mOnRetryClick.onRetry();
                    }
                }, 1500);
            }
        });
        mProgressBar = mView.findViewById(R.id.progress_bar);
        mLoadMoreProgressBar = mView.findViewById(R.id.loadMore_progress_bar);
    }

    public void setmEmptyView(int imageResource ,int emptyText){
        mEmptyView_Image.setImageResource(imageResource);
        mEmptyView_Text.setText(emptyText);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void loading() {
        mRetryView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void loadMore(){
        recyclerView.getLayoutManager().scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        mLoadMoreProgressBar.setVisibility(View.VISIBLE);
    }

    public void finishLoading(){
        mLoadMoreProgressBar.setVisibility(View.GONE);
    }

    public void empty() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRetryView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void retry() {
        mRetryView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void success() {
        mRetryView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void notifyChange(int size){
        if (size>0)
            success();
        else
            empty();
    }

    public void setOnRetryClick(myCustomRecyclerView.OnRetryClick onRetryClick) {
        mOnRetryClick = onRetryClick;
    }

    public interface OnRetryClick {
        void onRetry();
    }
}