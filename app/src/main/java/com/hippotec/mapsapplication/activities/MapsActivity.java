package com.hippotec.mapsapplication.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.azurechen.fcalendar.data.CalendarAdapter;
import com.azurechen.fcalendar.data.Day;
import com.azurechen.fcalendar.widget.FlexibleCalendar;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
/*import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;*/
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.hippotec.mapsapplication.AlarmReceiver;
import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.R;
import com.hippotec.mapsapplication.activities.base.BasePhotoAdderActivity;
import com.hippotec.mapsapplication.adapters.EventsBottomSheetAdapter;
import com.hippotec.mapsapplication.api.APIManager;
import com.hippotec.mapsapplication.api.LikelyPlacesFinder;
import com.hippotec.mapsapplication.api.URLs;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
import com.hippotec.mapsapplication.model.EntityType;
import com.hippotec.mapsapplication.model.EventDetails;
import com.hippotec.mapsapplication.model.EventAndPeopleObject;
import com.hippotec.mapsapplication.model.EventInfo;
import com.hippotec.mapsapplication.model.EventTime;
import com.hippotec.mapsapplication.utils.ArrayDeserializer;
import com.hippotec.mapsapplication.utils.CredentialsManager;
import com.hippotec.mapsapplication.utils.EventInfoDeserializer;
import com.hippotec.mapsapplication.utils.GooglePlacesAsyncFetcher;
import com.hippotec.mapsapplication.utils.ImageUtils;
import com.hippotec.mapsapplication.utils.Logger;
import com.hippotec.mapsapplication.utils.PermissionsChecker;
import com.hippotec.mapsapplication.utils.PreferenceCache;
import com.hippotec.mapsapplication.utils.TimeUtils;
import com.hippotec.mapsapplication.utils.Toaster;
//import com.wefika.calendar.CollapseCalendarView;
//import com.wefika.calendar.manager.CalendarManager;
//import com.wefika.calendar.manager.Day;

//import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.apache.commons.io.IOUtils;
/*import org.joda.time.LocalDate;*/
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import static com.google.android.gms.location.places.Places.*;

//import static com.hippotec.mapsapplication.R.id.eventDatePicker;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */

