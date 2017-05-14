package com.example.irek.homecontrolbetaversion.service;

import android.os.Binder;

import com.example.irek.homecontrolbetaversion.ui.base.IListenerFunctions;

/**
 * Created by Wojtek on 04.05.2017.
 */

public class ServiceBinder extends Binder {
    private BTService service;

    public ServiceBinder(BTService s) {
        this.service = s;
    }

    public BTService getService() {
        return service;
    }

    public void registerCallback(IListenerFunctions mCallback) {
        service.registerCallback(mCallback);
    }

    public void unregisterCallback() {
        service.unregisterCallback();
    }
}
