package com.hippotec.mapsapplication.activities.base;

import android.content.Intent;

public interface SwitchableScreenActivityResultHandler {
    void onActivityResult(final int requestCode, final int resultCode, final Intent data);
}