public class MapsActivity extends BasePhotoAdderActivity
        implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks, View.OnClickListener, GoogleMap.OnMarkerClickListener,
        EventsBottomSheetAdapter.ItemClickCallback, GoogleApiClient.OnConnectionFailedListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MapsActivity";

    private GoogleApiClient mGoogleApiClient;
    private PermissionsChecker permissionsChecker;
    private GoogleMap googleMap;
    private View mapView;
    private LatLng mapCenter;
    private boolean mapTouched, isInit = true; //, fromSeekBar;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    long INTERVAL_5_MINUTES = 5 * TimeUtils.MINUTE;

    private TextView /*tvDate,*/ tvStartTime, tvEndTime;
    private EditText etEventName, etEventDesc;

    //private int seekBarPreviousValue;


    @BindView(R.id.events_bottom_recycler_view)
    RecyclerView mEventsRecyclerView;

    //@BindView(R.id.seek_bar)
    //DiscreteSeekBar seekBar;

    //@BindView(R.id.img_user)
    //ImageView userImg;


    private EventsBottomSheetAdapter mEventsBottomSheetAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<EventInfo> mEventsList;

    private LatLng mNewEventLocation;
    private BottomSheetBehavior<View> mBottomSheetBehavior, mBottomSheetBehavior1;

    private List<EventAndPeopleObject> mMapObjectsList;
    private List<Marker> mMarkersList;
    private EventAndPeopleObject selectedEventOrPeopleObject;
    private FloatingActionButton eventDetailsFAB;
    private String mSelectedImagePath;
    //private LinearLayout llAddPhoto;

    private Dialog createEventDialog;
    private ImageView eventImage;

    private int radius;
    private byte[] mSelectedImageByte;

    private Handler getEventsAndUsersHandler = new Handler();
    private Runnable getEventsAndUsersRunnable = new Runnable() {
        @Override
        public void run() {
            getEventsAndUsers();
        }
    };


    private boolean fromMarkerOrEventItemClick;

    public boolean expandCollpaseFlag;//for switching between expand and collapsed
    public long startTimeSpecificDate;//for specific date API call
    public long endTimeSpecificDate;//for specific date API call
    //public boolean specificDateFlag;//Flag to decide if we use specifric date API call
    //public Date todayDay;//Used for check in calendar and in expandCollpaseTextViewDate showing current/specific date
    //public Day selectedDay;//Used for check in calendar and in expandCollpaseTextViewDate showing current/specific date
    public TextView m_expandCollpaseTextViewDate;//showing current/specific date

    //public static LocationsRequestListener mMapsActivityLocationsRequestListener;
    //public int count = 1;

    private GooglePlacesAsyncFetcher m_placesFetcher = null;
    //private volatile boolean m_useCustomLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //specificDateFlag=false;//Flag to decide if we use specifric date API call

        initializeViews();

        //collapse_expand_calendar();//collapse calendar and expandCollpaseTextViewDate show

        //permissionsChecker = new PermissionsChecker(MapsActivity.this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(MapsActivity.this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */,
                            this /* OnConnectionFailedListener */)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

        m_expandCollpaseTextViewDate=(TextView)findViewById(R.id.expandCollpaseTextViewDate);
        m_expandCollpaseTextViewDate.setVisibility(View.VISIBLE);
        setExpandCollapseHeader();

        setAlarmManager();
    }

    private void setExpandCollapseHeader() {
        Date now = new Date();
        m_expandCollpaseTextViewDate.setText(String.valueOf(now.getDate()) + "/" + String.valueOf(now.getMonth()+1) + "/" + String.valueOf(1900+now.getYear()));
        m_expandCollpaseTextViewDate.setVisibility(View.VISIBLE);
    }

    /*private void collapse_expand_calendar() {
        final ImageView buttonPickDate=(ImageView)findViewById(R.id.buttonPickDate);
        final ImageView expandCollpaseImage=(ImageView)findViewById(R.id.expandCollpaseImage);
        final RelativeLayout expandCollpase=(RelativeLayout)findViewById(R.id.expandCollpase);
        final LinearLayout calendarLinearWrapper=(LinearLayout)findViewById(R.id.calendarLinearWrapper);
        final Button todayEvents=(Button)findViewById(R.id.todayEvents);
        final Button abortCalendar=(Button)findViewById(R.id.abortCalendar);
        final FlexibleCalendar viewCalendar = (FlexibleCalendar) findViewById(R.id.calendar);
        final Bitmap expandCollpaseFlagExpand = BitmapFactory.decodeResource(this.getResources(), R.drawable.icn_arrow_down);
        final Bitmap expandCollpaseFlagCollapsed = BitmapFactory.decodeResource(this.getResources(), R.drawable.icn_arrow_up);
        calendarLinearWrapper.setVisibility(View.INVISIBLE);//instead of viewCalendar.setVisibility(View.INVISIBLE);expandCollpase.setVisibility(View.INVISIBLE);todayEvents.setVisibility(View.INVISIBLE);abortCalendar.setVisibility(View.INVISIBLE);

        //initiate calendar - start
        final Calendar cal = Calendar.getInstance();
        CalendarAdapter adapter = new CalendarAdapter(this, cal);
        viewCalendar.setAdapter(adapter);
        //initiate calendar - end

        //animate calendar layout - start
        calendarLinearWrapper.animate()
                .translationY(-(calendarLinearWrapper.getHeight()))
                .alpha(0.0f)
                .setListener(null);
        //animate calendar layout - end

        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //First Collapse - start
                viewCalendar.collapse(0);
                expandCollpaseFlag=true;
                expandCollpaseImage.setImageBitmap(expandCollpaseFlagExpand);
                //First Collapse - end

                //animate calendar layout - start
                calendarLinearWrapper.animate()
                        .translationY(0)
                        .alpha(1.0f)
                        .setListener(null);
                //animate calendar layout - end

                buttonPickDate.setVisibility(View.INVISIBLE);
                expandCollpaseTextViewDate.setVisibility(View.INVISIBLE);
                calendarLinearWrapper.setVisibility(View.VISIBLE);
            }
        });
        todayEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPickDate.setVisibility(View.VISIBLE);
                expandCollpaseTextViewDate.setVisibility(View.VISIBLE);

                //animate calendar layout - start
                calendarLinearWrapper.animate()
                        .translationY(-(calendarLinearWrapper.getHeight()))
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                calendarLinearWrapper.setVisibility(View.INVISIBLE);
                            }
                        });
                //animate calendar layout - end

                //Collapse - start
                viewCalendar.collapse(0);
                expandCollpaseFlag=true;
                expandCollpaseImage.setImageBitmap(expandCollpaseFlagExpand);
                //Collapse - end

                //Sets the week back to original and date to today - start
                todayDay=new Day(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                viewCalendar.select(todayDay);//viewCalendar.select(new Day(Calendar.getInstance().getTime().getYear(),Calendar.getInstance().getTime().getMonth(),Calendar.getInstance().getTime().getDay()));
                CalendarAdapter adapter = new CalendarAdapter(getApplicationContext(), cal);
                viewCalendar.setAdapter(adapter);
                //sets the week back to original and date to today - end

                specificDateFlag=false;
                getEventsAndUsers();
            }
        });
        abortCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPickDate.setVisibility(View.VISIBLE);
                expandCollpaseTextViewDate.setVisibility(View.VISIBLE);

                //animate calendar layout - start
                calendarLinearWrapper.animate()
                        .translationY(-(calendarLinearWrapper.getHeight()))
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                calendarLinearWrapper.setVisibility(View.INVISIBLE);
                            }
                        });
                //animate calendar layout - end

                //Collapse - start
                viewCalendar.collapse(0);
                expandCollpaseFlag=true;
                expandCollpaseImage.setImageBitmap(expandCollpaseFlagExpand);
                //Collapse - end
            }
        });
        expandCollpase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expandCollpaseFlag) {
                    //Expandd - start
                    viewCalendar.expand(500);
                    expandCollpaseFlag=false;
                    expandCollpaseImage.setImageBitmap(expandCollpaseFlagCollapsed);
                    //Collapse - end
                }else {
                    //Collapse - start
                    viewCalendar.collapse(500);
                    expandCollpaseFlag=true;
                    expandCollpaseImage.setImageBitmap(expandCollpaseFlagExpand);
                    //Collapse - end
                }
            }
        });

        // bind events of calendar
        viewCalendar.setCalendarListener(new FlexibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = viewCalendar.getSelectedDay();
                Log.i(getClass().getName(), "Selected Day: " + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());
                buttonPickDate.setVisibility(View.VISIBLE);
                expandCollpaseTextViewDate.setVisibility(View.VISIBLE);

                //animate calendar layout - start
                calendarLinearWrapper.animate()
                        .translationY(-(calendarLinearWrapper.getHeight()))
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                calendarLinearWrapper.setVisibility(View.INVISIBLE);
                            }
                        });
                //animate calendar layout - end


                //Collapse - start
                viewCalendar.collapse(0);
                expandCollpaseFlag=true;
                expandCollpaseImage.setImageBitmap(expandCollpaseFlagExpand);
                //Collapse - end

                String startDate=String.valueOf(day.getDay())+"/"+String.valueOf((day.getMonth() + 1))+"/"+String.valueOf(day.getYear());
                String startHour="00:00";
                String endDate=String.valueOf(day.getDay())+"/"+String.valueOf((day.getMonth() + 1))+"/"+String.valueOf(day.getYear());
                String endHour="23:59";
                startTimeSpecificDate = TimeUtils.convertStringToTimeDate(startDate,startHour).getTime();
                endTimeSpecificDate = TimeUtils.convertStringToTimeDate(endDate,endHour).getTime();

                todayDay=new Day(Calendar.getInstance().get(Calendar.YEAR),(Calendar.getInstance().get(Calendar.MONTH)+1),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                selectedDay=new Day(day.getYear(),(day.getMonth() + 1),day.getDay());

                if((todayDay.getDay()==selectedDay.getDay())&&(todayDay.getMonth()==selectedDay.getMonth())&&(todayDay.getYear()==selectedDay.getYear())){//needed here to change flag otherwise inconsistency when selecting current date
                    specificDateFlag = false;
                    getEventsAndUsers();
                }else {
                    specificDateFlag = true;
                    getEventsAndUsers();
                }
                //getEventsAndUsersForDateFromCollapseCalendar(startTimeSpecificDate,endTimeSpecificDate);
            }
            @Override
            public void onItemClick(View v) {
                Day day = viewCalendar.getSelectedDay();
                Log.i(getClass().getName(), "The Day of Clicked View: " + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());
            }
            @Override
            public void onDataUpdate() {Log.i(getClass().getName(), "Data Updated");}
            @Override
            public void onMonthChange() {Log.i(getClass().getName(), "Month Changed" + ". Current Year: " + viewCalendar.getYear() + ", Current Month: " + (viewCalendar.getMonth() + 1));}
            @Override
            public void onWeekChange(int position) {Log.i(getClass().getName(), "Week Changed" + ". Current Year: " + viewCalendar.getYear() + ", Current Month: " + (viewCalendar.getMonth() + 1) + ", Current Week position of Month: " + position);}
        });
    }*/

    private void setAlarmManager() {
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                INTERVAL_5_MINUTES, alarmIntent);
    }

    @Override
    protected void onStart() {
        if ((getPermissionsChecker() != null) && getPermissionsChecker().isLocationAvailable()) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEventsAndUsers();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }




    //@Override
    protected void initializeViews() {
        ButterKnife.bind(MapsActivity.this);

        //initUserImage();
        //initSeekBar();
        initEventsRecyclerView();
        initBottomSheet();
        initBottomSheetWithFAB();
    }

