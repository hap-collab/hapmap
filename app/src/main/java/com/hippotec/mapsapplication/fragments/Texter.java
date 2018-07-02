package com.hippotec.mapsapplication.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.hippotec.mapsapplication.R;

import butterknife.ButterKnife;

public class Texter extends LinearLayout {
    public Texter(Context context) {
        this(context, null);
    }

    public Texter(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Texter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.texter, this);
        ButterKnife.bind(this);

        if (attrs != null) {
            /*TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CameraControls,
                    0, 0);

            try {
                cameraViewId = a.getResourceId(R.styleable.CameraControls_camera, -1);
                coverViewId = a.getResourceId(R.styleable.CameraControls_cover, -1);
            } finally {
                a.recycle();
            }*/
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }
}
