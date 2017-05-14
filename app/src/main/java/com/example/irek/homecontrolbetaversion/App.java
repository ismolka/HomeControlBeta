package com.example.irek.homecontrolbetaversion;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.irek.homecontrolbetaversion.data.model.DaoMaster;
import com.example.irek.homecontrolbetaversion.data.model.DaoMaster.DevOpenHelper;
import com.example.irek.homecontrolbetaversion.data.model.DaoSession;
import com.example.irek.homecontrolbetaversion.data.model.DataToSend;
import com.example.irek.homecontrolbetaversion.service.BTService;
import com.example.irek.homecontrolbetaversion.ui.base.ConstantsUI;
import com.example.irek.homecontrolbetaversion.utils.SharedPrefManager;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Wojtek on 20.04.2017.
 */

public class App extends Application {
    private static final String TAG = "App";
    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;
    private SharedPrefManager sharedPrefManager;

    @Override
    public void onCreate() {
        super.onCreate();

        DevOpenHelper helper = new DevOpenHelper(this, ENCRYPTED ? "aa-db-encrypted" : "aa-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        if(!isServiceRunning()) {
            Log.d(TAG, "Sending start service intent");
            Intent serviceIntent = new Intent(this, BTService.class);
            startService(serviceIntent);
        }

        sharedPrefManager = new SharedPrefManager(getApplicationContext(), "com.example.irek.shared_pref_file", MODE_PRIVATE);
        sharedPrefManager.initDefSharedPref();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public boolean isServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        // Loop through the running services
        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (BTService.class.getName().equals(service.service.getClassName())) {
                Log.d(TAG, "service check: running");
                // If the service is running then return true
                return true;
            }
        }
        Log.d(TAG, "service check: NOT running");
        return false;
    }

    public SharedPrefManager getSharedPrefManager() {
        return sharedPrefManager;
    }
}
