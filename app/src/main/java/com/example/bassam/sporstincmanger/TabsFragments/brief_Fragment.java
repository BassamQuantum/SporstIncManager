package com.example.bassam.sporstincmanger.TabsFragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassam.sporstincmanger.Activities.EventsActivity;
import com.example.bassam.sporstincmanger.Activities.EventsDetailsActivity;
import com.example.bassam.sporstincmanger.Activities.NewsActivity;
import com.example.bassam.sporstincmanger.Activities.NewsDetailsActivity;
import com.example.bassam.sporstincmanger.Adapters.EventsRecyclerAdapter;
import com.example.bassam.sporstincmanger.Adapters.NewsRecyclerAdapter;
import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Backend.HttpRequest;
import com.example.bassam.sporstincmanger.Entities.EventEntity;
import com.example.bassam.sporstincmanger.Entities.NewsEntity;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.example.bassam.sporstincmanger.R;
import com.example.bassam.sporstincmanger.util.ConnectionUtilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bassam on 12/10/2017.
 */

public class brief_Fragment extends Fragment {
    private static final String TAG = brief_Fragment.class.getSimpleName();
    SwipeRefreshLayout mSwipeRefreshLayout;

    TextView AboutAcademy;
    ImageView Logo;
    LinearLayout newsLayout , eventsLayout;
    ImageView newsMore, eventMore;
    NewsEntity newsEntity;
    EventEntity eventEntity;
    String logo ,brief;

    ProgressBar progressBar;
    LinearLayout retry;
    RelativeLayout loading;
    NestedScrollView scrollView;

    LinearLayout newsItem , eventItem ;

    ImageView newsImage , eventImage;
    TextView newsDesc , eventDesc , eventDay , eventMonth ;

    ImageView linkSponsor1 , linkSponsor2 , linkSponsor3;

    int limitValue , Counter = 0;

