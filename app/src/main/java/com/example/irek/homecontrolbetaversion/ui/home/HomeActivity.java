package com.example.irek.homecontrolbetaversion.ui.home;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irek.homecontrolbetaversion.App;
import com.example.irek.homecontrolbetaversion.R;
import com.example.irek.homecontrolbetaversion.data.model.DataToSend;
import com.example.irek.homecontrolbetaversion.service.BTService;
import com.example.irek.homecontrolbetaversion.service.BluetoothChatService;
import com.example.irek.homecontrolbetaversion.service.ServiceBinder;
import com.example.irek.homecontrolbetaversion.ui.base.IListenerFunctions;
import com.example.irek.homecontrolbetaversion.ui.children.ChildrenActivity;
import com.example.irek.homecontrolbetaversion.ui.connect.ConnectActivity;
import com.example.irek.homecontrolbetaversion.ui.garden.GardenActivity;
import com.example.irek.homecontrolbetaversion.ui.login.LogonActivity;
import com.example.irek.homecontrolbetaversion.ui.settings.SettingsActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeView {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.seekBarTemperature) SeekBar seekTemp;
    @BindView(R.id.textViewCurrentTemp) TextView currentTemp;
    @BindView(R.id.textViewControlTemp) TextView controlTemp;
    @BindView(R.id.switchMEDIA) Switch switchAntena;
    @BindView(R.id.switchMOTION) Switch switchRuch;
    @BindView(R.id.switchLIGHT) Switch switchLampa;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;

    private HomePresenter presenter;

    boolean mBound = false;
    private BTService mService = null;

    private final int minSeek = -20;
    private final int maxSeek = 40;
    private final int stepSeek = 1;

    private UpdateUITask updateUITask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        initView();
        initPresenter();
        //initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");

        //presenter.initViewFields();
        updateUITask = new UpdateUITask();
        updateUITask.execute();

        if(((App)getApplication()).isServiceRunning()) {
            Intent intent = new Intent(this, BTService.class);
            bindService(intent, mConnection, BIND_AUTO_CREATE);
            Log.d(TAG, "service bounded");
        } else {
            Log.d(TAG, "service is not running, cannot bind to it");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");

        updateUITask.cancel(true);

        if(mBound) {
            mService.unregisterCallback(HomeActivity.this);
            unbindService(mConnection);
            mBound = false;
            mService = null;
            Log.d(TAG, "service unbounded");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");

        presenter.saveSharedPreferencesDTS();
    }

    private void initView() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        setStatus("not connected");

        navigationView.setNavigationItemSelectedListener(this);

        seekTemp.setMax((maxSeek - minSeek) / stepSeek);
        seekTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int value = minSeek + (progress * stepSeek);
                controlTemp.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mService == null) {
                    Log.d(TAG, "onSwitch - service is not bounded yet");
                    return;
                }
                Long progress = (long) seekBar.getProgress();
                Long value = minSeek + (progress * stepSeek);
                Log.d(TAG, "prefTempDom: " + value);
                Log.d(TAG, "onStopTrackingTouchControlTemp updating sharedPrfDts");
                Map<String, Long> map = new HashMap<>();
                map.put("prefTempDom", value);
                presenter.updateSharedPref(map);
                Log.d(TAG, "onStopTrackingTouchControlTemp about to write to device");
                mService.writeToDevice(presenter.getDtsJSONBytesArray());
            }
        });
    }

    private void initPresenter() {
        presenter = new HomePresenter();
        presenter.onAttach(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Intent intent = new Intent(this, HomeActivity.class);
            //startActivity(intent);
        } else if (id == R.id.nav_garden) {
            Intent intent = new Intent(this, GardenActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_children) {
            Intent intent = new Intent(this, ChildrenActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }/* else if (id == R.id.nav_connect) {
            Intent intent = new Intent(this, ConnectActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_logon) {
            Intent intent = new Intent(this, LogonActivity.class);
            startActivity(intent);
            finish();
        }*/

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.d(TAG, "onServiceConnected - bound to service");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServiceBinder binder = (ServiceBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.testujemy();
            mService.registerCallback(HomeActivity.this, listener);
            Log.d(TAG, "First write to device after estabilishing connection");
            mService.writeToDevice(presenter.getDtsJSONBytesArray());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mService = null;
        }
    };

    @Override
    public void refreshUI(String tempVal) {
         currentTemp.setText(tempVal);
    }

    private IListenerFunctions listener = new IListenerFunctions() {
        @Override
        public void refreshInterface() {
            Log.d(TAG, "activity's listener's callback was called");
            presenter.updateUI();
        }

        @Override
        public void reefreshActionBarStatus(CharSequence cs) {
            setStatus(cs);
        }
    };

    @OnCheckedChanged(R.id.switchMEDIA)
    public void onSwitchAntenaClick(boolean isChecked) {
        if (mService == null) {
            Log.d(TAG, "onSwitch - service is not bounded yet");
            return;
        }
        Log.d(TAG, "onSwitchAntenaClick updating sharedPrfDts");
        Map<String, Boolean> map = new HashMap<>();
        map.put("zasilanieAnten", isChecked);
        presenter.updateSharedPref(map);
        Log.d(TAG, "onSwitchAntenaClick about to write to device");
        mService.writeToDevice(presenter.getDtsJSONBytesArray());
    }

    @OnCheckedChanged(R.id.switchMOTION)
    public void onSwitchRuchClick(boolean isChecked) {
        if (mService == null) {
            Log.d(TAG, "onSwitch - service is not bounded yet");
            return;
        }
        Log.d(TAG, "onSwitchRuchClick updating sharedPrfDts");
        Map<String, Boolean> map = new HashMap<>();
        map.put("czujnikRuchu", isChecked);
        presenter.updateSharedPref(map);
        Log.d(TAG, "onSwitchRuchClick about to write to device");
        mService.writeToDevice(presenter.getDtsJSONBytesArray());
    }

    @OnCheckedChanged(R.id.switchLIGHT)
    public void onSwitchSwiatloClick(boolean isChecked) {
        if (mService == null) {
            Log.d(TAG, "onSwitch - service is not bounded yet");
            return;
        }
        Log.d(TAG, "onSwitchSwiatloClick updating sharedPrfDts");
        Map<String, Boolean> map = new HashMap<>();
        map.put("oswietlenieDom", isChecked);
        presenter.updateSharedPref(map);
        Log.d(TAG, "onSwitchSwiatloClick about to write to device");
        mService.writeToDevice(presenter.getDtsJSONBytesArray());
    }

    @Override
    public void initViewFields(DataToSend dts){
        Log.d(TAG, "initializing view fileds from shared pref");
        controlTemp.setText(String.valueOf(dts.getPrefTempDom()));
        int progress = (dts.getPrefTempDom().intValue()-minSeek)/stepSeek;
        seekTemp.setProgress(progress);
        switchAntena.setChecked(dts.isZasilanieAnten());
        switchLampa.setChecked(dts.isOswietlenieDom());
        switchRuch.setChecked(dts.isCzujnikRuchu());
    }

    private class UpdateUITask extends AsyncTask<Void, Void, Void> {
        private final static String TAG = "UpdateUITask";
        @Override
        protected Void doInBackground(Void... voids) {
            while(!isCancelled()) {
                try {
                    Log.d(TAG, "running on ui thread - update ui");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            presenter.updateUI();
                        }
                    });
                    Log.d(TAG, "pausing task");
                    Thread.sleep(5000);
                    Log.d(TAG, "waking task up");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        if (null == this) {
            return;
        }
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }
}
