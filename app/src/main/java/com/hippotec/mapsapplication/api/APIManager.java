package com.hippotec.mapsapplication.api;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.hippotec.mapsapplication.Const;
import com.hippotec.mapsapplication.activities.base.BaseActivity;
import com.hippotec.mapsapplication.api.listeners.BaseRequestListener;
import com.hippotec.mapsapplication.model.EventDetails;
import com.hippotec.mapsapplication.model.User;
import com.hippotec.mapsapplication.utils.EventSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Avishay Peretz on 03/04/2017.
 */

public class APIManager {

    private static APIManager instance;

    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/png");
    private final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


    private APIManager() {
    }

    public static APIManager getInstance() {
        if (instance == null) {
            instance = new APIManager();
        }
        return instance;
    }

    public void postRequest(BaseActivity baseActivity, String url, String json, BaseRequestListener listener) {
        if (baseActivity != null) {
            baseActivity.startProgress();
        }
        PostTask postTask = new PostTask(url, json, listener);
        postTask.execute();
    }

    public void postRequestNoProgress(BaseActivity baseActivity, String url, String json, BaseRequestListener listener) {
        PostTask postTask = new PostTask(url, json, listener);
        postTask.execute();
    }

    public void postJsonWithImageRequest(BaseActivity baseActivity, String url, String json, String imagePath, byte[] imageByte, BaseRequestListener listener) {
        if (baseActivity != null) {
            baseActivity.startProgress();
        }
        PostTask postTask = new PostTask(url, json, imagePath, imageByte, listener);
        postTask.execute();
    }


