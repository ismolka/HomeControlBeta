package com.example.irek.homecontrolbetaversion.service;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.irek.homecontrolbetaversion.App;
import com.example.irek.homecontrolbetaversion.data.model.DaoSession;
import com.example.irek.homecontrolbetaversion.data.model.DeviceData;
import com.example.irek.homecontrolbetaversion.data.model.DeviceDataDao;
import com.example.irek.homecontrolbetaversion.data.model.Request;
import com.example.irek.homecontrolbetaversion.data.model.SettingsPreferences;
import com.example.irek.homecontrolbetaversion.ui.base.IListenerFunctions;
import com.example.irek.homecontrolbetaversion.utils.MessageParser;
import com.example.irek.homecontrolbetaversion.utils.SharedPrefManager;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Wojtek on 27.04.2017.
 */

public class BTService extends Service {

    private final String TAG = "BTService";
    private IBinder mBinder = null;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mBluetoothChatService = null;
    private String mConnectedDeviceName = null;
    private String deviceMACAddress = "3C:BB:FD:6E:DF:F2"; // mama tel mac addr: 3C:BB:FD:6E:DF:F2 ; irek mac addr: 20:16:11:07:59:62
    private Thread mThread = null;
    private Thread requestThread = null;

    private DeviceDataDao deviceDataDao;

    private Map<Activity, IListenerFunctions> mCallbacks = new ConcurrentHashMap<Activity, IListenerFunctions>();

    private Request requestModel = new Request("getdata");
    private String requestJson = "null";

