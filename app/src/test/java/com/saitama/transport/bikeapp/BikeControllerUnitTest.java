package com.saitama.transport.bikeapp;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.saitama.transport.bikeapp.app.BikeApplication;
import com.saitama.transport.bikeapp.app.controller.BikeController;
import com.saitama.transport.bikeapp.app.controller.UserController;
import com.saitama.transport.bikeapp.model.Payment;
import com.saitama.transport.bikeapp.model.Place;
import com.saitama.transport.bikeapp.model.User;

import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, packageName = "com.saitama.transport.bikeapp", manifest = "src/main/AndroidManifest.xml", sdk = 16)
public class BikeControllerUnitTest {

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }


    @Test
    public void requestPlacesTest() throws Exception {
        final BikeController bikeController = new BikeController(RuntimeEnvironment.application);

        User user = new User();
        user.setEmail("crossover@crossover.com");
        user.setPassword("crossover");
        user.setAccessToken("AYqCb97qqSQ2AbMNjKR5skKQbpOpFE3oLr43A9NDFmABpIAtkgAAAAA");

        BikeApplication.setUser(user);

        final List<Place> places = new ArrayList<>();

        bikeController.requestPlaces(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    places.addAll(bikeController.parsePlacesResponse(response));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        Thread.sleep(3000);
        ShadowApplication.runBackgroundTasks();
        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();
        Thread.sleep(3000);

        System.out.print("Places found: "+places.size());
        assertThat(places.isEmpty(), Is.is(false));
    }

    @Test
    public void paymentTest() throws Exception {
        final BikeController bikeController = new BikeController(RuntimeEnvironment.application);

        User user = new User();
        user.setEmail("crossover@crossover.com");
        user.setPassword("crossover");
        user.setAccessToken("AYqCb97qqSQ2AbMNjKR5skKQbpOpFE3oLr43A9NDFmABpIAtkgAAAAA");

        BikeApplication.setUser(user);

        Payment payment = new Payment();
        payment.setNumber("1234");
        payment.setName("Joselito");
        payment.setExpiration("No expiration");
        payment.setCode("922");

        final StringBuilder sb = new StringBuilder();

        bikeController.requestPayment(payment,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    sb.append(bikeController.parsePaymentResponse(response));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        Thread.sleep(3000);
        ShadowApplication.runBackgroundTasks();
        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();
        Thread.sleep(3000);

        System.out.print("Payment Message: "+sb.toString());
        assertThat(sb.toString().trim().isEmpty(), Is.is(false));
    }
}