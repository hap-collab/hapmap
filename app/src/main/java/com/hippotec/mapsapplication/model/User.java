package com.hippotec.mapsapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hippotec.mapsapplication.utils.Logger;
import com.hippotec.mapsapplication.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Avishay Peretz on 02/04/2017.
 */
public class User /*implements Parcelable*/ {

    private static final String TAG = "User";

    public static final String EXTERNAL_METHOD_FACEBOOK = "fb";
//    public static final String KEY_ID = "id";
//    public static final String KEY_NAME = "name";
//    public static final String KEY_GENDER = "gender";
//    public static final String KEY_BIRTHDAY = "birthday";
//    public static final String KEY_PICTURE = "picture";

    private enum BirthdayType { UNKNOWN, EXACT, INFERRED };

    private String uid;
    private String externalLoginMethod;
    private String externalId;
    private String name;
    private String gender;
    private BirthdayType birthdayType = BirthdayType.UNKNOWN;
    private Date birthday;
    private String imagePath;

    public String getId() {
        return uid;
    }
    public String getExternalId() {
        return this.externalId;
    }
    public String getExternalLoginMethod() {
        return this.externalLoginMethod;
    }

    public void setId(String id) {
        this.uid = id; // ((id == "") || id.equals("000000000000000")) ? "4785074604081152" : id;
    }

    public void setExternalId(String method, String id) {
        this.externalLoginMethod = method;
        this.externalId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getAge() {
        Calendar dob = Calendar.getInstance();
        dob.setTime(birthday);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            --age;
        }

        return age;
    }

    public void setBirthday(String birthday) {
        Date bd = TimeUtils.convertStringToDate(birthday);
        this.birthday = bd;
        this.birthdayType = BirthdayType.EXACT;
    }

    public void setBirthYear(int year) {
        Calendar today = Calendar.getInstance();
        birthday = today.getTime();
        birthday.setYear(year);
        birthdayType = BirthdayType.INFERRED;

    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.id);
//        dest.writeString(this.name);
//        dest.writeString(this.gender);
//        dest.writeString(this.birthday);
//        dest.writeString(this.imagePath);
//
//    }

    public User() {
    }

//    protected User(Parcel in) {
//        this.id = in.readString();
//        this.name = in.readString();
//        this.gender = in.readString();
//        this.birthday = in.readString();
//        this.imagePath = in.readString();
//    }
//
//    public static final Creator<User> CREATOR = new Creator<User>() {
//        public User createFromParcel(Parcel source) {
//            return new User(source);
//        }
//
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };

/*
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("user", (Object)this);
            */
/*json.put(KEY_ID, id);
            json.put(KEY_NAME, name);
            json.put(KEY_GENDER, gender);
            json.put(KEY_BIRTHDAY, birthday);
            json.put(KEY_PICTURE, imagePath);*//*

        }
        catch (JSONException e) {
            assert("User.toJson()" == null);
            Logger.e(TAG, "toJson()");
        }
        return json;
    }
*/
}
