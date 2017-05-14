package com.example.irek.homecontrolbetaversion.ui.connect;

import com.example.irek.homecontrolbetaversion.ui.base.BasePresenter;

/**
 * Created by Wojtek on 24.04.2017.
 */

public class ConnectPresenter implements BasePresenter<ConnectView> {
    private ConnectView view;

    @Override
    public void onAttach(ConnectView view) {
        this.view = view;
    }
}
