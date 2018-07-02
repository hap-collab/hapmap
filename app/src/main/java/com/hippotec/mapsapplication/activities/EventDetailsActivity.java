package com.hippotec.mapsapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.base.BasePhotoAdderActivity;
import com.hippotec.mapsapplication.adapters.CommentsAdapter;
import com.hippotec.mapsapplication.api.APIManager;
import com.hippotec.mapsapplication.api.URLs;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
import com.hippotec.mapsapplication.model.Comment;
import com.hippotec.mapsapplication.model.EventInfo;
import com.hippotec.mapsapplication.model.EventUserInfo;
import com.hippotec.mapsapplication.model.User;
import com.hippotec.mapsapplication.utils.ArrayDeserializer;
import com.hippotec.mapsapplication.utils.CommentDeserializer;
import com.hippotec.mapsapplication.utils.EventInfoDeserializer;
import com.hippotec.mapsapplication.utils.GooglePlacesAsyncFetcher;
import com.hippotec.mapsapplication.utils.ImageUtils;
import com.hippotec.mapsapplication.utils.PermissionsChecker;
import com.hippotec.mapsapplication.utils.Toaster;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import static com.hippotec.mapsapplication.activities.MapsActivity.mMapsActivityLocationsRequestListener;

/**
 * Created by Avishay Peretz on 09/04/2017.
 */
public class EventDetailsActivity extends BasePhotoAdderActivity implements CommentsAdapter.ImageClickCallback {

    //@BindView(R.id.event_details_img)
    //ImageView eventImg;

    @BindView(R.id.tv_event_name)
    TextView tvName;
    @BindView(R.id.tv_event_desc)
    TextView tvDesc;
    @BindView(R.id.tv_likes_count)
    TextView tvLikeCount;
    //@BindView(R.id.tv_event_time)
    //TextView tvTime;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_female_count)
    TextView tvFemaleCount;
    @BindView(R.id.tv_male_count)
    TextView tvMaleCount;

    @BindView(R.id.like_btn)
    SwitchCompat likeBtn;
    @BindView(R.id.dislike_btn)
    SwitchCompat dislikeBtn;

    @BindView(R.id.et_add_comment)
    EditText etAddComment;

    @BindView(R.id.add_photo_comment)
    ImageView addImageBtn;

    @BindView(R.id.comments_recycler_view)
    RecyclerView commentsRecyclerView;



    private EventInfo mEvent;

    private CommentsAdapter commentsAdapter;
    private List<Comment> commentsList;

    private String currentImageUrlString;

    private int radius;
    //private int count;
    private LatLng mapCenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_details); // activity_event_details

        initializeViews();
        setData();
        getAllComments();
