package com.hippotec.mapsapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.model.EventInfo;


/**
 * Created by Avishay Peretz on 09/04/2017.
 */

public class EventDetailsFragment extends Fragment {

    TextView tvName, tvDesc;
    TextView tvLikeCount;

    SwitchCompat likeBtn, dislikeBtn;

    TextView tvTime;
    TextView tvTotal, tvFemaleCount, tvMaleCount;

    private EventInfo mEvent;

    public void setEvent(EventInfo event) {
        this.mEvent = event;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);

        initializeViews(rootView);
        setData();
        // Inflate the layout for this fragment
        return rootView;
     }

    private void setData() {
        tvName.setText(mEvent.getDetails().getName());
        tvLikeCount.setText(String.valueOf(mEvent.getStats().getEventLike()));
        tvTime.setText(mEvent.getDetails().getTimeRange());
        tvDesc.setText(mEvent.getDetails().getDescription());

        tvFemaleCount.setText(mEvent.getStats().getFemalePercent());
        tvMaleCount.setText(mEvent.getStats().getMalePercent());
        tvTotal.setText(mEvent.getStats().getTotalAgeLine(this.getString(R.string.totalCount), this.getString(R.string.age)));
    }

    private void initializeViews(View rootView) {
        tvLikeCount = (TextView) rootView.findViewById(R.id.tv_likes_count);
        likeBtn = (SwitchCompat) rootView.findViewById(R.id.like_btn);
        dislikeBtn = (SwitchCompat) rootView.findViewById(R.id.dislike_btn);

        tvName = (TextView) rootView.findViewById(R.id.tv_event_name);
        tvDesc = (TextView) rootView.findViewById(R.id.tv_event_desc);

        tvTime = (TextView) rootView.findViewById(R.id.tv_event_time);

        tvTotal = (TextView) rootView.findViewById(R.id.tv_total);
        tvFemaleCount = (TextView) rootView.findViewById(R.id.tv_female_count);
        tvMaleCount = (TextView) rootView.findViewById(R.id.tv_male_count);
    }
}