//    private void initUserImage() {
//        if (!TextUtils.isEmpty(user.getImagePath())) {
//            Target target = ImageUtils.displayRoundedPicture(MapsActivity.this, user.getImagePath(), userImg);
//            userImg.setTag(target);
//            userImg.setOnClickListener(null);
//        } else {
//            userImg.setImageResource(R.drawable.picture_placeholder);
//            userImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showSignupWithFBDialog("Would you like to sign up with Facebook?");
//                }
//            });
//        }
//    }

//    private void initSeekBar() {
//        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
//            @Override
//            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
//                seekBarPreviousValue = seekBar.getProgress();
//            }
//
//            @Override
//            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
//                fromSeekBar = true;
//                getEventsAndUsers();
//            }
//        });
//        seekBar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
//            @Override
//            public int transform(int value) {
//                return setNewNumericValues(value);
//            }
//        });
//    }

    private void initEventsRecyclerView() {
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mEventsRecyclerView.setLayoutManager(mLayoutManager);

        mEventsList = new ArrayList<>();
        mEventsBottomSheetAdapter = new EventsBottomSheetAdapter(this, mEventsList, user);
        mEventsBottomSheetAdapter.registerCallback(this);
        mEventsRecyclerView.setAdapter(mEventsBottomSheetAdapter);

        mMapObjectsList = new ArrayList<>();
        mMarkersList = new ArrayList<>();
    }

    @Override
    public void onEventClick(int position) {
        EventInfo clickedEvent = mEventsList.get(position);

        for (EventAndPeopleObject eventAndPeopleObject : mMapObjectsList) {
            if (eventAndPeopleObject.getObject().equals(clickedEvent)) {
                for (Marker marker : mMarkersList) {
                    if (eventAndPeopleObject == marker.getTag()) {
                        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
                        updateMarkersIcons(marker);
                        mapCenter = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                        reCenterMap(true);
                        break;
                    }
                }
                break;
            }
        }

//        getEventDetails(clickedEvent.getId());
    }

    private void initBottomSheet() {
        Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.grow);
        Animation shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.shrink);

