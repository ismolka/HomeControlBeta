package com.example.irek.homecontrolbetaversion.ui.settings;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.irek.homecontrolbetaversion.App;
import com.example.irek.homecontrolbetaversion.R;
import com.example.irek.homecontrolbetaversion.data.model.SettingsPreferences;
import com.example.irek.homecontrolbetaversion.service.BTService;
import com.example.irek.homecontrolbetaversion.service.ServiceBinder;
import com.example.irek.homecontrolbetaversion.ui.children.ChildrenActivity;
import com.example.irek.homecontrolbetaversion.ui.connect.ConnectActivity;
import com.example.irek.homecontrolbetaversion.ui.garden.GardenActivity;
import com.example.irek.homecontrolbetaversion.ui.home.HomeActivity;
import com.example.irek.homecontrolbetaversion.ui.login.LogonActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

import static com.example.irek.homecontrolbetaversion.R.id.checkALERT;
import static com.example.irek.homecontrolbetaversion.R.id.checkMOTION;
import static com.example.irek.homecontrolbetaversion.R.id.checkTEMP;
import static com.example.irek.homecontrolbetaversion.R.id.seekBarRangeTemperature;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsView {
    private static final String TAG = "SettingsActivity";

    @BindView(R.id.checkALERT) CheckBox alertOK;
    @BindView(R.id.checkTEMP) CheckBox alertTemp;
    @BindView(R.id.checkMOTION) CheckBox alertMotion;
    @BindView(R.id.seekBarRangeTemperature) SeekBar seekRange;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.textViewCurrentRange) TextView currentRange;

    private SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        alertOK = (CheckBox)findViewById(checkALERT);
        alertMotion = (CheckBox)findViewById(checkMOTION);
        alertTemp = (CheckBox)findViewById(checkTEMP);
        seekRange = (SeekBar) findViewById(seekBarRangeTemperature);

        initPresenter();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");

        presenter.saveUserSettings();
    }

    private void initView() {
        navigationView.setNavigationItemSelectedListener(this);

        seekRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentRange.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Long value = (long) seekBar.getProgress();
                Log.d(TAG, "onStopTrackingTouchControlTemp updating sharedPrfDts");
                Map<String, Long> map = new HashMap<>();
                map.put("tempPercentageDiff", value);
                presenter.updateSharedPref(map);
                Log.d(TAG, "onStopTrackingTouchControlTemp about to write to device");
            }
        });
    }

    private void initPresenter() {
        presenter = new SettingsPresenter();
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
            Intent intent = new Intent(this, ChildrenActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
            //Intent intent = new Intent(this, SettingsActivity.class);
            //startActivity(intent);
        }/* else if (id == R.id.nav_connect) {
            Intent intent = new Intent(this, ConnectActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logon) {
            Intent intent = new Intent(this, LogonActivity.class);
            startActivity(intent);
        }*/

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnCheckedChanged(R.id.checkALERT)
    public void onAlertOKCheck(boolean isChecked) {
        Log.d(TAG, "IM NOT HERE NOW LOL");
        alertTemp.setEnabled(isChecked);
        seekRange.setEnabled(isChecked);
        alertMotion.setEnabled(isChecked);

        Log.d(TAG, "onAlertCheckedClick updating sharedPrfDts");
        Map<String, Boolean> map = new HashMap<>();
        map.put("showNotifications", isChecked);
        presenter.updateSharedPref(map);
        Log.d(TAG, "onAlertCheckedClick about to write to device");
    }

    @OnCheckedChanged(R.id.checkTEMP)
    public void onAlertTEMPChecked(boolean isChecked) {
        Log.d(TAG, "IM HERE LOL");

        Log.d(TAG, "onTempCheckedClick updating sharedPrfDts");
        Map<String, Boolean> map = new HashMap<>();
        map.put("notifyTemp", isChecked);
        presenter.updateSharedPref(map);
        Log.d(TAG, "onTempCheckedClick about to write to device");
    }

    @OnCheckedChanged(R.id.checkMOTION)
    public void onCheckMOTIONChecked(boolean isChecked){
        Log.d(TAG, "onMotionCheckedClick updating sharedPrfDts");
        Map<String, Boolean> map = new HashMap<>();
        map.put("notifyMotion", isChecked);
        presenter.updateSharedPref(map);
        Log.d(TAG, "onMotionCheckedClick about to write to device");
    }

    @Override
    public void initViewFields(SettingsPreferences sp) {
        Log.d(TAG, "initializing view fileds from shared pref");
        alertOK.setChecked(sp.isShowNotifications());
        alertTemp.setChecked(sp.isNotifyTemp());
        seekRange.setProgress(sp.getTempPercentageDiff().intValue());
        currentRange.setText(String.valueOf(sp.getTempPercentageDiff()));
        alertMotion.setChecked(sp.isNotifyMotion());
    }
}
