package com.saitama.transport.bikeapp;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.saitama.transport.bikeapp.app.controller.UserController;
import com.saitama.transport.bikeapp.model.User;

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

import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, packageName = "com.saitama.transport.bikeapp", manifest = "src/main/AndroidManifest.xml", sdk = 16)
public class UserControllerUnitTest {

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void loginSuccess() throws Exception {
        User user = new User();
        user.setPassword("crossover");
        user.setEmail("crossover@crossover.com");

        user = attempLogin(user);
        System.out.println("User token: "+user.getAccessToken());
        assertThat(user.getAccessToken(), IsNull.notNullValue());
    }
    @Test
    public void loginError() throws Exception {
        User user = new User();
        user.setPassword("someone");
        user.setEmail("someone@someone.com");

        user = attempLogin(user);
        assertThat(user.getAccessToken(), IsNull.nullValue());
    }

    @Test
        public void createUser() throws Exception {
        User user = new User();
        user.setPassword("someone");
        user.setEmail("someone@someone.com");

        user = createUser(user);
        assertThat(user.getAccessToken(), IsNull.notNullValue());
    }



    public User attempLogin(final User user) throws Exception {
        final UserController userController = new UserController(RuntimeEnvironment.application);

        userController.attempLogin(user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token = userController.parseLoginAttempResponse(response);
                    user.setAccessToken(token);
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

        return user;
    }

    public User createUser(final User user) throws Exception {
        final UserController userController = new UserController(RuntimeEnvironment.application);

        userController.createUser(user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token = userController.parseLoginAttempResponse(response);
                    user.setAccessToken(token);
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

        return user;
    }
}