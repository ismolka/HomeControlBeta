package com.example.irek.homecontrolbetaversion.ui.children;

import android.util.Log;

import com.example.irek.homecontrolbetaversion.App;
import com.example.irek.homecontrolbetaversion.data.model.DaoSession;
import com.example.irek.homecontrolbetaversion.data.model.DeviceData;
import com.example.irek.homecontrolbetaversion.data.model.DeviceDataDao;
import com.example.irek.homecontrolbetaversion.ui.base.BasePresenter;
import com.example.irek.homecontrolbetaversion.utils.MessageParser;
import com.example.irek.homecontrolbetaversion.utils.SharedPrefManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Wojtek on 24.04.2017.
 */

public class ChildrenPresenter implements BasePresenter<ChildrenView> {
    private static final String TAG = "ChildrenPresenter";

    private ChildrenView view;
    private DeviceDataDao deviceDataDao;
    private SharedPrefManager sharedPrefManager;

    @Override
    public void onAttach(ChildrenView view) {
        this.view = view;

        DaoSession daoSession = ((App)((ChildrenActivity)this.view).getApplication()).getDaoSession();
        deviceDataDao = daoSession.getDeviceDataDao();

        sharedPrefManager = ((App)((ChildrenActivity)this.view).getApplication()).getSharedPrefManager();

        initViewFields();
        updateUI();
    }

    public void updateUI() {
        List<DeviceData> dataList = deviceDataDao.queryBuilder().orderDesc(DeviceDataDao.Properties.Id).limit(1).list();
        if(dataList.size() > 0) {
            Log.d(TAG, "DeviceDataList size: " + dataList.size() + ", last received child temp: " + dataList.get(0).getTempPokojDzieci().toString());
            view.refreshUI(dataList.get(0).getTempPokojDzieci().toString());
        } else {
            Log.d(TAG, "no items in table device data");
        }
    }

    public void updateSharedPref(Map<String, ?> map) {
        sharedPrefManager.updateDts(map);
    }

    public byte[] getDtsJSONBytesArray() {
        return MessageParser.parseToJson(sharedPrefManager.getSharedPrefDataToSend()).getBytes();
    }

    private void initViewFields() {
        view.initViewFields(sharedPrefManager.getSharedPrefDataToSend());
    }

    public void saveSharedPreferencesDTS() {
        sharedPrefManager.savePref();
    }
}
