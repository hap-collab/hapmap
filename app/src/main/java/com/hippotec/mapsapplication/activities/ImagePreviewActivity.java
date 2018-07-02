package com.hippotec.mapsapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Spinner;

import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.PhotoResultHolder;
import com.hippotec.mapsapplication.activities.base.BaseUserActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImagePreviewActivity extends BaseUserActivity /* AppCompatActivity */ {
    public static final String PARAM_KEY_IS_EVENT_CREATION = "isEventCreation";

    //public static final String RESULT_KEY_IMAGE = "image";
    public static final String RESULT_KEY_EVENT_NAME = "eventName";
    public static final String RESULT_KEY_COMMENT = "comment";
    public static final String RESULT_KEY_PLACE_IDX = "placeIdx";
    public static final String RESULT_KEY_PLACE_DESC = "place";
    public static final String RESULT_KEY_ERROR = "error";
    public static final int RESULT_IMAGE_SAVE_ERROR = RESULT_FIRST_USER;

    public interface OnImagePreviewReady {
        void onImagePreviewReady(ImagePreviewActivity previewActivity);
    }

    public static OnImagePreviewReady onImagePreviewReady;

    @BindView(R.id.image_comment_layout)
    View layoutComment;

    @BindView(R.id.image_event_description_layout)
    View layoutEventDescription;

    @BindView(R.id.previewed_image)
    ImageView imageView;

    @BindView(R.id.spinner_google_place)
    Spinner m_spinnerPlaces;

    //@BindView(R.id.video)
    //VideoView videoView;

    //@BindView(R.id.actualResolution)
    //TextView actualResolution;

    //@BindView(R.id.approxUncompressedSize)
    //TextView approxUncompressedSize;

    @BindView(R.id.et_image_preview_comment)
    EditText m_etImagePreviewComment;

    @BindView(R.id.et_image_preview_event_name)
    EditText m_etImagePreviewEventName;

    @BindView(R.id.et_image_preview_event_desc)
    EditText m_etImagePreviewEventDescription;

    public Spinner getPlacesSpinner() {
        return m_spinnerPlaces;
    }

    private File imageSaver(Bitmap bitmap) throws IOException
    {
        File picsFolder = this.getPermissionsChecker().isStorageAvailable()
                ? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                : this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File bitmapFile = new File(picsFolder, imageFileName);
        try {
            if (!picsFolder.exists()) {
                picsFolder.mkdirs();
            }
            
            FileOutputStream out = new FileOutputStream(bitmapFile);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            }
            finally {
                out.close();
                bitmap.recycle();
            }

            return bitmapFile.exists() ? bitmapFile : null;
        }
        catch (IOException e) {
            e.printStackTrace();
            if (bitmapFile.exists()) {
                bitmapFile.delete();
            }
            throw e;
        }
    }

    @OnClick(R.id.btn_image_preview_add)
    void addCommentedImage() {
        Intent result = new Intent();
        if (addImageToIntent(result)) {
            result.putExtra(RESULT_KEY_COMMENT, m_etImagePreviewComment.getText().toString());
            setResult(RESULT_OK, result);
        }
        else {
            setResult(RESULT_IMAGE_SAVE_ERROR, result);
        }
        finish();
    }

    private boolean addImageToIntent(Intent result)
    {
        //result.putExtra(RESULT_KEY_IMAGE, PhotoResultHolder.getImage());
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(PhotoResultHolder.getImage(), 0, PhotoResultHolder.getImage().length);
            File pic = imageSaver(bitmap);
            if (pic == null) {
                result.putExtra(RESULT_KEY_ERROR, "Error writing to image file");
                return false;
            }
            result.setData(Uri.fromFile(pic));

            //int thumbW = getIntent().getIntExtra(PARAM_THUMBNAIL_W, 0);
            //int thumbH = getIntent().getIntExtra(PARAM_THUMBNAIL_H, 0);
            //if ((thumbW >= 0) && (thumbH >= 0)) {
            //    byte[] thumbnail = createThumbnail(bitmap, thumbW, thumbH);
            //    result.putExtra(RESULT_KEY_IMAGE, thumbnail);
            //}
            return true;
        }
        catch (IOException e) {
            result.putExtra(RESULT_KEY_ERROR, e.getMessage());
            return false;
        }
    }

    /*private byte[] createThumbnail(Bitmap bitmap, int thumbW, int thumbH) {
        // Get the dimensions of the bitmap
        int photoW = bitmap.getWidth();
        int photoH = bitmap.getHeight();

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / thumbW, photoH / thumbH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap.
        Bitmap bitmap = BitmapFactory.decodeByteArray(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }*/

    @OnClick(R.id.btn_image_preview_create_event)
    void createEvent()
    {
        Intent result = new Intent();
        if (addImageToIntent(result)) {
            result.putExtra(RESULT_KEY_EVENT_NAME, m_etImagePreviewEventName.getText().toString());
            result.putExtra(RESULT_KEY_COMMENT, m_etImagePreviewEventDescription.getText().toString());
            result.putExtra(RESULT_KEY_PLACE_DESC, (String)m_spinnerPlaces.getSelectedItem());
            result.putExtra(RESULT_KEY_PLACE_IDX, m_spinnerPlaces.getSelectedItemId());
            setResult(RESULT_OK, result);
        }
        else {
            setResult(RESULT_IMAGE_SAVE_ERROR, result);
        }
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        ButterKnife.bind(this);

        setupToolbar();

        byte[] jpeg = PhotoResultHolder.getImage();
        File video = PhotoResultHolder.getVideo();

        if (jpeg != null) {
            imageView.setVisibility(View.VISIBLE);

            Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
            if (bitmap == null) {
                setResult(RESULT_CANCELED);
                finish();
                return;
            }

            imageView.setImageBitmap(bitmap);

            /*actualResolution.setText(bitmap.getWidth() + " x " + bitmap.getHeight());
            approxUncompressedSize.setText(getApproximateFileMegabytes(bitmap) + "MB");
            captureLatency.setText(PhotoResultHolder.getTimeToCallback() + " milliseconds");*/
        }

        /*else if (video != null) {
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(video.getAbsolutePath()));
            MediaController mediaController = new MediaController(this);
            mediaController.setVisibility(View.GONE);
            videoView.setMediaController(mediaController);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    mp.start();

                    float multiplier = (float) videoView.getWidth() / (float) mp.getVideoWidth();
                    videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (mp.getVideoHeight() * multiplier)));
                }
            });
            //videoView.start();
        }*/

        else {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        Intent intent = getIntent();
        if (intent.getBooleanExtra(PARAM_KEY_IS_EVENT_CREATION, false)) {
            layoutComment.setVisibility(View.GONE);
            layoutEventDescription.setVisibility(View.VISIBLE);
        }
        else {
            layoutComment.setVisibility(View.VISIBLE);
            layoutEventDescription.setVisibility(View.GONE);
        }

        if (onImagePreviewReady != null) {
            onImagePreviewReady.onImagePreviewReady(this);
        }
    }

    private void setupToolbar() {
        /*if (getSupportActionBar() != null) {
            View toolbarView = getLayoutInflater().inflate(R.layout.action_bar, null, false);
            TextView titleView = toolbarView.findViewById(R.id.toolbar_title);
            titleView.setText(Html.fromHtml("<b>Camera</b>Kit"));

            getSupportActionBar().setCustomView(toolbarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }*/
    }

    private static float getApproximateFileMegabytes(Bitmap bitmap) {
        return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024 / 1024;
    }

}
/*
myscrollview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View lastChild = myscrollview.getChildAt(myscrollview.getChildCount() - 1);
                            int bottom = lastChild.getBottom() + myscrollview.getPaddingBottom();
                            int sy = myscrollview.getScrollY();
                            int sh = myscrollview.getHeight();
                            int delta = bottom - (sy + sh);
                            myscrollview.smoothScrollBy(0, delta);
                        }
                    }, 200);
 */