//        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        View bottomSheet = findViewById(R.id.bottom_sheet);

        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setHideable(true);
        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    mButton2.setText(R.string.button2_peek);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    mButton2.setText(R.string.button2_hide);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    mButton2.setText(R.string.button2);
                    displayListBtn.setChecked(false);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    private void initBottomSheetWithFAB() {
        View bottomSheetWithFAB = findViewById(R.id.bottom_sheet_with_fab);
        eventDetailsFAB = (FloatingActionButton) findViewById(R.id.eventDetailsFAB);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetWithFAB);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    eventDetailsFAB.show();
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    eventDetailsFAB.show();
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    eventDetailsFAB.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                eventDetailsFAB.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }




    private void getEventsAndUsers() {
        if (mapCenter != null) {

            calculateMapRadius();
            //count = 1; //setNewNumericValues(seekBar.getProgress());

            //if(!specificDateFlag) {//Flag to decide if we use specifric date API call
            setExpandCollapseHeader();

            //API call for events - start
            JSONObject locationsJsonObject = APIManager.getInstance().locationsJson(user, mapCenter, radius);
            APIManager.getInstance().postRequestNoProgress(this, URLs.LOCATIONS, locationsJsonObject.toString(), new LocationsRequestListener());
            //API call for events - end
//            }
//            else{
//                //Date in thextview for the user - start
//                expandCollpaseTextViewDate.setText(String.valueOf(selectedDay.getDay()) + "/" + String.valueOf(selectedDay.getMonth()) + "/" + String.valueOf(selectedDay.getYear()));
//                expandCollpaseTextViewDate.setVisibility(View.VISIBLE);
//                //Date in thextview for the user - end
//
//                //API call for events specific date - start
//                JSONObject locationsJsonObject = APIManager.getInstance().locationsJsonForCollapseCalendar(user, mapCenter, radius, startTimeSpecificDate,endTimeSpecificDate); //Rami - check here if I need to add date
//                APIManager.getInstance().postRequestNoProgress(this, URLs.LOCATIONS, locationsJsonObject.toString(), new LocationsRequestListener());
//                //API call for events specific date - end
//            }
        }
    }

    private void calculateMapRadius() {
        if (googleMap != null) {

            Location centerLocation = new Location("");
            centerLocation.setLatitude(mapCenter.latitude);
            centerLocation.setLongitude(mapCenter.longitude);

            Location edgeLocation = new Location("");
//            edgeLocation.setLatitude(mapCenter.latitude);
//            edgeLocation.setLongitude(googleMap.getProjection().getVisibleRegion().latLngBounds.northeast.longitude); // southwest.longitude
            LatLng nCorner = googleMap.getProjection().getVisibleRegion().latLngBounds.northeast;
            edgeLocation.setLatitude(nCorner.latitude);
            edgeLocation.setLongitude(mapCenter.longitude);

            radius = (int) Math.round(centerLocation.distanceTo(edgeLocation) + 0.5);
        } else {
            radius = Const.RADIUS_IN_METERS;
        }
    }

