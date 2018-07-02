package com.hippotec.mapsapplication.api.listeners;

import com.hippotec.mapsapplication.utils.Logger;

/**
 * Created by Avishay Peretz on 03/04/2017.
 */

public class BaseRequestListener {

   public void onRequestFailure(String err){
//        stopProgress();
        Logger.e("BaseRequestListener", err);
    }

     public void onRequestSuccess(String result){
         //        stopProgress();
          Logger.i("BaseRequestListener", result.toString());
      }
}