package com.hippotec.mapsapplication.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.hippotec.mapsapplication.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Elior Ben-Yosef on 07/09/2017.
 */

public class ImageActivity extends AppCompatActivity {

    final public static String IMAGE_URL_STRING_EXTRA = "current image url string";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load(getIntent().getStringExtra(IMAGE_URL_STRING_EXTRA)).into(imageView);
    }
}
