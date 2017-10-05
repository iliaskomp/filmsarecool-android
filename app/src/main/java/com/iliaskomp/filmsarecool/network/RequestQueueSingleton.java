package com.iliaskomp.filmsarecool.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by IliasKomp on 21/09/17.
 */

public class RequestQueueSingleton {
    private static RequestQueueSingleton sInstance;
    private RequestQueue mRequestQueue;
    private static Context sContext;

    private RequestQueueSingleton(Context context) {
        sContext = context;
        mRequestQueue = getRequestQueue();
    }


    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RequestQueueSingleton(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext());
        }
        return mRequestQueue;
    }
}
