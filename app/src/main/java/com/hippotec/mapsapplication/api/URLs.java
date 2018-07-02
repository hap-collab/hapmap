package com.hippotec.mapsapplication.api;

/**
 * Created by Avishay Peretz on 03/04/2017.
 */

public interface URLs {
    String BASE_URL = /*"http://10.0.2.2:8080";*/ "https://hap-poc-197809.appspot.com"; //"http://10.0.2.2:8080"; //http://52.51.81.191:8080";
//    String BASE_URL = "http://192.168.10.77:3007";
    String API = BASE_URL + "/api";
    String IMAGE = BASE_URL + "/uploads";

    String FB_SIGN_UP = API + "/extsignup";

    String CREATE_EVENT = API + "/v1/event";

    String MAPS = API + "/maps";
    String SIGN_UP = MAPS + "/signup";
    String UPDATE_LOCATION = MAPS + "/updatelocation";

    String EVENTS = API + "/events";
    String LIKE_EVENT = EVENTS + "/like";
    String LOCATIONS = EVENTS + "/locations";
    String EVENT_DETAILS = EVENTS + "/details";
    String ADD_COMMENT = EVENTS + "/addcomment";
    String ALL_COMMENTS = EVENTS +"/allcomments";
}
