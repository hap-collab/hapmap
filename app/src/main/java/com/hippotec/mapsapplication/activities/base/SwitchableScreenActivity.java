package com.hippotec.mapsapplication.activities.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.widget.ViewSwitcher;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public abstract class SwitchableScreenActivity extends BaseActivity {
    private int m_layoutId;
    private int m_viewSwitcherId;
    public ViewSwitcher m_viewSwitcher = null;
    private SwitchableScreen m_currentScreen = null;
    private ArrayList<SwitchableScreen> m_screens = new ArrayList<SwitchableScreen>();
    private Runnable m_finalAction;
    private SwitchableScreenActivityResultHandler m_activityResultHandler = null;
    private Bundle m_state;

    public SwitchableScreenActivity(@LayoutRes int layoutResID, @LayoutRes int viewSwitcherId) {

        m_layoutId = layoutResID;
        m_viewSwitcherId = viewSwitcherId;
    }

    public void addScreen(SwitchableScreen scr) {
        m_screens.add(scr);
    }
    public void setFinalAction(Runnable r) { m_finalAction = r; }

    public void assignSharedValue(String tag, String value) {

        if (m_state == null) {
            m_state = new Bundle();
        }
        m_state.putString(tag, value);
    }
    public String getSharedValue(String tag) {
        return m_state.getString(tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(m_layoutId);

        super.onCreate(savedInstanceState);
        m_viewSwitcher = (ViewSwitcher) findViewById(m_viewSwitcherId);
        assert(m_viewSwitcher != null);

        for (SwitchableScreen s : m_screens) {
            s.onCreate(savedInstanceState);
        }
        m_currentScreen = m_screens.get(0);
        m_state = savedInstanceState;
    }

    public void skipCurrentScreen() {
        nextScreen();
    }

    public void nextScreen(String thisScreensValTag, String thisScreensVal) {
        assignSharedValue(thisScreensValTag, thisScreensVal);
        nextScreen();
    }

    private void nextScreen() {
        if (m_viewSwitcher.getDisplayedChild() + 1 < m_screens.size()) {
            m_currentScreen = m_screens.get(m_viewSwitcher.getDisplayedChild() + 1);
            m_viewSwitcher.showNext();
        }
        else {
            m_finalAction.run();
        }
    }

    @Override
    public void onBackPressed() {
        if (m_viewSwitcher.getDisplayedChild() == 0)
            super.onBackPressed();
        else {
            m_viewSwitcher.showPrevious();
        }
    }

    public void startActivityFinish(@NonNull SwitchableScreenActivityResultHandler resultHandler, @NonNull Intent intent) {
        m_activityResultHandler = resultHandler;
        startActivityFinish(intent);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        m_activityResultHandler.onActivityResult(requestCode, resultCode, data);
    }

}