//    private int setNewNumericValues(int value){
//        if (value < 11) //10
//            return value;
//        else if (value < 29) { //18
//            value -= 10;
//            return (value * 5) + 10;
//        } else /*if (value < 47)*/ { //18
//            value -= 28;
//            return (value * 50) + 100;
//        }
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toaster.longToast(this, "Connecting to google maps failed!");
    }

    public class LocationsRequestListener extends BaseRequestListener {//Rami changed from private to public to use in EventDetailsActivity after Like/Dislike
        @Override
        public void onRequestFailure(String err) {
            super.onRequestFailure(err);
            stopProgress();
            presentError();
        }

        @Override
        public void onRequestSuccess(String result) {
            try {
                JSONObject json = new JSONObject(result);
                if (json.optBoolean(Const.KEY_SUCCESS, false)) {
                    int protocolVersion = json.getInt(Const.KEY_VERSION);
                    final JSONObject data = json.getJSONObject(Const.KEY_DATA);
                    if (data != null) {
                        mEventsList.clear();
                        mMapObjectsList.clear();
                        mMarkersList.clear();
                        googleMap.clear();

                        if (ArrayDeserializer.fromJson(protocolVersion, data, Const.KEY_EVENTS, mEventsList, new ArrayDeserializer.ItemDeserializer<EventInfo>() {
                                @Override
                                public EventInfo fromJson(int protocolVersion, JSONObject json) throws JSONException {
                                    return EventInfoDeserializer.fromJson(protocolVersion, json);
                                }
                            })) {

                            //Type listType = new TypeToken<List<EventDetails>>() {}.getType();
                            //mEventsList = new Gson().fromJson(data.get(Const.KEY_EVENTS).toString(), listType);
                            for (EventInfo e : mEventsList) {
                                EventAndPeopleObject obj = new EventAndPeopleObject(e);
                                mMapObjectsList.add(obj);
                                addMarkerToMap(obj);
                            }
                        }

//                        if (!data.isNull(Const.KEY_PEOPLE)) {
//                            Type listType = new TypeToken<List<People>>() {}.getType();
//                            List<People> mPeoplesList = new Gson().fromJson(data.get(Const.KEY_PEOPLE).toString(), listType);
//                            for (People p : mPeoplesList) {
//                                EventAndPeopleObject obj = new EventAndPeopleObject(EntityType.PEOPLE, p, index);
//                                mMapObjectsList.add(obj);
//                                addMarkerToMap(index++, obj);
//                            }
//                        }
                    }
                    mEventsBottomSheetAdapter.setEvents(mEventsList);
                    mEventsBottomSheetAdapter.notifyDataSetChanged();
                } else {
                    presentError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
//                Logger.e(getClass().getSimpleName(), e.toString());
            }

            if (mEventsList.isEmpty()) {
                displayListBtn.setVisibility(View.GONE);
                if (mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_HIDDEN)
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
            } else {
                displayListBtn.setVisibility(View.VISIBLE);
            }

            stopProgress();
        }
    }

    private void presentError() {
        Toast.makeText(MapsActivity.this, "Server ERROR", Toast.LENGTH_SHORT).show();

        //if (fromSeekBar) {
        //    seekBar.setProgress(seekBarPreviousValue);
        //}
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_PICK_IMAGE) {
                mSelectedImagePath = ImageUtils.getPath(this, data.getData());
            }

            Bitmap bmp = null;
            if (mSelectedImagePath == null) {
//                    bmp = BitmapFactory.decodeFile(data.getData().getPath());
                try {
                    bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                    mSelectedImageByte  = IOUtils.toByteArray(getContentResolver().openInputStream(data.getData()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else {
                bmp = ImageUtils.getBitmapFromPath(mSelectedImagePath);
                //eventImage.setImageDrawable(Drawable.createFromPath(this.mSelectedImagePath));
            }
            /*if (eventImage != null && bmp != null) {
                eventImage.setImageBitmap(bmp);
                eventImage.setVisibility(View.VISIBLE);
            }*/
            String eventTitle = data.getStringExtra(ImagePreviewActivity.RESULT_KEY_EVENT_NAME);
            String eventDesc = data.getStringExtra(ImagePreviewActivity.RESULT_KEY_COMMENT);
            long eventPlaceIdx = data.getLongExtra(ImagePreviewActivity.RESULT_KEY_PLACE_IDX, -1);
            String eventPlace = m_placesFetcher.isValidPlace(eventPlaceIdx)
                    ? data.getStringExtra(ImagePreviewActivity.RESULT_KEY_PLACE_DESC)
                    : null;
            //showCreateEventDialog(bmp, eventTitle, eventDesc);
            createEvent(mNewEventLocation, bmp, eventTitle, eventDesc, eventPlace);
        }
        else {
            if (m_placesFetcher != null) {
                m_placesFetcher.cancel();
                m_placesFetcher = null;
            }

            if (resultCode == ImagePreviewActivity.RESULT_IMAGE_SAVE_ERROR) {
                String errMsg = data.getStringExtra(ImagePreviewActivity.RESULT_KEY_ERROR);
                if ((errMsg != null) && !errMsg.isEmpty()) {
                    Toaster.longToast(this, errMsg);
                }
            }
        }
    }

    @Override
    public void getTakenImagePath(String mSelectedImagePath) {
        this.mSelectedImagePath = mSelectedImagePath;
    }








    @BindView(R.id.list_btn)
    SwitchCompat displayListBtn;

    @OnClick(R.id.list_btn)
    void bottomSheetEventsList() {
        if (displayListBtn.isChecked()) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    //@BindView(R.id.new_event_btn)
    //SwitchCompat m_btnNewEvent;

    @OnClick(R.id.new_event_floating_btn)
    void onBtnNewEvent() {
        initiateNewEventCreation(mapCenter);
    }

    void initiateNewEventCreation(LatLng latLng) {
        Bundle extras = new Bundle();
        mNewEventLocation = latLng;
        if (mNewEventLocation != null) {
            extras.putBoolean(ImagePreviewActivity.PARAM_KEY_IS_EVENT_CREATION, true);
            m_placesFetcher = new GooglePlacesAsyncFetcher(this, mGoogleApiClient);
            showPhotoDialog(REQUEST_EVENT_PHOTO, "Create New Event", new ImagePreviewActivity.OnImagePreviewReady() {
                public void onImagePreviewReady(ImagePreviewActivity previewActivity) {
                    Spinner placesSpinner = previewActivity.getPlacesSpinner();
                    if (placesSpinner != null) {
                        m_placesFetcher.setSpinner(placesSpinner);
                    }
                }
            }, extras);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        stopProgress();

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);
//        uiSettings.setMyLocationButtonEnabled(false);

        this.googleMap = googleMap;
        googleMap.setOnCameraChangeListener(MapsActivity.this);
        googleMap.setOnCameraIdleListener(MapsActivity.this);

        if (getPermissionsChecker().isLocationAvailable()) {
            subscribeForMyLocation();
        }

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //if (TextUtils.isEmpty(CredentialsManager.getInstance(MapsActivity.this).getToken())) {
                //    showSignupWithFBDialog("Please, sign up with Facebook");
                //}
                mNewEventLocation = latLng;
                if (mNewEventLocation != null) {
                    //openContextMenu(mapView);
                    //showCreateEventDialog();
                    initiateNewEventCreation(latLng);
                }
            }
        });
    }

    /*private void showCreateEventDialog(Bitmap bmp, String eventTitle, String eventDesc) {
        //registerForContextMenu(mapView);
        //addPhoto(mapView);
        createEventDialog = new Dialog(this);
        createEventDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        createEventDialog.setContentView(R.layout.dialog_create_new_event);

        final Calendar cal = Calendar.getInstance();
        int dYear = cal.get(Calendar.YEAR);
        int dMonth = cal.get(Calendar.MONTH);
        int dDay = cal.get(Calendar.DAY_OF_MONTH);
        int dHour = cal.get(Calendar.HOUR_OF_DAY);
        int dMinute = cal.get(Calendar.MINUTE) > 30 ? 30 : 0;

        etEventName = (EditText) createEventDialog.findViewById(R.id.tv_event_name);
        if (eventTitle != null) {
            etEventName.setText(eventTitle);
        }
        etEventDesc = (EditText) createEventDialog.findViewById(R.id.tv_event_desc);
        if (eventDesc != null) {
            etEventDesc.setText(eventDesc);
        }
        eventImage = (ImageView) createEventDialog.findViewById(R.id.event_img);
        if (eventImage != null && bmp != null) {
            eventImage.setImageBitmap(bmp);
            eventImage.setVisibility(View.VISIBLE);
        }

        //tvDate = (TextView) createEventDialog.findViewById(R.id.tv_date);
        //tvDate.setOnClickListener(this);
        //tvDate.setText(TimeUtils.getFixDatedStr(dDay, dMonth, dYear));

        tvStartTime = (TextView) createEventDialog.findViewById(R.id.tv_start_time);
        tvStartTime.setOnClickListener(this);
        tvStartTime.setText(TimeUtils.getFixTimeStr(dHour, dMinute));

        tvEndTime = (TextView) createEventDialog.findViewById(R.id.tv_end_time);
        tvEndTime.setOnClickListener(this);
        tvEndTime.setText(TimeUtils.getFixTimeStr(dHour + 4, dMinute));

        (createEventDialog.findViewById(R.id.create_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etEventName.getText().toString()) ||
                        TextUtils.isEmpty(etEventDesc.getText().toString())) {
                    Toaster.longToast(MapsActivity.this, "Please, fill all fields");
                } else {
                    createEventDialog.dismiss();
                    createEvent();
                }
            }
        });

        (createEventDialog.findViewById(R.id.cancel_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventDialog.dismiss();
            }
        });

        createEventDialog.show();
    }*/

    private void createEvent(LatLng location, Bitmap eventImg, String name, String desc, String eventPlace) {
        String userId = user.getId();
        String accessToken = CredentialsManager.getInstance(MapsActivity.this).getToken();

        EventDetails eventDetails = new EventDetails();
        eventDetails.setEventLocation(new LatLng(location.latitude, location.longitude));
        if (!TextUtils.isEmpty(name))
            eventDetails.setName(name);
        if (!TextUtils.isEmpty(desc))
            eventDetails.setDescription(desc);
        if (!TextUtils.isEmpty(eventPlace))
            eventDetails.setPlace(eventPlace);

        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) > 30 ? 30 : 0);

        int dYear = cal.get(Calendar.YEAR);
        int dMonth = cal.get(Calendar.MONTH);
        int dDay = cal.get(Calendar.DAY_OF_MONTH);
        int dHour = cal.get(Calendar.HOUR_OF_DAY);
        int dMinute = cal.get(Calendar.MINUTE) > 30 ? 30 : 0;

        Date startTime = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 4);
        Date endTime = cal.getTime();
        eventDetails.setEventTime(new EventTime(startTime, endTime));

        JSONObject jsonObject = APIManager.getInstance().createEventJson(userId, eventDetails, accessToken);
        if (jsonObject != null) {
            APIManager.getInstance().postJsonWithImageRequest(
                    this, URLs.CREATE_EVENT,
                    jsonObject.toString(), mSelectedImagePath, mSelectedImageByte,
                    new CreateEventRequestListener());
        }
    }

    // the server is supposed to return only the relevant events...