    List<String> sponsorsLinks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home,container,false);
        setHasOptionsMenu(false);

        limitValue = getResources().getInteger(R.integer.sliderLimit);

        sponsorsLinks.add("https://vasatrainer.com/");
        sponsorsLinks.add("https://dolfinswimwear.com/");

        scrollView = root.findViewById(R.id.layoutScrollView);
        mSwipeRefreshLayout = root.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorWhite));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Counter = 0;
                getContent();
            }
        });
        newsLayout = root.findViewById(R.id.homeNewsSection);
        eventsLayout = root.findViewById(R.id.homeNewsSection);
        progressBar = root.findViewById(R.id.progress_bar);
        loading = root.findViewById(R.id.LoadingData);

        retry = root.findViewById(R.id.layout_retry);
        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL , false);

        newsItem = root.findViewById(R.id.news_item);
        eventItem = root.findViewById(R.id.event_item);
        newsImage = root.findViewById(R.id.news_image);
        eventImage = root.findViewById(R.id.event_image);
        newsDesc = root.findViewById(R.id.news_description);
        eventDesc = root.findViewById(R.id.event_description);
        eventDay = root.findViewById(R.id.event_day);
        eventMonth = root.findViewById(R.id.event_month);

        newsMore = root.findViewById(R.id.newsMore);
        eventMore = root.findViewById(R.id.eventsMore);

        newsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
                intent.putExtra("MyNews",newsEntity);
                startActivity(intent);
            }
        });

        eventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EventsDetailsActivity.class);
                intent.putExtra("MyEvent",eventEntity);
                startActivity(intent);
            }
        });
        newsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NewsActivity.class));
            }
        });

        eventMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EventsActivity.class));
            }
        });
        AboutAcademy = root.findViewById(R.id.AboutAcademy);
        Logo = root.findViewById(R.id.AcademyLogo);

        linkSponsor1 = root.findViewById(R.id.sponsorLink1);
        linkSponsor2 = root.findViewById(R.id.sponsorLink2);
        linkSponsor3 = root.findViewById(R.id.sponsorLink3);

        linkSponsor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSponsorPage(sponsorsLinks.get(0));
            }
        });

        linkSponsor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSponsorPage(sponsorsLinks.get(1));
            }
        });

        linkSponsor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openSponsorPage(sponsorsLinks.get(2));
            }
        });

        if (savedInstanceState!=null)
            fillBySavedState(savedInstanceState);

        else
            getContent();

        return root;
    }

    private void openSponsorPage(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("ScrollPosition", new int[]{scrollView.getScrollX() , scrollView.getScrollY()});
        outState.putSerializable("NewsEntity", newsEntity);
        outState.putSerializable("EventsEntity", eventEntity);
        outState.putString("About",brief);
        outState.putString("logo",logo);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        loading.setVisibility(View.GONE);
        newsEntity = (NewsEntity) savedInstanceState.getSerializable("NewsEntity");
        eventEntity = (EventEntity) savedInstanceState.getSerializable("EventsEntity");

        AboutAcademy.setText(brief);
        if (!logo.equals("")) {
            Picasso.with(getContext()).load(logo).into(Logo);
        }

        int[] positions = savedInstanceState.getIntArray("ScrollPosition");
        if (positions != null)
            scrollView.scrollTo(positions[0],positions[1]);
    }

    private void getContent(){
        if (checkConnection())
            initializeHome();

        else {
            progressBar.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        }
    }

    private void fillView(JSONArray response) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (response != null) {
            try {
                JSONObject object = response.getJSONObject(0);

                logo ="";// object.getString("logo");
                brief = object.getString("about");

                if (!logo.equals("")) {
                    Picasso.with(getContext()).load(logo).into(Logo);
                }

                AboutAcademy.setText(brief);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillNewsView() {
        if (newsEntity == null){
            newsItem.setVisibility(View.GONE);
            newsMore.setVisibility(View.GONE);
            return;
        }
        newsDesc.setText(newsEntity.getContent());
        String ImgUrl = newsEntity.getImg();
        if (!ImgUrl.equals("")) {
            if (getContext() != null)
                Picasso.with(getContext()).load(Constants.others_host + ImgUrl).into(newsImage);
        }
    }


    private void fillEventView() {
        if (eventEntity == null){
            eventItem.setVisibility(View.GONE);
            eventMore.setVisibility(View.GONE);
            return;
        }
        eventDesc.setText(eventEntity.getDescription());
        String ImgUrl = eventEntity.getImgUrl();
        if (!ImgUrl.equals("")) {
            if (getContext() != null)
                Picasso.with(getContext()).load(Constants.others_host + ImgUrl).into(eventImage);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM", Locale.ENGLISH);
        String event_date = formatter.format(eventEntity.getDate());
        eventDay.setText(event_date);
    }

    private void initializeHome(){
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectHomeData);
            JSONObject limit = new JSONObject();
            limit.put("start", 0);
            limit.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put("limit", limit.toString());

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if (response != null) {
                        try {
                            JSONObject academyResult = response.getJSONObject(0).getJSONArray("academy").getJSONObject(0);
                            JSONObject eventResult = null;

                            try {
                                eventResult = response.getJSONObject(1).getJSONArray("event").getJSONObject(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JSONObject newsResult =null;
                            try {
                                newsResult = response.getJSONObject(2).getJSONArray("news").getJSONObject(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            fillHomeView(academyResult, eventResult , newsResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAboutView(JSONObject object) {
        try {
            logo ="";// object.getString("logo");
            brief = object.getString("about");

            if (!logo.equals("")) {
                Picasso.with(getContext()).load(logo).into(Logo);
            }

            AboutAcademy.setText(brief);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillHomeView(JSONObject academyResult, JSONObject eventResult, JSONObject newsResult) {
        mSwipeRefreshLayout.setRefreshing(false);
        fillAboutView(academyResult);
        if (eventResult != null)
            eventEntity = new EventEntity(eventResult);
        fillEventView();
        if (newsResult != null)
            newsEntity = new NewsEntity(newsResult);
        fillNewsView();
        loading.setVisibility(View.GONE);
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }
}
