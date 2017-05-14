package com.example.irek.homecontrolbetaversion.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.irek.homecontrolbetaversion.data.model.DataToSend;
import com.example.irek.homecontrolbetaversion.ui.base.ConstantsUI;

import java.util.Map;

/**
 * Created by Wojtek on 13.05.2017.
 */

public class SharedPrefManager {
    private static final String TAG = "SharedPrefManager";

    private SharedPreferences sharedPreferences;
    private DataToSend dts = null;

    public SharedPrefManager(Context context, String filename, int mode) {
        sharedPreferences = context.getSharedPreferences(filename, mode);
    }

    public DataToSend getSharedPrefDataToSend() { return dts; }

    public void initDefSharedPref() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for(String s : ConstantsUI.DataToSendStringArr) {
            if(!sharedPreferences.contains(s)) {
                if(s.equals("prefTempDom") || s.equals("uruchomienieZraszaczy") || s.equals("prefTempDzieci") || s.equals("jasnoscOswietlenia")) {
                    editor.putLong(s, 22);
                } else {
                    editor.putBoolean(s, false);
                }
            }
        }

        editor.commit();

        initDts();
    }

    private void initDts() {
        dts = new DataToSend();
        updateDts(sharedPreferences.getAll());
    }

    public void updateDts(Map<String, ?> map) {
        for (Map.Entry<String,?> entry : map.entrySet()) {
            switch(entry.getKey()) {
                case "prefTempDom":
                    dts.setPrefTempDom((Long) entry.getValue());
                    break;
                case "czujnikRuchu":
                    dts.setCzujnikRuchu((Boolean) entry.getValue());
                    break;
                case "zasilanieAnten":
                    dts.setZasilanieAnten((Boolean) entry.getValue());
                    break;
                case "oswietlenieDom":
                    dts.setOswietlenieDom((Boolean) entry.getValue());
                    break;
                case "uruchomienieZraszaczy":
                    dts.setUruchomienieZraszaczy((Long) entry.getValue());
                    break;
                case "blokadaBramy":
                    dts.setBlokadaBramy((Boolean) entry.getValue());
                    break;
                case "fontanna":
                    dts.setFontanna((Boolean) entry.getValue());
                    break;
                case "oswietlenieOgrodu":
                    dts.setOswietlenieOgrodu((Boolean) entry.getValue());
                    break;
                case "prefTempDzieci":
                    dts.setPrefTempDzieci((Long) entry.getValue());
                    break;
                case "jasnoscOswietlenia":
                    dts.setJasnoscOswietlenia((Long) entry.getValue());
                    break;
                case "internetSwitch":
                    dts.setInternetSwitch((Boolean) entry.getValue());
                    break;
                case "tvsatSwitch":
                    dts.setTvsatSwitch((Boolean) entry.getValue());
                    break;
            }
        }
    }

    public void savePref() {
        Log.d(TAG, "saving pref in android memory");
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong("prefTempDom", dts.getPrefTempDom());
        editor.putBoolean("czujnikRuchu", dts.isCzujnikRuchu());
        editor.putBoolean("zasilanieAnten", dts.isZasilanieAnten());
        editor.putBoolean("oswietlenieDom", dts.isOswietlenieDom());
        editor.putLong("uruchomienieZraszaczy", dts.getUruchomienieZraszaczy());
        editor.putBoolean("blokadaBramy", dts.isBlokadaBramy());
        editor.putBoolean("fontanna", dts.isFontanna());
        editor.putBoolean("oswietlenieOgrodu", dts.isOswietlenieOgrodu());
        editor.putLong("prefTempDzieci", dts.getPrefTempDzieci());
        editor.putLong("jasnoscOswietlenia", dts.getJasnoscOswietlenia());
        editor.putBoolean("internetSwitch", dts.isInternetSwitch());
        editor.putBoolean("tvsatSwitch", dts.isTvsatSwitch());

        editor.commit();
        Log.d(TAG, "saving completed");
    }
}
