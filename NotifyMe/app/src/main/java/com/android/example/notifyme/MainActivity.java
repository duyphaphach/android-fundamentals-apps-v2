/*
 *
 *   Copyright (C) 2018 Google Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.android.example.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * MainActivity for the Notify Me! app. Contains three buttons that deliver,
 * update, and cancel notification.
 */
public class MainActivity extends AppCompatActivity {

    private Button button_notify;
    private Button button_update;
    private Button button_cancel;

    // BroadcastReceiver
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";

    private NotificationReceiver mReceiver = new NotificationReceiver();

    // Define Notification Channel properties
    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "notify_me";
    private static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

    // Define Notification
    private static final int NOTIFICATION_ID = 1;

    // Deliver notification to users
    private NotificationManager notificationManager;

    // Init a Notification Channel
    public void createNotificationChannel(){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    // Init a NotiBuilder
    private NotificationCompat.Builder getNotiBuilder (){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notiPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("You have been notified")
            .setSmallIcon(R.drawable.ic_android)
            .setContentText("Now you are seeing some content texts")
            .setContentIntent(notiPendingIntent)
            .setAutoCancel(true);
    }

    // Trigger notification using notificationManager
    private void sendNotification() {
        NotificationCompat.Builder notiBuilder = getNotiBuilder();
        notificationManager.notify(NOTIFICATION_ID, notiBuilder.build());
    }

    // Update notification using notificationManager
    public void updateNotification() {
        NotificationCompat.Builder notiBuilder = getNotiBuilder();
        notiBuilder.setStyle(
                new NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.mascot_1))
                .setBigContentTitle("Notification Updated!"));
        notificationManager.notify(NOTIFICATION_ID, notiBuilder.build());
    }

    // Cancel notification using notificationManager
    public void cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init notify button
        button_notify = findViewById(R.id.notify);

        // Add button listener
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        button_update = findViewById(R.id.update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotification();
            }
        });

        button_cancel = findViewById(R.id.cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
            }
        });

        createNotificationChannel();
        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));
    }

    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }

        @Override
        protected void onDestroy() {
            unregisterReceiver(mReceiver);
            super.onDestroy();
        }
    }
}
