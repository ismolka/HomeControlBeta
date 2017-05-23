package com.example.irek.homecontrolbetaversion.ui.login;

import com.example.irek.homecontrolbetaversion.ui.base.BasePresenter;

/**
 * Created by Wojtek on 24.04.2017.
 */

public class LogonPresenter implements BasePresenter<LogonView> {
    private LogonView view;

    @Override
    public void onAttach(LogonView view) {
        this.view = view;
    }
}
