package com.saitama.transport.bikeapp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.saitama.transport.bikeapp.R;
import com.saitama.transport.bikeapp.app.controller.BikeController;
import com.saitama.transport.bikeapp.model.Payment;
import com.saitama.transport.bikeapp.model.Place;
import com.saitama.transport.bikeapp.utils.AlertUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Class responsible for displaying and controlling map, place and payment information
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, Response.Listener<JSONObject>, Response.ErrorListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {


    private BikeController mController;
    private List<Place> placeList;
    private Place placeSelected;

    //Components
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private View rentLayout;
    private TextView rentNameTextView;
    private Button rentCancelButton;
    private Button rentConfirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        try {
            initComponents();
            initListeners();
            initData();
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    private void initListeners() {
        try {
            rentCancelButton.setOnClickListener(this);
            rentLayout.setOnClickListener(this);
            rentConfirmButton.setOnClickListener(this);
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Init screen components
     */
    private void initComponents() {
        setUpMapIfNeeded();

        rentLayout = findViewById(R.id.mapActivity_rentLayout);
        rentNameTextView = (TextView)findViewById(R.id.mapActivity_rentName);
        rentCancelButton = (Button)findViewById(R.id.mapActivity_rentCancel);
        rentConfirmButton = (Button)findViewById(R.id.mapActivity_rent);
    }

    /**
     * Init data and default behaviors
     */
    private void initData() throws Exception {
        mController = new BikeController(this);
    }

    /**
     * Set value of views
     */
    private void setViews() {
        if(placeList != null){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(Place place: placeList){
                LatLng position = new LatLng(place.getLocation().getLat(), place.getLocation().getLng());

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position)
                        .title(place.getName());

                mMap.addMarker(markerOptions).setTag(place);
                builder.include(position);
            }

            if(placeList != null && !placeList.isEmpty()) {
                //Center camera
                LatLngBounds center = builder.build();

                //Animate
                CameraUpdate update = CameraUpdateFactory.newLatLngBounds(center, 80);
                mMap.animateCamera(update);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            setUpMapIfNeeded();
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Setup the map when needs.
     */
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .build();
            mGoogleApiClient.connect();
            mapFragment.getMapAsync(this);
        }
    }


    /**
     * Fired when googleMap is ready
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.setOnMarkerClickListener(this);

            mController.requestPlaces(this, this);
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Fired when googleMap connected
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Unused
     */
    @Override
    public void onConnectionSuspended(int i) {}

    /**
     * Success callback for the places request
     */
    @Override
    public void onResponse(JSONObject response) {
        try {
            placeList = mController.parsePlacesResponse(response);

            setViews();
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Error callback for the places request
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            throw error;
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Action when user click on marker
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        this.placeSelected = (Place) marker.getTag();
        rentNameTextView.setText(marker.getTitle());

        rentLayout.setVisibility(View.VISIBLE);
        rentLayout.clearAnimation();
        rentLayout.setTranslationY(0);

        Animation animation = new TranslateAnimation(0, 0,5000, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        rentLayout.startAnimation(animation);


        return false;
    }

    /**
     * Action fired when user click on rent a bike
     */
    private void onClickRent() {
        try {
            onClickCancelRent();

            LayoutInflater inflater = getLayoutInflater();
            final View dialoglayout = inflater.inflate(R.layout.fragment_payment, null);

            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setView(dialoglayout);
            b.setTitle(R.string.payment);
            b.setPositiveButton(R.string.label_ok, null);
            b.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            final AlertDialog dialog = b.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(onClickConfirmPayment(dialoglayout)){
                        dialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Action fired when user confirm payment
     */
    private boolean onClickConfirmPayment(View dialoglayout) {
        boolean validForm = true;
        try {
            EditText numberEditText = (EditText) dialoglayout.findViewById(R.id.paymentFragment_number);
            EditText nameEditText = (EditText) dialoglayout.findViewById(R.id.paymentFragment_name);
            EditText expirationEditText = (EditText) dialoglayout.findViewById(R.id.paymentFragment_expiration);
            EditText codeEditText = (EditText) dialoglayout.findViewById(R.id.paymentFragment_code);

            String number = numberEditText.getText().toString();
            String name = nameEditText.getText().toString();
            String expiraiton = expirationEditText.getText().toString();
            String code = codeEditText.getText().toString();

            if(number.isEmpty()){
                numberEditText.setError(getString(R.string.required));
            }
            if(name.isEmpty()){
                nameEditText.setError(getString(R.string.required));
            }
            if(expiraiton.isEmpty()){
                expirationEditText.setError(getString(R.string.required));
            }
            if(code.isEmpty()){
                codeEditText.setError(getString(R.string.required));
            }

            if(!number.isEmpty() && !name.isEmpty() && !expiraiton.isEmpty() && !code.isEmpty()){

                showProgressDialog();

                Payment payment = new Payment();
                payment.setCode(code);
                payment.setExpiration(expiraiton);
                payment.setName(name);
                payment.setNumber(number);

                mController.requestPayment(payment, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onResponsePayment(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onErrorResponsePayment(error);
                    }
                });
            }else{
                validForm = false;
            }
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
        return validForm;
    }

    /**
     * Succes response for payment request
     */
    private void onResponsePayment(JSONObject response) {
        try {
            progressDialog.dismiss();

            String message = mController.parsePaymentResponse(response);

            AlertUtils.showMessage(this, message);
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Error callback for the payment request
     */
    private void onErrorResponsePayment(VolleyError error) {
        try {
            progressDialog.dismiss();
            throw error;
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }

    /**
     * Show progressbar indicating backgorund proccess
     */
    private void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setTitle(getString(R.string.wait));
        progressDialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == rentCancelButton.getId()){
            onClickCancelRent();
        }else if(view.getId() == rentConfirmButton.getId()){
            onClickRent();
        }else if(view.getId() == rentLayout.getId()){
            onClickCancelRent();
        }
    }

    private void onClickCancelRent() {
        try {
            placeSelected=null;

            Animation animation = new TranslateAnimation(0, 0,0, 500);
            animation.setDuration(250);
            animation.setFillAfter(true);
            rentLayout.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    rentLayout.setVisibility(View.GONE);
                    rentLayout.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } catch (Exception e) {
            AlertUtils.showError(this, e);
        }
    }
}
