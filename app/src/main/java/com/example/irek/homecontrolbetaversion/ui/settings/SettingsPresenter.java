package com.example.irek.homecontrolbetaversion.ui.settings;

import com.example.irek.homecontrolbetaversion.ui.base.BasePresenter;

/**
 * Created by Wojtek on 24.04.2017.
 */

public class SettingsPresenter implements BasePresenter<SettingsView> {
    private SettingsView view;

    @Override
    public void onAttach(SettingsView view) {
        this.view = view;
    }
}
