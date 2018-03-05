package com.example.abdel.yourfavredditclient.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.example.abdel.yourfavredditclient.R;

/**
 * Created by abdel on 3/5/2018.
 */

public class NotificationService extends IntentService {

    final int NOTIFICATION_ID = 1;

    public NotificationService() {
        super(NotificationService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);

        manager.notify(
                NOTIFICATION_ID,
                new NotificationCompat.Builder(this)
                        .setContentTitle(getString(R.string.notification_title))
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentText(getString(R.string.notification_content))
                        .build()
        );
    }
}
