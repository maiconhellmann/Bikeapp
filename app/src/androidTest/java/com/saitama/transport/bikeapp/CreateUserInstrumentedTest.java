package com.saitama.transport.bikeapp;

import android.support.test.espresso.Espresso;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.saitama.transport.bikeapp.activity.LoginActivity;
import com.saitama.transport.bikeapp.app.BikeApplication;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class CreateUserInstrumentedTest {

    private String email ="someone@someone.com";
    private String password ="someone";

    @Rule
    public ActivityTestRule<LoginActivity> activity = new ActivityTestRule<>(LoginActivity.class);

    private String TAG = getClass().getSimpleName();

    @Test
    public void login(){
        BikeApplication.setUser(null);

        Log.d(TAG, "Starting tests");

        Log.d(TAG, "Input username");
        onView(withId(R.id.loginActivity_username)).perform(click()).perform(replaceText(email));

        Espresso.pressBack();

        //Senha
        Log.d(TAG, "Input password "+ password);
        onView(withId(R.id.loginActivity_password)).perform(click()).perform(replaceText(password));

        Espresso.pressBack();

        onView(withId(R.id.loginActivity_createAccount)).perform(click());

        getInstrumentation().waitForIdleSync();

        Log.d(TAG, "Verify activity opened");
        junit.framework.Assert.assertNotNull("User is null", BikeApplication.getUser());
    }
}
