package com.example.arjun.su_bca.Utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestQueue {
    private volatile static VolleyRequestQueue instance;
    private static Context context;
    private volatile RequestQueue queue;

    private VolleyRequestQueue (Context context) {
        VolleyRequestQueue.context = context;
        queue = getRequestQueue();
    }

    public static synchronized VolleyRequestQueue getInstance (Context context) {
        if (instance == null) {
            instance = new VolleyRequestQueue(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue () {
        if (queue == null) {
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return queue;
    }

    public RequestQueue getQueue () {
        return queue;
    }

}
