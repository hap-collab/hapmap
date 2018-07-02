package com.hippotec.mapsapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.base.BaseActivity;
import com.hippotec.mapsapplication.api.APIManager;
import com.hippotec.mapsapplication.api.URLs;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
import com.hippotec.mapsapplication.model.EventInfo;
import com.hippotec.mapsapplication.model.User;
import com.hippotec.mapsapplication.utils.CredentialsManager;
import com.hippotec.mapsapplication.utils.EventInfoDeserializer;
import com.hippotec.mapsapplication.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Avishay Peretz on 04/04/2017.
 */

public class EventsBottomSheetAdapter extends RecyclerView.Adapter<EventsBottomSheetAdapter.ViewHolder> implements View.OnClickListener {

    private BaseActivity mContext;
    private List<EventInfo> events;
    private User user;

    private EventInfo clickedEvent;
    private ViewHolder clickedViewHolder;

    private ItemClickCallback itemClickCallback;

    public EventsBottomSheetAdapter(BaseActivity context, List<EventInfo> list, User user) {
        this.mContext = context;
        this.events = list;
        this.user = user;
    }

    public void setEvents(List<EventInfo> events) {
        this.events = events;
    }

    public void registerCallback(ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    @Override
    public EventsBottomSheetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events_bottom_recycler_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EventInfo event = events.get(position);
        holder.bindItem(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_event_name)
        TextView tvName;

        @BindView(R.id.tv_likes_count)
        TextView tvLikeCount;

        @BindView(R.id.like_btn)
        SwitchCompat likeBtn;

        @BindView(R.id.dislike_btn)
        SwitchCompat dislikeBtn;

        @BindView(R.id.tv_event_time)
        TextView tvTime;

        @BindView(R.id.tv_event_desc)
        TextView tvDesc;

        @BindView(R.id.rl_statistics)
        RelativeLayout rlStatistics;

        @BindView(R.id.tv_total)
        TextView tvTotal;

        @BindView(R.id.tv_female_count)
        TextView tvFemaleCount;

        @BindView(R.id.tv_male_count)
        TextView tvMaleCount;

        @BindView(R.id.ll_not_now)
        LinearLayout llNotNow;

        @BindView(R.id.tv_not_now)
        TextView tvNotNow;


        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(ViewHolder.this, view);
        }

        private void bindItem(final EventInfo event) {
            tvName.setText(event.getDetails().getName());
            tvLikeCount.setText(String.valueOf(event.getStats().getEventLike()));
            tvTime.setText(event.getDetails().getTimeRange());
            tvDesc.setText(event.getDetails().getDescription());

            Date startTime = event.getDetails().getEventTime().getStart();
            if (new Date().before(startTime)) {
                setNotNowView(TimeUtils.getRemainingTime(new Date(), startTime));
            } else {
                setStatisticsView(event);
            }

            likeBtn.setTag(R.string.key_position, getLayoutPosition()); // getAdapterPosition()
            likeBtn.setTag(R.string.key_holder, this);
            likeBtn.setOnClickListener(EventsBottomSheetAdapter.this);
            likeBtn.setChecked(event.getUserInfo().isUserLiked());

            dislikeBtn.setTag(R.string.key_position, getLayoutPosition()); // getAdapterPosition()
            dislikeBtn.setTag(R.string.key_holder, this);
            dislikeBtn.setOnClickListener(EventsBottomSheetAdapter.this);
            dislikeBtn.setChecked(event.getUserInfo().isUserDisliked());

            itemView.setTag(getLayoutPosition()); // getAdapterPosition()
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickCallback.onEventClick(getLayoutPosition());
                }
            });
        }

        private void setStatisticsView(EventInfo event) {
            tvFemaleCount.setText(event.getStats().getFemalePercent());
            tvMaleCount.setText(event.getStats().getMalePercent());
            tvTotal.setText(event.getStats().getTotalAgeLine(mContext.getString(R.string.totalCount), mContext.getString(R.string.age)));
            llNotNow.setVisibility(View.GONE);
            rlStatistics.setVisibility(View.VISIBLE);
        }

        private void setNotNowView(int[] rt) {
            if (rt[0] > 0) {
                tvNotNow.setText(mContext.getString(R.string.in) + " " + rt[0] + " " + mContext.getString(R.string.hours));
            } else {
                tvNotNow.setText(mContext.getString(R.string.in) + " " + rt[1] + " " + mContext.getString(R.string.minutes));
            }
            llNotNow.setVisibility(View.VISIBLE);
            rlStatistics.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int)v.getTag(R.string.key_position);
        clickedViewHolder = (ViewHolder)v.getTag(R.string.key_holder);
        String userId = CredentialsManager.getInstance(mContext).getUser().getId();
        clickedEvent = events.get(position);
        String eventId = clickedEvent.getDetails().getId();
        switch (v.getId()) {
            case R.id.like_btn:
                clickedViewHolder.dislikeBtn.setChecked(false);
                likePressed(user, eventId, Const.TYPE_LIKE);
                break;
            case R.id.dislike_btn:
                clickedViewHolder.likeBtn.setChecked(false);
                likePressed(user, eventId, Const.TYPE_DISLIKE);
                break;
        }
    }

    void likePressed(User user, String eventId, int likeType) {
        JSONObject jsonObject = APIManager.getInstance().likeEventJson(user, eventId, likeType);
        APIManager.getInstance().postRequestNoProgress(null, URLs.LIKE_EVENT, jsonObject.toString(), new LikeRequestListener());
    }

    private class LikeRequestListener extends BaseRequestListener {
        @Override
        public void onRequestSuccess(String result) {
            super.onRequestSuccess(result);
            getEventDetails(clickedEvent.getDetails().getId());
        }
    }

    public void getEventDetails(String eventId) {
        JSONObject jsonObject = APIManager.getInstance().eventDetailsJson(user, eventId);
        if (jsonObject != null)
            APIManager.getInstance().postRequestNoProgress(mContext, URLs.EVENT_DETAILS, jsonObject.toString(), new EventDetailsRequestListener());
    }

    private class EventDetailsRequestListener extends BaseRequestListener {
        @Override
        public void onRequestSuccess(String result) {
            super.onRequestSuccess(result);
//            mContext.stopProgress();
            try {
                JSONObject json = new JSONObject(result);

                if (json.getBoolean(Const.KEY_SUCCESS)) {
                    int protocolVersion = json.getInt(Const.KEY_VERSION);
                    final JSONObject data = json.getJSONObject(Const.KEY_DATA);
                    if (data != null) {
                        clickedEvent = EventInfoDeserializer.fromJson(protocolVersion, data);
                        clickedViewHolder.tvLikeCount.setText(String.valueOf(clickedEvent.getStats().getEventLike()));
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public interface ItemClickCallback {
        void onEventClick(int position);
    }

}