    public void getRequest(String url, BaseRequestListener listener) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            listener.onRequestSuccess(response.body().string());
        } catch (Exception e) {
            listener.onRequestFailure(e.toString());
        }
    }

    public JSONObject createEventJson(String id, EventDetails eventDetails, String accessToken) {
        JSONObject json;
        try {
            json = new JSONObject();
            json.put(JsonKeys.KEY_USER_ID, id);
            json.put(Const.KEY_ACCESS_TOKEN, accessToken);
            json.put(JsonKeys.KEY_EVENT_DETAILS, EventSerializer.toJson(eventDetails));
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject updateLocationJson(String id, double lat, double lng) {
        JSONObject json;
        try {
            json = new JSONObject();
            json.put(JsonKeys.KEY_USER_ID, id);
            json.put(Const.KEY_LATITUDE, lat);
            json.put(Const.KEY_LONGITUDE, lng);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public JSONObject eventsJson(LatLng lastKnownLocation, int radius) {
        EventLocation eventLocation = new EventLocation(lastKnownLocation.latitude, lastKnownLocation.longitude);
        JSONObject json;
        try {
            json = new JSONObject();
            json.put(Const.KEY_LOCATION, eventLocation.toJson());
            json.put(Const.KEY_RADIUS, radius);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public JSONObject likeEventJson(User user, String eventId, int typeLike) {
        JSONObject json;
        try {
            json = new JSONObject();
            json.put(JsonKeys.KEY_USER_ID, user.getId());
            json.put(Const.KEY_EVENT_ID, eventId);
            json.put(Const.KEY_TYPE, typeLike);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject locationsJson(User user, LatLng lastKnownLocation, int radius) {//Rami - check here if I need to add date
        JSONObject json = new JSONObject();
        try {
            json.put(JsonKeys.KEY_USER_ID, user.getId());
            json.put(Const.KEY_LATITUDE, lastKnownLocation.latitude);
            json.put(Const.KEY_LONGITUDE, lastKnownLocation.longitude);
            json.put(Const.KEY_RADIUS, radius);
            //json.put(Const.KEY_COUNT, count);
            //json.put(Const.KEY_PHONE_ID, phoneId);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private static final String KEY_START_DATE = "start";
    private static final String KEY_END_DATE = "end";
    public JSONObject locationsJsonForCollapseCalendar(User user, LatLng lastKnownLocation, int radius, long startDate, long endDate) {//Rami - check here if I need to add date
        JSONObject json = new JSONObject();
        try {
            json.put(JsonKeys.KEY_USER_ID, user.getId());
            json.put(Const.KEY_LATITUDE, lastKnownLocation.latitude);
            json.put(Const.KEY_LONGITUDE, lastKnownLocation.longitude);
            json.put(Const.KEY_RADIUS, radius);
            //json.put(Const.KEY_COUNT, count);
            json.put(KEY_START_DATE, startDate);
            json.put(KEY_END_DATE, endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject eventDetailsJson(User user, String eventId) {
        JSONObject json = new JSONObject();
        try {
            json.put(JsonKeys.KEY_USER_ID, user.getId());
            json.put(Const.KEY_EVENT_ID, eventId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject addCommentJson(User user, EventDetails event, String text) {
        JSONObject json = new JSONObject();
        try {
            json.put(JsonKeys.KEY_USER_ID, user.getId());
            json.put(Const.KEY_EVENT_ID, event.getId());
            json.put(Const.KEY_COMMENT, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    private class PostTask extends AsyncTask<Void, String, String> {
        private final String url;
        private final String json;
        private final String imagePath;
        private byte[] imageBytes = null;
        private final BaseRequestListener listener;
        private String token = "";

        public PostTask(String url, String json, BaseRequestListener listener) {
            this.url = url;
            this.json = json;
            this.listener = listener;
            imagePath = null;
        }

        public PostTask(String url, String json, String imagePath,byte[] imageByte, BaseRequestListener listener) {
            this.url = url;
            this.json = json;
            this.imagePath = imagePath;
            this.listener = listener;
            this.imageBytes = imageByte;
        }

        @Override
        protected String doInBackground(Void... params) {
            String res = "";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = null;

            if (TextUtils.isEmpty(imagePath) && imageBytes == null) {
                body = RequestBody.create(MEDIA_TYPE_JSON, json);
            }
            else {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    token = jsonObject.optString(Const.KEY_ACCESS_TOKEN, "");
                    RequestBody file = null;
                    String filenameSuggestionOnServer;
                    if (!TextUtils.isEmpty(imagePath) ) {
                        File imagePathParsed = new File(imagePath);
                        filenameSuggestionOnServer = imagePathParsed.getName();
                        MediaType contentType =  (filenameSuggestionOnServer.toLowerCase().endsWith(".jpg")) ? MEDIA_TYPE_JPG : MEDIA_TYPE_PNG;
                        file = RequestBody.create(contentType, imagePathParsed);
                    }
                    else if (imageBytes != null)
                    {
                        JSONObject eventDetailsJson = jsonObject.optJSONObject(JsonKeys.KEY_EVENT_DETAILS);
                        filenameSuggestionOnServer = eventDetailsJson == null ? "image" : eventDetailsJson.optString(Const.KEY_NAME,"image");
                        file = RequestBody.create(MEDIA_TYPE_PNG, imageBytes);
                    }
                    else
                    {
                        filenameSuggestionOnServer = null; // impossible! cause a NullPtrException
                    }
                    body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("photo",
                                    filenameSuggestionOnServer, file)
//                                    RequestBody.create(MEDIA_TYPE_PNG, file/*new File(imagePath)*//*sourceFile*/))
                            .addPart(
                                    Headers.of("Content-Disposition", "form-data; name=\"data\""),
                                    RequestBody.create(MEDIA_TYPE_JSON, json))
//                            .addFormDataPart("data",null, RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))


/*//                            .addPart(    Headers.of("Content-Type", "application/json")  ,RequestBody.create(MEDIA_TYPE_JSON, json))
//                            .addFormDataPart("result", "my_image")
                            .addFormDataPart(Const.KEY_ACCESS_TOKEN, jsonObject.getString(Const.KEY_ACCESS_TOKEN))
                            .addFormDataPart(Const.KEY_ID, jsonObject.getString(Const.KEY_ID))
                            .addFormDataPart(Const.KEY_LOCATION, jsonObject.getJSONObject(Const.KEY_LOCATION).toString())
                            .addFormDataPart(Const.KEY_NAME, jsonObject.getString(Const.KEY_NAME))
                            .addFormDataPart(Const.KEY_DESCRIPTION, jsonObject.getString(Const.KEY_DESCRIPTION))
                            .addFormDataPart(Const.KEY_TIME, jsonObject.getJSONObject(Const.KEY_TIME).toString())
*/

                            .build();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                body = RequestBody.create(MEDIA_TYPE_PNG, "------WebKitFormBoundary7MA4YWxkTrZu0gW" +
//                        "\r\nContent-Disposition: form-data; name=\"photo\"; " +
//                        "filename=\"" + imagePath +
//                        "\"\r\nContent-Type: application/pdf\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
            }
            if (body != null) {
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .header("x-access-token", token)
                        .header("x-key", token)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        res = response.body().string();
                    }
                    else {
                        res = Const.KEY_EXCEPTION + response.message();
                    }
                    return res;
                } catch (Exception e) {
                    res = Const.KEY_EXCEPTION + e.toString();
                    return res;
                }
            }
            return Const.KEY_EXCEPTION + ": body = null";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.startsWith(Const.KEY_EXCEPTION)) {
                listener.onRequestFailure(s);
            } else {
                listener.onRequestSuccess(s);
            }
        }
    }
}



