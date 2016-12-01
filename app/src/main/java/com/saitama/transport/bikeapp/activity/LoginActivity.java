package com.saitama.transport.bikeapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.saitama.transport.bikeapp.R;
import com.saitama.transport.bikeapp.app.controller.UserController;
import com.saitama.transport.bikeapp.model.User;
import com.saitama.transport.bikeapp.utils.AlertUtils;

import org.json.JSONObject;

/**
 * Class responsible for displaying and controlling the login screen and user registry
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {

//    Components
    private TextInputEditText mEmailEditText;
    private TextInputEditText mPasswordEditText;
    private View mCreateAccountButton;
    private View mLoginButton;
    private ProgressDialog progressDialog;

    private UserController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            mController = new UserController(this);
            initComponents();
            initListeners();
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Init view componets
     */
    private void initComponents(){
        mEmailEditText = (TextInputEditText)findViewById(R.id.loginActivity_username);
        mPasswordEditText = (TextInputEditText)findViewById(R.id.loginActivity_password);
        mCreateAccountButton = findViewById(R.id.loginActivity_createAccount);
        mLoginButton = findViewById(R.id.loginActivity_login);
    }

    /**
     * Init componet listener
     */
    private void initListeners(){
        mCreateAccountButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
            if(view.getId() == mCreateAccountButton.getId()){
                onClickCreateAccount();
            }else if(view.getId() == mLoginButton.getId()){
                onClickLogin();
            }
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Validate the login form
     * @return true if form is valid
     */
    private boolean validateForm(){
        boolean valid = true;

        try {
            if(mEmailEditText.getText().toString().isEmpty()){
                mEmailEditText.setError(getString(R.string.username_required));
                valid=false;
            }
            if(mPasswordEditText.getText().toString().isEmpty()){
                mPasswordEditText.setError(getString(R.string.password_required));
                valid=false;
            }

        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
        return valid;
    }

    /**
     * Get the form as User class
     * @return User
     */
    private User getUserFromForm(){
        User user = null;
        try {
            String email = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();

            user = new User();
            user.setEmail(email);
            user.setPassword(password);

        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }

        return user;
    }

    /**
     * Show progress bar indicating a background task
     */
    private void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setTitle(getString(R.string.wait));
        progressDialog.show();
    }

    /**
     * Action when user click on login button
     */
    private void onClickLogin() {
        try {
            if(validateForm()){
                User user = getUserFromForm();
                mController.attempLogin(user, this, this);

                showProgressDialog();
            }
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * ACtion where user click "create account" button
     */
    private void onClickCreateAccount() {
        try {
            if(validateForm()){
                showProgressDialog();

                User user  = getUserFromForm();

                mController.createUser(user, this, this);
            }
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Server response of login or create account
     * @param error if bad request
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            progressDialog.dismiss();
            if(error.networkResponse != null && error.networkResponse.statusCode == 401){
                throw new Exception(getString(R.string.login_invalid));
            }else {
                throw error;
            }
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Success callback of login or create account event
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        try {
            User user = getUserFromForm();
            String token = mController.parseLoginAttempResponse(response);

            user.setAccessToken(token);

            mController.persistUser(user);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    LauncherActivity.startMapActivity(LoginActivity.this);
                }
            }, 2000);
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }
}