    private SettingsPreferences settingsPreferences;
    private SharedPrefManager sharedPrefManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "running service");
        Toast.makeText(this, "service running", Toast.LENGTH_SHORT).show();
        //stopSelf(startId);

        return START_STICKY; // jak padnie to padnie i czeka na ponowne odpalenie = START_NOT_STICKY
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBinder = new ServiceBinder(this);
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "creating service");

        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        deviceDataDao = daoSession.getDeviceDataDao();

        sharedPrefManager = ((App) getApplication()).getSharedPrefManager();
        settingsPreferences = sharedPrefManager.getUserSettings();


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothChatService = new BluetoothChatService(getApplicationContext(), mHandler);

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBluetoothChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBluetoothChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mBluetoothChatService.start();
                connectDevice();
            }
        }

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Request> jsonAdapter = moshi.adapter(Request.class);
        requestJson = jsonAdapter.toJson(requestModel);
        Log.d(TAG, "requestJson string: " + requestJson);

        requestThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "starting requestThread");
                while(true) {
                    if (mBluetoothChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
                        Log.d(TAG, "sending msg to device");
                        mBluetoothChatService.write(requestJson.getBytes());
                    } else {
                        Log.d(TAG, "device not connected");
                    }
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d(TAG, "requestThread interrupted");
                    }
                }
            }
        });
        requestThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroying service");
        Log.d(TAG, "forcing thread to stop");

        if(requestThread != null) {
            Log.d(TAG, "going to interrupt requestThread");
            requestThread.interrupt();
        }

        if(mThread != null) {
            Log.d(TAG, "going to interrupt mThread");
            mThread.interrupt();
        }

        if (mBluetoothChatService != null) {
            mBluetoothChatService.stop();
        }
    }

    public void registerCallback(Activity activity, IListenerFunctions callback) {
        mCallbacks.put(activity, callback);
        Log.d(TAG, "callback registered");

        switch(mBluetoothChatService.getState()) {
            case BluetoothChatService.STATE_CONNECTED:
                callback.reefreshActionBarStatus("connected");
                break;
            case BluetoothChatService.STATE_CONNECTING:
                callback.reefreshActionBarStatus("connecting...");
                break;
            case BluetoothChatService.STATE_LISTEN:
            case BluetoothChatService.STATE_NONE:
                callback.reefreshActionBarStatus("not connected");
                break;
        }
    }

    public void unregisterCallback(Activity activity) {
        Log.d(TAG, "unregistering callback");
        this.mCallbacks.remove(activity);
    }

    public void testujemy() {
        Log.d(TAG, "testujemy lolo");
    }

    private void connectDevice() {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceMACAddress);
        mBluetoothChatService.connect(device, false);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    for(Activity client : mCallbacks.keySet()) {
                        IListenerFunctions callback = mCallbacks.get(client);
                        switch (msg.arg1) {
                            case BluetoothChatService.STATE_CONNECTED:
                                callback.reefreshActionBarStatus("connected");
                                //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                                //mConversationArrayAdapter.clear();
                                break;
                            case BluetoothChatService.STATE_CONNECTING:
                                callback.reefreshActionBarStatus("connecting...");
                                //setStatus(R.string.title_connecting);
                                break;
                            case BluetoothChatService.STATE_LISTEN:
                            case BluetoothChatService.STATE_NONE:
                                callback.reefreshActionBarStatus("not connected");
                                //setStatus(R.string.title_not_connected);
                                break;
                        }
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d(TAG, "MESSAGE_READ: " + readMessage);
                    processReceivedMessage(readMessage);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    //if (null != activity) {
                        Toast.makeText(/*activity*/ getApplicationContext(), "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    //}
                    break;
                case Constants.MESSAGE_TOAST:
                    //if (null != activity) {
                        Toast.makeText(/*activity*/ getApplicationContext(), msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    //}
                    break;
                case Constants.DO_RECONNECT:
                    mThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d(TAG, "reconnecting thread going to sleep!!!!!!");
                                Thread.sleep(20000);
                                Log.d(TAG, "reconnecting thread is waking up !!!!!!!");

                                synchronized (BTService.this) {
                                    mThread = null;
                                }

                                connectDevice();
                            } catch (InterruptedException e) {
                                Log.d(TAG, "mThread has been interrupted :D");
                                e.printStackTrace();
                            }
                        }
                    });
                    mThread.start();
            }
        }
    };

    private void processReceivedMessage(String message) {
        if (MessageParser.isJSONValid(message)) {
            Log.d(TAG, "json is valid");
            DeviceData model = MessageParser.parseMessageToModelObject(message);
            if(model != null) {
                deviceDataDao.insert(model);
                Log.d(TAG, "Inserted new note, ID: " + model.getId());
                if(settingsPreferences.isShowNotifications()) {
                    checkNotificationConditions(model);
                }
                for(Activity client : mCallbacks.keySet()) {
                    Log.d(TAG, "calling activity to refresh interface");
                    mCallbacks.get(client).refreshInterface();
                }
            } else {
                Log.d(TAG, "received corrupted message");
            }
        } else {
            Log.d(TAG, "json is invalid");
        }
    }

    public void writeToDevice(byte[] message) {
        // Check that we're actually connected before trying anything
        if (mBluetoothChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Log.d(TAG, "We are not connected to anything. Not going to send a msg to device.");
            return;
        }
        Log.d(TAG, "message to device: " + message.toString());
        mBluetoothChatService.write(message);
    }

    private void checkNotificationConditions(DeviceData data) {
        Log.d(TAG, "checkNotificationConditions");
        if(settingsPreferences.isNotifyTemp()) {
            int diff = Math.abs((int) ((data.getTempDom() * 100) / sharedPrefManager.getSharedPrefDataToSend().getPrefTempDom() - 100));
            Log.d(TAG, "diff = " + diff);
            if(diff >= settingsPreferences.getTempPercentageDiff()) {
                Log.d(TAG, "sending notfity temp intent");
                sendBroadcast(getBroadcastIntent(Constants.NOTIFY_TEMP));
            }
        }
        if(settingsPreferences.isNotifyMotion()) {
            Log.d(TAG, "sending notify motion intent");
            if(data.isWykrytoRuch())
                sendBroadcast(getBroadcastIntent(Constants.NOTIFY_RUCH));
        }
    }

    private Intent getBroadcastIntent(int extraInt) {
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_NOTIFY_ACTION);
        intent.putExtra("notificationType", extraInt);
        return intent;
    }
}
