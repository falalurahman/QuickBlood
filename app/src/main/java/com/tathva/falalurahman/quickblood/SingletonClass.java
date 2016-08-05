package com.tathva.falalurahman.quickblood;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonClass {

    private static SingletonClass mInstance;
    private RequestQueue requestQueue;
    private static Context mContext;

    public SingletonClass(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized SingletonClass getInstance(Context context){
        if( mInstance == null ){
            mInstance = new SingletonClass(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
