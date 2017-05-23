package com.example.irek.homecontrolbetaversion.broadcastreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.irek.homecontrolbetaversion.R;
import com.example.irek.homecontrolbetaversion.service.BTService;
import com.example.irek.homecontrolbetaversion.service.Constants;
import com.example.irek.homecontrolbetaversion.ui.home.HomeActivity;

/**
 * Created by Wojtek on 01.05.2017.
 */

public class BTBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BTBroadcastReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();

        if(action.equals(Constants.INTENT_NOTIFY_ACTION)) {
            final PendingResult pendingResult = goAsync();

            AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    final int notificationType = intent.getIntExtra("notificationType", 0);
                    switch (notificationType) {
                        case Constants.NOTIFY_RUCH:
                            showNotification(context, notificationType);
                            break;
                        case Constants.NOTIFY_TEMP:
                            showNotification(context, notificationType);
                            break;
                    }

                    pendingResult.finish();
                    return null;
                }
            };
            asyncTask.execute();
        }
    }

    private void showNotification(Context context, int notificationType) {
        Log.d(TAG, "showing notification type: " + notificationType);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("HCB notification")
                .setContentText(notificationType == Constants.NOTIFY_RUCH ? "Wykryto ruch" : "Przekroczona temperatura")
                .setAutoCancel(true);

        Intent resultIntent = new Intent(context, HomeActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify("hcb", notificationType == Constants.NOTIFY_RUCH ? 0 : 1, mBuilder.build());
    }
}
