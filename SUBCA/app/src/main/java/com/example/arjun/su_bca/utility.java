package com.example.arjun.su_bca;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class utility {

    public static String CHANNEL_ID = "SMU";
    public static String CHANNEL_NAME = "SMU_CHANNEL";

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLog (String TAG, String message) {
        Log.d(TAG, message);
    }

    static CollectionReference getCollectionReferenceForNotes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_notes");
    }

    @SuppressLint("SimpleDateFormat")
    static String timeStampToString(Timestamp timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }

    static String accessLoveCalculator(String str) {
        String[] testArray = str.split(" ");

        String name1;
        String name2;
        if(testArray.length == 3) {
            name1 = testArray[1];
            name2 = testArray[2];
        } else {
            name1 = testArray[1] + " " + testArray[2];
            name2 = testArray[3] + " " + testArray[4];
        }
        return loveCalculator(name1, name2);
    }

    static String loveCalculator(String str1, String str2) {

        String name3 = (str1 + str2).toLowerCase();

        int t = counter(name3, 't');
        int r = counter(name3, 'r');
        int u = counter(name3, 'u');
        int e = counter(name3, 'e');
        int l = counter(name3, 'l');
        int o = counter(name3, 'o');
        int v = counter(name3, 'v');

        int sum1 = t + r + u + e, sum2 = l + o + v + e;

        int love = Integer.parseInt(sum1 + "" + sum2);

        return showResult(love);
    }

    static String showResult(int love) {
        if(love < 10 || love > 90) {
            return "Your score is " + love + ", you go together like coke and mentos.";
        } else if(love >= 50) {
            return "Your score is " + love + ", you're alright together.";
        } else {
            return "Your score is " + love + ".";
        }
    }

    static int counter(String name, char x) {
        int count = 0;
        for(char character : name.toCharArray()) {
            if(character == x) {
                count++;
            }
        }
        return count;
    }

    public static void CreateNotification (Context context, String title, String body) {

        int flags;

        if (Build.VERSION.SDK_INT < 31) {
            flags = PendingIntent.FLAG_UPDATE_CURRENT;
        } else {
            flags = PendingIntent.FLAG_IMMUTABLE;
        }

        Intent content = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pendingContent = PendingIntent.getActivity(context.getApplicationContext(), 9, content, flags);

        NotificationManager manager = (NotificationManager) context.getSystemService(NotificationManager.class);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(Color.BLUE)
                .setContentIntent(pendingContent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setAutoCancel(true)
                .build();

        manager.notify(8, notification);

    }

}
