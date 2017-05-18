package com.example.irek.homecontrolbetaversion.service;

/**
 * Defines several constants used between {@link BluetoothChatService} and the UI.
 */
public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final int DO_RECONNECT = 6;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public static final String INTENT_NOTIFY_ACTION = "com.example.irek.homecontrolbeta.broadcast.NOTIFY_USER";
    public static final int NOTIFY_TEMP = 7;
    public static final int NOTIFY_RUCH = 8;
}
