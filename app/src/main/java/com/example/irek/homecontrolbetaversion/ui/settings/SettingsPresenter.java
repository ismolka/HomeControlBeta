package com.example.irek.homecontrolbetaversion.ui.settings;

import com.example.irek.homecontrolbetaversion.App;
import com.example.irek.homecontrolbetaversion.data.model.SettingsPreferences;
import com.example.irek.homecontrolbetaversion.ui.base.BasePresenter;
import com.example.irek.homecontrolbetaversion.utils.SharedPrefManager;

import java.util.Map;

/**
 * Created by Wojtek on 24.04.2017.
 */

public class SettingsPresenter implements BasePresenter<SettingsView> {
    private SettingsView view;
    private SharedPrefManager sharedPrefManager;

    @Override
    public void onAttach(SettingsView view) {
        this.view = view;
        sharedPrefManager = ((App)((SettingsActivity)this.view).getApplication()).getSharedPrefManager();

        initViewFields();
    }

    private void initViewFields() {
        view.initViewFields(sharedPrefManager.getUserSettings());
    }

    public void updateSharedPref(Map<String, ?> map) {
        sharedPrefManager.updateDts(map);
    }

    public void saveUserSettings() {
        sharedPrefManager.saveUserSettings();
    }

}
