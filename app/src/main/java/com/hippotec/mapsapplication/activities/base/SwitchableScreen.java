package com.hippotec.mapsapplication.activities.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public interface SwitchableScreen {
    public void onCreate(Bundle savedInstanceState);
    public void onActivityResult(int requestCode, int resultCode, Intent data);
}
