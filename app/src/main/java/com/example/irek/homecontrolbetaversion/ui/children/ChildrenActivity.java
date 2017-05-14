package com.example.irek.homecontrolbetaversion.ui.children;

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

import com.example.irek.homecontrolbetaversion.App;
import com.example.irek.homecontrolbetaversion.R;
import com.example.irek.homecontrolbetaversion.data.model.DataToSend;
import com.example.irek.homecontrolbetaversion.service.BTService;
import com.example.irek.homecontrolbetaversion.service.ServiceBinder;
import com.example.irek.homecontrolbetaversion.ui.base.IListenerFunctions;
import com.example.irek.homecontrolbetaversion.ui.connect.ConnectActivity;
import com.example.irek.homecontrolbetaversion.ui.garden.GardenActivity;
import com.example.irek.homecontrolbetaversion.ui.login.LogonActivity;
import com.example.irek.homecontrolbetaversion.ui.settings.SettingsActivity;
import com.example.irek.homecontrolbetaversion.ui.home.HomeActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class ChildrenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChildrenView {
    private static final String TAG = "ChildrenActivity";

    @BindView(R.id.seekBarLightChild) SeekBar controlLight;
    @BindView(R.id.seekBarTemperatureChild) SeekBar controlTemp;
    @BindView(R.id.switchNetworkChild) Switch switchINTERNET;
    @BindView(R.id.switchTVChild) Switch switchTVSAT;
    @BindView(R.id.textViewCurrentTempChild) TextView currentTEMP;
    @BindView(R.id.textViewControlTempChild) TextView controlTempChild;
    @BindView(R.id.textViewControlLightChild) TextView controlLightInfo;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;

    private ChildrenPresenter presenter;

    boolean mBound = false;
    private BTService mService = null;

    private final int minSeek = -20;
    private final int maxSeek = 40;
    private final int stepSeek = 1;

    private UpdateUITask updateUITask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        ButterKnife.bind(this);

        initView();
        initPresenter();
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
            mService.unregisterCallback();
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
        navigationView.setNavigationItemSelectedListener(this);

        controlTemp.setMax((maxSeek -minSeek) / stepSeek);
        controlTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int value = minSeek + (progress * stepSeek);
                controlTempChild.setText(String.valueOf(value));
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
                Long value = (long) seekBar.getProgress();
                Log.d(TAG, "onStopTrackingTouchControlTemp updating sharedPrfDts");
                Map<String, Long> map = new HashMap<>();
                map.put("prefTempDzieci", value);
                presenter.updateSharedPref(map);
                Log.d(TAG, "onStopTrackingTouchControlTemp about to write to device");
                mService.writeToDevice(presenter.getDtsJSONBytesArray());
            }
        });

        controlLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                controlLightInfo.setText(String.valueOf(progress));
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
                Long value = (long) seekBar.getProgress();
                Log.d(TAG, "onStopTrackingTouchControlTemp updating sharedPrfDts");
                Map<String, Long> map = new HashMap<>();
                map.put("jasnoscOswietlenia", value);
                presenter.updateSharedPref(map);
                Log.d(TAG, "onStopTrackingTouchControlTemp about to write to device");
                mService.writeToDevice(presenter.getDtsJSONBytesArray());
            }
        });
    }

    private void initPresenter() {
        presenter = new ChildrenPresenter();
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
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_garden) {
            Intent intent = new Intent(this, GardenActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_children) {
            //Intent intent = new Intent(this, ChildrenActivity.class);
            //startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_connect) {
            Intent intent = new Intent(this, ConnectActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_logon) {
            Intent intent = new Intent(this, LogonActivity.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
            mService.registerCallback(listener);
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
        currentTEMP.setText(tempVal);
    }

    private IListenerFunctions listener = new IListenerFunctions() {
        @Override
        public void refreshInterface() {
            Log.d(TAG, "activity's listener's callback was called");
            presenter.updateUI();
        }
    };

    @OnCheckedChanged(R.id.switchNetworkChild)
    public void onSwitchNetworkClick(boolean isChecked) {
        if (mService == null) {
            Log.d(TAG, "onSwitch - service is not bounded yet");
            return;
        }
        Log.d(TAG, "onSwitchAntenaClick updating sharedPrfDts");
        Map<String, Boolean> map = new HashMap<>();
        map.put("internetSwitch", isChecked);
        presenter.updateSharedPref(map);
        Log.d(TAG, "onSwitchAntenaClick about to write to device");
        mService.writeToDevice(presenter.getDtsJSONBytesArray());
    }

    @OnCheckedChanged(R.id.switchTVChild)
    public void onSwitchTVClick(boolean isChecked) {
        if (mService == null) {
            Log.d(TAG, "onSwitch - service is not bounded yet");
            return;
        }
        Log.d(TAG, "onSwitchAntenaClick updating sharedPrfDts");
        Map<String, Boolean> map = new HashMap<>();
        map.put("tvsatSwitch", isChecked);
        presenter.updateSharedPref(map);
        Log.d(TAG, "onSwitchAntenaClick about to write to device");
        mService.writeToDevice(presenter.getDtsJSONBytesArray());
    }

    @Override
    public void initViewFields(DataToSend dts) {
        Log.d(TAG, "initializing view fileds from shared pref");
        controlLightInfo.setText(String.valueOf(dts.getJasnoscOswietlenia()));
        controlLight.setProgress(dts.getJasnoscOswietlenia().intValue());
        controlTempChild.setText(String.valueOf(dts.getPrefTempDzieci()));
        controlTemp.setProgress(dts.getPrefTempDzieci().intValue());
        switchINTERNET.setChecked(dts.isInternetSwitch());
        switchTVSAT.setChecked(dts.isTvsatSwitch());
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
}
