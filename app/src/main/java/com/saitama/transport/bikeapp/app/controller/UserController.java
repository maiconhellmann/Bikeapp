package com.saitama.transport.bikeapp.app.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.saitama.transport.bikeapp.R;
import com.saitama.transport.bikeapp.app.BikeApplication;
import com.saitama.transport.bikeapp.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class UserController {

    private Context context;


    public UserController(Context context) {
        this.context = context;
    }

    public void attempLogin(User user, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws Exception {
        String url = context.getString(R.string.api_address, context.getString(R.string.api_auth));

        HashMap<String, String> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());

        int method = Request.Method.POST;

        BikeApplication.doRequestAsync(method, url, params, listener, errorListener);
    }


    public String parseLoginAttempResponse(JSONObject response) throws Exception {
        if(!response.has("accessToken")){
            throw new Exception(context.getString(R.string.login_invalid));
        }

        return response.getString("accessToken");
    }

    public void persistUser(User user) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.putString("accessToken", user.getAccessToken());

        editor.apply();

        BikeApplication.setUser(user);
    }

    public User queryLoggedUser(){
        User user = null;

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);

        String email = sharedPref.getString("email", null);
        String password = sharedPref.getString("password", null);
        String accessToken = sharedPref.getString("accessToken", null);

        if(email != null && password != null && accessToken != null){
            user = new User();
            user.setAccessToken(accessToken);
            user.setPassword(password);
            user.setEmail(email);

            BikeApplication.setUser(user);
        }
        return user;
    }

    public void createUser(User user, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        String url = context.getString(R.string.api_address, context.getString(R.string.api_register));

        HashMap<String, String> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());

        int method = Request.Method.POST;

        BikeApplication.doRequestAsync(method, url, params, listener, errorListener);
    }
}
