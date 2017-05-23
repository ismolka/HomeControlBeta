package com.example.irek.homecontrolbetaversion.ui.base;

/**
 * Created by Wojtek on 23.04.2017.
 */

public interface BasePresenter<T extends BaseView> {
    public void onAttach(T view);
}
