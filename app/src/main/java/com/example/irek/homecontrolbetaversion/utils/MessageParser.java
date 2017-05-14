package com.example.irek.homecontrolbetaversion.utils;

import android.util.Log;

import com.example.irek.homecontrolbetaversion.data.model.DataToSend;
import com.example.irek.homecontrolbetaversion.data.model.DeviceData;
import com.example.irek.homecontrolbetaversion.data.model.Request;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonEncodingException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Wojtek on 10.05.2017.
 */

public class MessageParser {
    private static final String TAG = "MessageParser";
    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
    public static DeviceData parseMessageToModelObject(String jsonString) {
        DeviceData dd = null;

        try {
            JSONObject jo = new JSONObject(jsonString);
            dd = new DeviceData();
            dd.setTempDom(jo.getLong("tempDom"));
            dd.setWilgotnosc(jo.getLong("wilgotnosc"));
            dd.setTempPokojDzieci(jo.getLong("tempPokojDzieci"));
        } catch (JSONException e) {
            e.printStackTrace();
            dd = null;
        }

        return dd;
    }

    public static String parseToJson(Object obj) {
        Moshi moshi = new Moshi.Builder().build();
        if(obj instanceof DataToSend) {
            JsonAdapter<DataToSend> jsonAdapter = moshi.adapter(DataToSend.class);
            Log.d(TAG, jsonAdapter.toJson((DataToSend) obj));
            return jsonAdapter.toJson((DataToSend) obj);
        } else if ( obj instanceof Request ) {
            JsonAdapter<Request> jsonAdapter = moshi.adapter(Request.class);
            return jsonAdapter.toJson((Request) obj);
        }
        return "";
    }
}
