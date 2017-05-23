package com.example.irek.homecontrolbetaversion.ui.settings;

import com.example.irek.homecontrolbetaversion.data.model.SettingsPreferences;
import com.example.irek.homecontrolbetaversion.ui.base.BaseView;

/**
 * Created by Wojtek on 24.04.2017.
 */

public interface SettingsView extends BaseView {
    public void initViewFields(SettingsPreferences sp);
}
