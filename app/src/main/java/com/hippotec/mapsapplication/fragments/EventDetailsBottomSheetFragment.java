//package com.hippotec.mapsapplication.fragments;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.support.design.widget.BottomSheetDialogFragment;
//import android.view.View;
//import android.widget.TextView;
//
//import com.hippotec.mapsapplication.Const;
//import com.hippotec.mapsapplication.R;
//import com.hippotec.mapsapplication.activities.EventDetailsActivity;
//
//import com.hippotec.mapsapplication.model.EventDetails;
//import com.hippotec.mapsapplication.utils.TimeUtils;
//
//import java.util.Date;
//
///**
// * Created by Avishay Peretz on 09/04/2017.
// */
//
//public class EventDetailsBottomSheetFragment extends BottomSheetDialogFragment {
//
//    private TextView tvName, tvDesc;
//    private TextView tvTime;
//
//    private EventDetails mEvent;
//    private View container;
//
//    public void setEvent(EventDetails event) {
//        this.mEvent = event;
//
//    }
//
//    @Override
//    public void setupDialog(final Dialog dialog, int style) {
//        super.setupDialog(dialog, style);
//        View contentView = View.inflate(getContext(), R.layout.fragment_bottomsheet_event_details, null);
//        dialog.setContentView(contentView);
//
//        initializeViews(dialog);
//        setData();
//
//    }
//
//    private void setData() {
//        tvName.setText(mEvent.getName());
//        tvDesc.setText(mEvent.getDescription());
//        tvTime.setText(mEvent.getTimeRange());
//    }
//
//    private void initializeViews(final Dialog dialog) {
//
//        tvName = (TextView) dialog.findViewById(R.id.tv_event_name);
//        tvDesc = (TextView) dialog.findViewById(R.id.tv_event_desc);
//        tvTime = (TextView) dialog.findViewById(R.id.tv_event_time);
//
//        container = dialog.findViewById( R.id.bottom_sheet_event_details_container);
//        container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), EventDetailsActivity.class);
//                intent.putExtra(Const.KEY_PASS_EVENT, mEvent);
//                startActivity(intent);
//                dialog.dismiss();
//            }
//        });
//    }
//
//
//}
