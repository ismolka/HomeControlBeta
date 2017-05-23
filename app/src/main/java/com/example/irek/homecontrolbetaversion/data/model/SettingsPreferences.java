package com.example.irek.homecontrolbetaversion.data.model;

/**
 * Created by Wojtek on 17.05.2017.
 */

public class SettingsPreferences {
    private boolean showNotifications;
    private boolean notifyTemp;
    private Long tempPercentageDiff;
    private boolean notifyMotion;

    public boolean isShowNotifications() {
        return showNotifications;
    }

    public void setShowNotifications(boolean showNotifications) {
        this.showNotifications = showNotifications;
    }

    public boolean isNotifyTemp() {
        return notifyTemp;
    }

    public void setNotifyTemp(boolean notifyTemp) {
        this.notifyTemp = notifyTemp;
    }

    public Long getTempPercentageDiff() {
        return tempPercentageDiff;
    }

    public void setTempPercentageDiff(Long tempPercentageDiff) {
        this.tempPercentageDiff = tempPercentageDiff;
    }

    public boolean isNotifyMotion() {
        return notifyMotion;
    }

    public void setNotifyMotion(boolean notifyMotion) {
        this.notifyMotion = notifyMotion;
    }
}
