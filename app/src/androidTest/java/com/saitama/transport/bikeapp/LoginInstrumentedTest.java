package com.saitama.transport.bikeapp;

import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import static android.support.test.espresso.Espresso.*;

import com.saitama.transport.bikeapp.activity.LauncherActivity;
import com.saitama.transport.bikeapp.activity.LoginActivity;
import com.saitama.transport.bikeapp.app.BikeApplication;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.InstrumentationRegistry.*;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class LoginInstrumentedTest {

    @Rule
    public ActivityTestRule<LoginActivity> activity = new ActivityTestRule<>(LoginActivity.class);

    private String TAG = getClass().getSimpleName();

    @Test
    public void login(){
        BikeApplication.setUser(null);

        Log.d(TAG, "Starting tests");

        Log.d(TAG, "Input username");
        String email = activity.getActivity().getString(R.string.default_email);
        onView(withId(R.id.loginActivity_username)).perform(click()).perform(replaceText(email));

        Espresso.pressBack();

        //Senha
        String password = activity.getActivity().getString(R.string.default_pass);
        Log.d(TAG, "Input password "+ password);
        onView(withId(R.id.loginActivity_password)).perform(click()).perform(replaceText(password));

        Espresso.pressBack();

        onView(withId(R.id.loginActivity_login)).perform(click());

        getInstrumentation().waitForIdleSync();

        Log.d(TAG, "Verify activity opened");
        junit.framework.Assert.assertNotNull("User is null", BikeApplication.getUser());
    }
}