//        setEventDetailsFragment();
    }




    //@Override
    protected void initializeViews() {
        ButterKnife.bind(EventDetailsActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
        commentsRecyclerView.setLayoutManager(linearLayoutManager);
        commentsList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(this, commentsList);
        commentsAdapter.registerCallback(this);
        commentsRecyclerView.setAdapter(commentsAdapter);
    }

    @Override
    public void onImageClick(String commentImageUrlString) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra(ImageActivity.IMAGE_URL_STRING_EXTRA, commentImageUrlString);
        startActivity(intent);
    }

    /*@OnClick(R.id.event_details_img)
    void imageClick() {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra(ImageActivity.IMAGE_URL_STRING_EXTRA, URLs.BASE_URL + "/" + mEvent.getDetails().getImage());
        startActivity(intent);
    }*/

    private void setData() {
        mapCenter = getIntent().getParcelableExtra(Const.KEY_PASS_EVENT + "mapCenter");
        //radius= getIntent().getParcelableExtra(Const.KEY_PASS_EVENT+"radius");
        //radius=Const.RADIUS_IN_METERS;
        //count= getIntent().getParcelableExtra(Const.KEY_PASS_EVENT+"count");
        radius = getIntent().getIntExtra(Const.KEY_PASS_EVENT+"radius",0);
        //count = getIntent().getIntExtra(Const.KEY_PASS_EVENT+"count",0);

        mEvent = getIntent().getParcelableExtra(Const.KEY_PASS_EVENT);
        /*if (!TextUtils.isEmpty(mEvent.getDetails().getImage())) {
            Picasso.with(this).load(URLs.BASE_URL + "/" + mEvent.getDetails().getImage())
//                .placeholder(getResources().getDrawable(R.drawable.picture_placeholder))
                    .into(eventImg);
        }*/



        getEventDetails(mEvent.getDetails().getId());//Rami - fixes the missing Like/Dislike on bottomsheet press
        //tvLikeCount.setText(String.valueOf(mEvent.getEventLike()));
        //likeBtn.setChecked(mEvent.isUserLiked());
        //dislikeBtn.setChecked(mEvent.isUserDisliked());

        tvName.setText(mEvent.getDetails().getName());
        tvDesc.setText(mEvent.getDetails().getDescription());
        //tvTime.setText(mEvent.getDetails().getTimeRange());
        tvFemaleCount.setText(mEvent.getStats().getFemalePercent());
        tvMaleCount.setText(mEvent.getStats().getMalePercent());
        tvTotal.setText(mEvent.getStats().getTotalAgeLine(getString(R.string.totalCount), getString(R.string.age)));

        registerForContextMenu(addImageBtn);
    }




    private void getAllComments() {
        JSONObject json = new JSONObject();
        try {
            json.put(Const.KEY_EVENT_ID, mEvent.getDetails().getId());
            APIManager.getInstance().postRequest(this, URLs.ALL_COMMENTS, json.toString(), new AllCommentsRequestListener());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class AllCommentsRequestListener extends BaseRequestListener {
        @Override
        public void onRequestFailure(String err) {
            super.onRequestFailure(err);
            stopProgress();
        }

        @Override
        public void onRequestSuccess(String result) {
            super.onRequestSuccess(result);
            try {
                JSONObject json = new JSONObject(result);
                if (json.getBoolean(Const.KEY_SUCCESS)) {
                    int protocolVersion = json.getInt(Const.KEY_VERSION);
                    commentsList.clear();
                    final JSONObject data = json.getJSONObject(Const.KEY_DATA);
                    if (data != null) {
                        ArrayDeserializer.fromJson(protocolVersion, data, Const.KEY_COMMENTS, commentsList, new ArrayDeserializer.ItemDeserializer<Comment>() {
                            @Override
                            public Comment fromJson(int protocolVersion, JSONObject json) throws JSONException {
                                return CommentDeserializer.fromJson(mEvent.getDetails(), protocolVersion, json);
                            }
                        });

//                        commentsAdapter = new CommentsAdapter(EventDetailsActivity.this, commentsList);
//                        commentsRecyclerView.setAdapter(commentsAdapter);
                        commentsAdapter.setComments(commentsList);
                        commentsAdapter.notifyDataSetChanged();
                        commentsRecyclerView.scrollToPosition(commentsAdapter.getItemCount() - 1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            stopProgress();
        }
    }




/*
    private void setEventDetailsFragment() {
        EventDetailsFragment fragment = new EventDetailsFragment();
        fragment.setEvent(new EventDetails());

        if (fragment != null) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.event_details_fragment, fragment);
            fragmentTransaction.commit();
        }
    }
*/

    @OnClick(R.id.add_comment_btn)
    void addComment() {
        if (!etAddComment.getText().toString().trim().equals("")) {
            getCurrentFocus().clearFocus();
            hideSoftKeyboard(this);
            JSONObject json = APIManager.getInstance().addCommentJson(
                    user, mEvent.getDetails(), etAddComment.getText().toString());
            if (json != null) {
                APIManager.getInstance().postJsonWithImageRequest(
                        this, URLs.ADD_COMMENT,
                        json.toString(), null, null,
                        new AddCommentRequestListener());
            }
            etAddComment.setText("");
        }
    }

    private class AddCommentRequestListener extends BaseRequestListener {
        @Override
        public void onRequestFailure(String err) {
            super.onRequestFailure(err);
            stopProgress();
            commentAddFailed();
        }

        @Override
        public void onRequestSuccess(String result) {
            super.onRequestSuccess(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean(Const.KEY_SUCCESS)) {
                    getAllComments();
//                    commentsList.add(new Comment(null, user.getId(), mEvent.getId(), etAddComment.getText().toString(), mSelectedImagePath));
//                    commentsAdapter.notifyItemInserted(commentsList.size());
                } else {
                    commentAddFailed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            stopProgress();
        }
    }

    private void commentAddFailed() {
        Toast.makeText(EventDetailsActivity.this, "comment add failed", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.add_photo_comment)
    void addImage() {
        //addPhoto(addImageBtn);
        Bundle extras = new Bundle();
        extras.putBoolean(ImagePreviewActivity.PARAM_KEY_IS_EVENT_CREATION, false);
        showPhotoDialog(REQUEST_COMMENT_PHOTO,"Add Photo", new ImagePreviewActivity.OnImagePreviewReady() {
            public void onImagePreviewReady(ImagePreviewActivity previewActivity) {
            }
        }, extras);
    }



    @OnClick(R.id.like_btn_wrapper)
    void like1() {
        handleLikeDislike(mEvent.getUserInfo().isUserLiked() ? Const.TYPE_UNLIKE : Const.TYPE_LIKE);
    }

    @OnClick(R.id.like_btn)
    void like2() {
        handleLikeDislike(mEvent.getUserInfo().isUserLiked() ? Const.TYPE_UNLIKE : Const.TYPE_LIKE);
    }

/*    @OnClick(R.id.like_btn)
    void like() {
        dislikeBtn.setChecked(false);

        //likePressed(user.getId(), mEvent.getId(), Const.TYPE_LIKE);
        if(mEvent.isUserLiked()){
            likePressed(user.getId(), mEvent.getId(), Const.TYPE_DISLIKE);
        }else if(!mEvent.isUserLiked()) {
            likePressed(user.getId(), mEvent.getId(), Const.TYPE_LIKE);
        }
    }*/




    @OnClick(R.id.dislike_btn_wrapper)
    void dislike1() {
        handleLikeDislike(mEvent.getUserInfo().isUserDisliked() ? Const.TYPE_UNLIKE : Const.TYPE_DISLIKE);
    }

    @OnClick(R.id.dislike_btn)
    void dislike2() {
        handleLikeDislike(mEvent.getUserInfo().isUserDisliked() ? Const.TYPE_UNLIKE : Const.TYPE_DISLIKE);
    }

/*    @OnClick(R.id.dislike_btn)
    void dislike() {
        likeBtn.setChecked(false);
        likePressed(user.getId(), mEvent.getId(), Const.TYPE_DISLIKE);
    }*/

    void handleLikeDislike(int newLikeType) {
        EventUserInfo evernUserInfo = mEvent.getUserInfo();

        {
            boolean isLiked = (newLikeType == Const.TYPE_LIKE);
            mEvent.getUserInfo().setUserLiked(isLiked);
            likeBtn.setChecked(isLiked);
        }
        {
            boolean isLiked = (newLikeType == Const.TYPE_DISLIKE);
            mEvent.getUserInfo().setUserDisliked(isLiked);
            dislikeBtn.setChecked(isLiked);
        }

        String eventId = mEvent.getDetails().getId();
        JSONObject jsonObject = APIManager.getInstance().likeEventJson(user, eventId, newLikeType);
        APIManager.getInstance().postRequestNoProgress(null, URLs.LIKE_EVENT, jsonObject.toString(), new LikeRequestListener());

        //API call for events - start
        //JSONObject locationsJsonObject = APIManager.getInstance().locationsJson(user, mapCenter, radius);
        //APIManager.getInstance().postRequest(this, URLs.LOCATIONS, locationsJsonObject.toString(), new MapsActivity.LocationsRequestListener());
        //getEventDetails(eventId);
    }

    private class LikeRequestListener extends BaseRequestListener {
        @Override
        public void onRequestSuccess(String result) {
            super.onRequestSuccess(result);
            getEventDetails(mEvent.getDetails().getId());
        }
    }

    public void getEventDetails(String eventId) {
        JSONObject jsonObject = APIManager.getInstance().eventDetailsJson(user, eventId);
        if (jsonObject != null)
            APIManager.getInstance().postRequestNoProgress(this, URLs.EVENT_DETAILS, jsonObject.toString(), new EventDetailsRequestListener());
    }

    private class EventDetailsRequestListener extends BaseRequestListener {
        @Override
        public void onRequestSuccess(String result) {
            super.onRequestSuccess(result);
//            stopProgress();
            try {
                JSONObject json = new JSONObject(result);

                if (json.getBoolean(Const.KEY_SUCCESS)) {
                    int protocolVersion = json.getInt(Const.KEY_VERSION);
                    final JSONObject data = json.getJSONObject(Const.KEY_DATA);
                    if (data != null) {
                        mEvent = EventInfoDeserializer.fromJson(protocolVersion, data);
                        tvLikeCount.setText(String.valueOf(mEvent.getStats().getEventLike()));

                        //Rami - fixes the missing Like/Dislike on bottomsheet press
                        likeBtn.setChecked(mEvent.getUserInfo().isUserLiked());
                        dislikeBtn.setChecked(mEvent.getUserInfo().isUserDisliked());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsChecker.REQUEST_PERMISSION_STORAGE:
                openContextMenu(addImageBtn);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_COMMENT_PHOTO) {
                //byte[] mSelectedImageByte = null;
                String selectedImagePath = ImageUtils.getPath(this, data.getData());
                //Bitmap bmp = null;
                /*if (selectedImagePath == null) {
                    try {
                        //bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                        mSelectedImageByte  = IOUtils.toByteArray(getContentResolver().openInputStream(data.getData()));
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }*/

                String comment = data.getStringExtra(ImagePreviewActivity.RESULT_KEY_COMMENT);
                JSONObject json = APIManager.getInstance().addCommentJson(
                        user, mEvent.getDetails(), comment);
                if (json != null) {
                    APIManager.getInstance().postJsonWithImageRequest(
                            this, URLs.ADD_COMMENT,
                            json.toString(), selectedImagePath, null,
                            new AddCommentRequestListener());
                }
            }
        }
        else if (resultCode == ImagePreviewActivity.RESULT_IMAGE_SAVE_ERROR) {
            String errMsg = data.getStringExtra(ImagePreviewActivity.RESULT_KEY_ERROR);
            if ((errMsg != null) && !errMsg.isEmpty()) {
                Toaster.longToast(this, errMsg);
            }
        }
    }

    @Override
    public void getTakenImagePath(String selectedImagePath) {
    }
}
