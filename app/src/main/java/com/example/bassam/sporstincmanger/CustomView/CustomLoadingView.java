package com.example.bassam.sporstincmanger.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bassam.sporstincmanger.R;

/**
 * Created by Bassam on 2/18/2018.
 */

public class CustomLoadingView  extends RelativeLayout {
    private Context context;
    private View mRetryView;
    private ProgressBar mProgressBar;
    private OnRetryClick mOnRetryClick;
    private TextView mRetryView_Button;

    public CustomLoadingView(Context context) {
        this(context, null);
    }

    public CustomLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        initializeUILayout();
        loading();
    }

    public CustomLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeUILayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.custom_loading_view, this);
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
    }

    public void loading() {
        this.setVisibility(View.VISIBLE);
        mRetryView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        dismissRetry();
    }

    public void fails() {
        mRetryView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void success() {
        this.setVisibility(View.GONE);
    }

    public void dismissRetry(){
        mRetryView_Button.setVisibility(View.GONE);
    }

    public void enableRetry(){
        mRetryView_Button.setVisibility(View.VISIBLE);
    }

    public void setOnRetryClick(OnRetryClick onRetryClick) {
        mOnRetryClick = onRetryClick;
    }

    public interface OnRetryClick {
        void onRetry();
    }
}