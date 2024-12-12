package com.example.arjun.su_bca.services;

import androidx.annotation.NonNull;

import com.example.arjun.su_bca.Utils.utility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        RemoteMessage.Notification notification = message.getNotification();

        utility.CreateNotification(getApplicationContext(),
                notification.getTitle(),
                notification.getBody()
        );

    }
}