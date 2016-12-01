package com.saitama.transport.bikeapp.app.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saitama.transport.bikeapp.R;
import com.saitama.transport.bikeapp.app.BikeApplication;
import com.saitama.transport.bikeapp.model.Payment;
import com.saitama.transport.bikeapp.model.Place;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class BikeController {

    private Context context;

    public BikeController(Context context) {
        this.context = context;
    }

    public void requestPlaces(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws Exception {
        String url = context.getString(R.string.api_address, context.getString(R.string.api_places));

        int method = Request.Method.GET;

        BikeApplication.doRequestAsync(method, url, new HashMap<String, String>(), listener, errorListener);
    }

    public List<Place> parsePlacesResponse(JSONObject response) throws Exception {
        Type listType = new TypeToken<ArrayList<Place>>() {}.getType();
        return new Gson().fromJson(response.getString("results"), listType);
    }

    public void requestPayment(Payment payment, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        String url = context.getString(R.string.api_address, context.getString(R.string.api_rent));

        Map<String, String> map = new HashMap<>();
        map.put("code", payment.getCode());
        map.put("expiration", payment.getExpiration());
        map.put("name", payment.getName());
        map.put("number", payment.getNumber());

        int method = Request.Method.POST;

        BikeApplication.doRequestAsync(method, url, map, listener, errorListener);
    }

    public String parsePaymentResponse(JSONObject response) throws JSONException {
        String message = "";

        if(response.has("message")){
            message=response.getString("message");
        }
        return message;
    }
}
