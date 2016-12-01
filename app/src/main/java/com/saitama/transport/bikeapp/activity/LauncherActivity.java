package com.saitama.transport.bikeapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.saitama.transport.bikeapp.app.BikeApplication;
import com.saitama.transport.bikeapp.utils.AlertUtils;

/**
 * Class responsible for driving the app flow
 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    /**
     * Appflow
     * Is user is logged then start MapActivity else start login activity
     */
    private void init() {
        try{
            //Is user is logged then start MapActivity
            if(BikeApplication.isLoggedUser()){
                startMapActivity(this);
            }else{
                //If not logged then start loginActivity
                startLoginActivity(this);
            }
        }catch (Exception e){
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Start map activity
     */
    public static void startMapActivity(Activity activity){
        activity.startActivity(new Intent(activity, MapActivity.class));
        activity.finish();
    }

    /**
     * Start LoginActivity to sign in
     */
    public static void startLoginActivity(Activity activity){
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }
}
