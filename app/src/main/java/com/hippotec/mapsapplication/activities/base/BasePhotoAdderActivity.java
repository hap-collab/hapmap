package com.hippotec.mapsapplication.activities.base;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.EventDetailsActivity;
import com.hippotec.mapsapplication.activities.ImagePreviewActivity;
import com.hippotec.mapsapplication.activities.MapsActivity;
import com.hippotec.mapsapplication.activities.TakePhotoActivity;
import com.hippotec.mapsapplication.utils.PermissionsChecker;

import java.io.File;

/**
 * Created by Elior on 11/07/2017.
 */

public abstract class BasePhotoAdderActivity extends BaseUserActivity {
    private File saveFile;

    public static final int RESULT_PICK_IMAGE = 601;
    public static final int RESULT_TAKE_IMAGE = 602;
    public static final int REQUEST_EVENT_PHOTO = 603;
    public static final int REQUEST_COMMENT_PHOTO = 604;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addPhoto(View vAddPhoto) {
        hideKeyboard(vAddPhoto);
        openContextMenu(vAddPhoto);
    }

    public void showPhotoDialog(int requestCode, String title, ImagePreviewActivity.OnImagePreviewReady onImagePreviewReady, Bundle extras) {
        Intent intent = new Intent(this, TakePhotoActivity.class);
        intent.putExtras(extras);

        ImagePreviewActivity.onImagePreviewReady = onImagePreviewReady;
        //intent.putExtra(Const.KEY_PASS_EVENT, event);
        //intent.putExtra(Const.KEY_PASS_EVENT+"mapCenter", mapCenter);
        //intent.putExtra(Const.KEY_PASS_EVENT+"radius", radius);
        startActivityForResult(intent, requestCode);
        /*final Dialog photoDialog = new Dialog(this);
        photoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        photoDialog.setContentView(R.layout.dialog_acquire_image);

        TextView tvTitle = (TextView) photoDialog.findViewById(R.id.tv_acquire_image_header);
        tvTitle.setText(title);

        String locationStr = location.toString();
        TextView tvLocation = (TextView) photoDialog.findViewById(R.id.tv_acquire_image_cur_location);
        tvLocation.setText(locationStr);

        (photoDialog.findViewById(R.id.take_photo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog.dismiss();
                onPhotoAcquisitionInitiatedListener.onPhotoAcquisitionInitiated();
                takePhotoWrapper();
            }
        });

        (photoDialog.findViewById(R.id.upload_existing_photo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog.dismiss();
                onPhotoAcquisitionInitiatedListener.onPhotoAcquisitionInitiated();
                pickPhotoWrapper();
            }
        });

        photoDialog.show();*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId()== R.id.add_photo) {
            getMenuInflater().inflate(R.menu.dialog_image_menu, menu);
            menu.findItem(R.id.upload_existing_photo).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    pickPhotoWrapper();
                    return true;
                }
            });
            menu.findItem(R.id.take_new_photo).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    takePhotoWrapper();
                    return true;
                }
            });
        }
    }

    private void pickPhotoWrapper() {
        if (getPermissionsChecker().attemptStoragePermission()) {
            pickPhoto();
        }
    }

    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_PICK_IMAGE);

        // Goes directly to gallery:
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, RESULT_PICK_IMAGE);

    }

    private void takePhotoWrapper() {
        if (getPermissionsChecker().isStorageAvailable()) {
            takePhoto();
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Long timeStampLong = System.currentTimeMillis()/1000;
        saveFile = new File(Environment.getExternalStorageDirectory(), timeStampLong.toString() + ".jpg");
        //Uri saveFileUri = Uri.fromFile(saveFile);
        Uri saveFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", saveFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileUri);
        startActivityForResult(intent, RESULT_TAKE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_TAKE_IMAGE) {
                getTakenImagePath(saveFile.getAbsolutePath());
            }
            if (requestCode == REQUEST_EVENT_PHOTO) {
                getTakenImagePath(data.getData().getPath());
            }
            if (requestCode == REQUEST_COMMENT_PHOTO) {
                getTakenImagePath(data.getData().getPath());
            }
        }
    }

    public abstract void getTakenImagePath(String mSelectedImagePath);


}
