package com.saitama.transport.bikeapp.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.saitama.transport.bikeapp.app.controller.UserController;
import com.saitama.transport.bikeapp.model.User;
import com.saitama.transport.bikeapp.utils.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Class responsible for keeping singleton of context, user, and RequestQueue.
 * It also provides heavily used methods in various parts of the application.
 */
public class BikeApplication extends Application {

    public static final String TAG = BikeApplication.class.getSimpleName();
    /**
     * Context Singleton
     */
    private static Context context;

    /**
     * RequestQueue Singleton
     */
    private static RequestQueue mRequestQueue;

    /**
     * User Singleton
     */
    private static User loggerUser;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            context = getApplicationContext();

            UserController user = new UserController(context);
            loggerUser = user.queryLoggedUser();

        }catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Get the logged user
     * @return logged user
     */
    public static User getUser(){
        return loggerUser;
    }

    /**
     * Verify if user is logged
     * @return true if user is logged
     */
    public static boolean isLoggedUser(){
        return loggerUser != null;
    }

    /**
     * Get the context
     * @return context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * Get RequestQueue
     * @return RequestQueue
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getContext());
        }

        return mRequestQueue;
    }

    /**
     * Add request to queue
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * Add reqyest ti queue
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * Cancel pending requests
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Alias to get string from resource
     */
    public static String getStringRes(@StringRes int res, Object... args){
        return getContext().getString(res, args);
    }

    /**
     * Do request to server API
     */
    public static void doRequestAsync(int method, String apiUrl, Map<String, String> paramMap, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        Request request;
        if(method == Request.Method.POST){
            JSONObject json = new JSONObject();

            if(paramMap != null) {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    json.put(entry.getKey(), entry.getValue());
                }
            }
            request = new JsonObjectRequest(method, apiUrl,json, listener, errorListener){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();

                    User user = BikeApplication.getUser();
                    if(user != null && user.getAccessToken() != null) {
                        headers.put("Authorization", user.getAccessToken());
                    }
                    return headers;
                }
            };
        }else{
            request = new CustomRequest(method, apiUrl, paramMap, listener, errorListener);
        }

        request.setRetryPolicy(new DefaultRetryPolicy(5000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adiciona a lista de requisições
        BikeApplication.getRequestQueue().add(request);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try{
            MultiDex.install(this);
        }catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Set the logged user
     */
    public static void setUser(User user) {
        loggerUser = user;
    }
}
