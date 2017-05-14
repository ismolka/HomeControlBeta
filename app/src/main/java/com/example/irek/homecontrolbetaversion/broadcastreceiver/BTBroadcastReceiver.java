package com.example.irek.homecontrolbetaversion.broadcastreceiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.irek.homecontrolbetaversion.service.BTService;

/**
 * Created by Wojtek on 01.05.2017.
 */

public class BTBroadcastReceiver extends BroadcastReceiver {

    //private Intent serviceIntent = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            Intent serviceIntent = null;

            switch (state) {
                case BluetoothAdapter.STATE_ON:
                    Toast.makeText(context, "bt on", Toast.LENGTH_SHORT).show();
                    serviceIntent = new Intent(context, BTService.class);
                    context.startService(serviceIntent);
                    break;
                case BluetoothAdapter.STATE_OFF:
                    //if (serviceIntent != null) {
                        Log.d("Test", "test2");
                        Toast.makeText(context, "bt off", Toast.LENGTH_SHORT).show();
                        serviceIntent = new Intent(context, BTService.class);
                        context.stopService(serviceIntent);
                        //serviceIntent = null;
                    //}
                    break;
            }
        }
    }
}
