package com.hippotec.mapsapplication.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.model.User;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Avishay Peretz on 02/04/2017.
 */
public class CredentialsManager {

    private static final String TAG = "CredentialsManager";

    private static CredentialsManager instance;
    private String token;

    public static CredentialsManager getInstance(Context context) {
        if (instance == null) {
            instance = new CredentialsManager(context);
        }
        return instance;
    }

    private CredentialsManager(Context context) {
        this.context = context;
    }

    private Context context;
    private User user;
    //private String imei;

    public User getUser() {
        if (user == null) {
            user = loadUserData();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        saveUserData(user);
    }

    private void saveUserData(User user) {
        String jsUserData = (user != null)
                ? UserSerializer.toJson(user).toString()
                : null;
        PreferenceCache.putString(context, Const.KEY_USER_DATA, jsUserData);
    }

    private User loadUserData() {
        String storedData = PreferenceCache.getString(context, Const.KEY_USER_DATA);
        if (storedData == null) {
            return null;
        }
        try {
            JSONObject jsUserData = new JSONObject(storedData);
            return jsUserData == null ? null : UserSerializer.fromJson(jsUserData);
        }
        catch (JSONException e) {
            // corrupt data?
            e.printStackTrace();
            return null;
        }
    }

    /*public String getImei() {
        if (TextUtils.isEmpty(imei)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        }
        return imei;
    }*/

    /*public void setImei(String imei) {
        this.imei = imei;
    }*/

    public String getToken() {
        if (TextUtils.isEmpty(token)) {
            token = "";
            if (PreferenceCache.getString(context, Const.KEY_TOKEN) != null) {
                token = PreferenceCache.getString(context, Const.KEY_TOKEN);
            }
        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        PreferenceCache.putString(context, Const.KEY_TOKEN, token);
    }


//    private ObjectMapper mMapper = new ObjectMapper();
//    private LoginData sLoginData;
//    private UserDevice mDeviceData;
//
//    public void setPubnubUDID() {
//        PreferenceCache.putString(context, UDID_KEY, UUID.randomUUID().toString());
//    }
//
//    public String getPubnubUDID() {
//        if (PreferenceCache.getString(context,UDID_KEY)==null)
//            setPubnubUDID();
//        return PreferenceCache.getString(context,UDID_KEY);
//    }
//
//    public LoginData getLoginData() {
//        if (sLoginData == null) {
//            sLoginData = loadLoginData();
//        }
//        return sLoginData;
//    }
//
//    public UserDevice getDevice() {
//        if (mDeviceData == null) {
//            mDeviceData = loadDeviceData();
//        }
//        return mDeviceData;
//    }
//
//
//    public void setLoginData(LoginData loginData) {
//        sLoginData = loginData;
//        saveLoginData(loginData);
//    }
//
//    public void setDeviceData(UserDevice data) {
//        mDeviceData = data;
//        saveDeviceData(data);
//    }
//
//    private LoginData loadLoginData() {
//        LoginData loginData = null;
//        try {
//            String jsLoginData = getString(context, LOGIN_DATA_KEY);
//            loginData = jsLoginData != null
//                    ? mMapper.readValue(jsLoginData, LoginData.class)
//                    : null;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return loginData;
//    }
//
//    private void saveLoginData(LoginData loginData) {
//        try {
//            String jsLoginData = loginData != null
//                    ? mMapper.writeValueAsString(loginData)
//                    : null;
//            PreferenceCache.putString(context, LOGIN_DATA_KEY, jsLoginData);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private UserDevice loadDeviceData() {
//        UserDevice data = null;
//        try {
//            String jsDeviceData = getString(context, DEVICE_DATA_KEY);
//            data = jsDeviceData != null
//                    ? mMapper.readValue(jsDeviceData, UserDevice.class)
//                    : null;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }
//
//    private void saveDeviceData(UserDevice data) {
//        try {
//            String jsDeviceData = data != null
//                    ? mMapper.writeValueAsString(data)
//                    : null;
//            PreferenceCache.putString(context, DEVICE_DATA_KEY, jsDeviceData);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void clearData() {
//        sLoginData = null;
//        mDeviceData = null;
//        PreferenceCache.putString(context, LOGIN_DATA_KEY, null);
//        PreferenceCache.putString(context, DEVICE_DATA_KEY, null);
//        SharedPreferences sharedPreferences =
//                PreferenceManager.getDefaultSharedPreferences(context);
//        sharedPreferences.edit().putBoolean(GCMPreferences.SENT_TOKEN_TO_SERVER, false).apply();
//    }
}