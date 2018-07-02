package com.hippotec.mapsapplication.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.hippotec.mapsapplication.R;


/**
 * Created by Avishay Peretz on 06/04/2017.
 */
public class CustomProgressDialog extends ProgressDialog {

    public CustomProgressDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        setCancelable(true);
        setIndeterminate(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
    }
}