/*
    private void updateMarkersVisibility(Date currentDate) {
        for (int i=0; i<mMarkersList.size(); i++) {
            EventAndPeopleObject eapObject = mMapObjectsList.get(i); // i = int position = (int) mMarkersList.get(i).getTag();
            if (eapObject.getType() == EntityType.EVENT) {
                if (currentDate.after(eapObject.getEventStartTime()) &&
                        currentDate.before(eapObject.getEventEndTime())) {
                    mMarkersList.get(i).setVisible(true);
                } else {
                    mMarkersList.get(i).setVisible(false);
                }
            }
        }
    }
*/

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraIdle() {
        mapCenter = googleMap.getCameraPosition().target;

        if (!fromMarkerOrEventItemClick) {
            getEventsAndUsersHandler.removeCallbacks(getEventsAndUsersRunnable);
            getEventsAndUsersHandler.postDelayed(getEventsAndUsersRunnable, 500);
        }

        fromMarkerOrEventItemClick = false;
    }

    private void showSignupWithFBDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(message);
        alertDialogBuilder
//                .setMessage("Please, sign up with Facebook")
                .setCancelable(false)
                .setPositiveButton("GO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (PreferenceCache.clear(MapsActivity.this)) {
                            Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
                            intent.putExtra(Const.KEY_SIGN_FB, true);
                            startActivityFinish(intent);
                        } else {
//                            Toaster.longToast(MapsActivity.this, "Some error with the data");
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void reCenterMap(boolean fromEventItemClick) {
        if (mapCenter == null || googleMap == null) return;
        CameraPosition newCameraPosition = CameraPosition.fromLatLngZoom(mapCenter, 16);
        if (fromEventItemClick) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition), 500, null);
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsChecker.REQUEST_PERMISSION_STORAGE:
                openContextMenu(etEventName);
                break;
            case PermissionsChecker.REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationPermissionGranted();
                break;
        }
    }

    //    @Override
    protected void locationPermissionGranted() {
        subscribeForMyLocation();
    }


    private void subscribeForMyLocation() {
        if (googleMap == null) {
            return;
        }
        googleMap.setOnMyLocationChangeListener(new LocationChangeListener());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 0, 300);
        }
        reCenterMap(false);

        if ((getPermissionsChecker() != null) && getPermissionsChecker().isLocationAvailable()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationRequest reqParams = new LocationRequest();
        reqParams.setFastestInterval(10 * 1000);
        reqParams.setInterval(60 * 1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, reqParams, new LocationListener() {
            //@override
            public void onLocationChanged(Location location) {
                locationUpdateReceived(location);
            }
        });
        if (lastLocation != null) { // maybe does not have a last location, yet
            locationUpdateReceived(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.tv_date:
            //    datePicker();
            //    break;
            case R.id.tv_start_time:
                timePicker(R.id.tv_start_time);
                break;
            case R.id.tv_end_time:
                timePicker(R.id.tv_end_time);
                break;
        }
    }

    /*void datePicker() {
//        Toast.makeText(this, "RL DATE", Toast.LENGTH_SHORT).show();
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);
                        tvDate.setText(TimeUtils.getFixDatedStr(dayOfMonth, monthOfYear, year));
                    }
                })
                .setFirstDayOfWeek(Calendar.SUNDAY)
                //.setPreselectedDate(year, month, day)
                .setDoneText(getResources().getString(R.string.ok))
                .setCancelText(getResources().getString(R.string.cancel))
                .setThemeLight();
        cdp.show(getSupportFragmentManager(), "FRAG_TAG_DATE_PICKER");
    }*/

    //    @OnClick(R.id.tv_start_time)
    void timePicker(final int id) {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setForced24hFormat()
                .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
                        //final Calendar cal = Calendar.getInstance();
                        //cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        //cal.set(Calendar.MINUTE, minute);
                        //cal.set(Calendar.SECOND, 0);
                        if (id == tvStartTime.getId())
                            tvStartTime.setText(TimeUtils.getFixTimeStr(hourOfDay, minute));
                        else if (id == tvEndTime.getId())
                            tvEndTime.setText(TimeUtils.getFixTimeStr(hourOfDay, minute));
                    }
                })
                .setThemeLight();
        rtpd.show(getSupportFragmentManager(), "FRAG_TAG_TIME_PICKER");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        updateMarkersIcons(marker);
        return false; // return true disables auto center on marker
    }

    private void updateMarkersIcons(Marker newMarker) {
        fromMarkerOrEventItemClick = true;
        if (selectedEventOrPeopleObject != null) { // previous object clicked
            if (selectedEventOrPeopleObject.getType() == EntityType.EVENT) {
                for (Marker prevMarker : mMarkersList) {
                    if (selectedEventOrPeopleObject == prevMarker.getTag()) {
                        prevMarker.setIcon(ImageUtils.getMarkerIcon(R.drawable.icn_flag));
                    }
                }
            }
        }

        selectedEventOrPeopleObject = (EventAndPeopleObject) newMarker.getTag();
        if (selectedEventOrPeopleObject.getType() == EntityType.EVENT) {
            newMarker.setIcon(ImageUtils.getMarkerIcon(R.drawable.icn_flag_selected));
            getEventDetails(selectedEventOrPeopleObject.getId());
        }
    }


    private final class LocationChangeListener implements GoogleMap.OnMyLocationChangeListener {

        @Override
        public void onMyLocationChange(Location location) {
            //m_useCustomLocation = true;
            locationUpdateReceived(location);
        }
    }

    private void locationUpdateReceived(Location location) {
        if (location == null)
            return;

//        if (mapTouched) return;

        mapCenter = new LatLng(location.getLatitude(), location.getLongitude());
        if (isInit) {
            isInit = false;
            //mapCenter = new LatLng(location.getLatitude(), location.getLongitude());
            reCenterMap(false);
            getEventsAndUsers();
        } else {
            return;
        }
    }

    private class CreateEventRequestListener extends BaseRequestListener {
        @Override
        public void onRequestFailure(String err) {
            super.onRequestFailure(err);
            stopProgress();
        }

        @Override
        public void onRequestSuccess(String result) {
            super.onRequestSuccess(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean(Const.KEY_SUCCESS)) {
                    Toaster.longToast(MapsActivity.this, "EventDetails created successfully!");
                } else {
                    Toaster.longToast(MapsActivity.this, "EventDetails creation failed!");

                    // TODO "already exist" popup
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getEventsAndUsers();
        }
    }

    private void addMarkerToMap(final EventAndPeopleObject object) {
        LatLng latLng = object.getLocation();

        BitmapDescriptor icon = ImageUtils.getMarkerIcon(object.getMarkerIcon());

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(icon));
        marker.setTag(object);

        mMarkersList.add(marker);
        googleMap.setOnMarkerClickListener(MapsActivity.this);
    }


    public void getEventDetails(String eventId) {
        JSONObject jsonObject = APIManager.getInstance().eventDetailsJson(user, eventId);
        if (jsonObject != null)
            APIManager.getInstance().postRequest(this, URLs.EVENT_DETAILS, jsonObject.toString(), new EventDetailsRequestListener());
    }


    private class EventDetailsRequestListener extends BaseRequestListener {

        @Override
        public void onRequestFailure(String err) {
            super.onRequestFailure(err);
            stopProgress();
        }

        @Override
        public void onRequestSuccess(String result) {
            super.onRequestSuccess(result);
            stopProgress();
            try {
                JSONObject json = new JSONObject(result);

                if (json.getBoolean(Const.KEY_SUCCESS)) {
                    int protocolVersion = json.getInt(Const.KEY_VERSION);
                    final JSONObject data = json.getJSONObject(Const.KEY_DATA);
                    if (data != null) {
                        EventInfo event = EventInfoDeserializer.fromJson(protocolVersion, data);
                        setEventBottomSheet(event);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setEventBottomSheet(final EventInfo event) {
        TextView tvName = (TextView) findViewById(R.id.tv_event_details_name);
        TextView tvDesc = (TextView) findViewById(R.id.tv_event_details_desc);
        TextView tvTime = (TextView) findViewById(R.id.tv_event_details_time);

        tvName.setText(event.getDetails().getName());
        tvDesc.setText(event.getDetails().getDescription());
        tvTime.setText(event.getDetails().getTimeRange());

        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        View container = findViewById(R.id.bottom_sheet_event_details_container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateMapRadius();
                Intent intent = new Intent(MapsActivity.this, EventDetailsActivity.class);
                intent.putExtra(Const.KEY_PASS_EVENT, event);
                intent.putExtra(Const.KEY_PASS_EVENT+"mapCenter", mapCenter);
                intent.putExtra(Const.KEY_PASS_EVENT+"radius", radius);
                //intent.putExtra(Const.KEY_PASS_EVENT+"count", count);
                //mMapsActivityLocationsRequestListener =new LocationsRequestListener();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED &&
                mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED)
            super.onBackPressed();
        else {
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (mBottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_EXPANDED)
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}