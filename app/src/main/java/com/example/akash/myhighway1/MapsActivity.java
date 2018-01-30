package com.example.akash.myhighway1;

import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.akash.myhighway1.POJO.Example;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private GoogleMap mMap;
    public GPSTracker myTracker;
    private int PROXIMITY_RADIUS=1000;
    GeoDataClient geoDataClient=null;
    GoogleApiClient mGoogleApiClient=null;
    FloatingActionButton floatingActionButton;
    //SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myTracker=new GPSTracker(MapsActivity.this);
        //swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //swipeRefreshLayout.setOnRefreshListener(this);
        floatingActionButton=(FloatingActionButton) findViewById(R.id.floatingRefereshButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(myTracker.cangetLocation()){
            myTracker.getLocation();
            setLocationOnMap(myTracker.getLatitude(),myTracker.getLongitude());
        }
        else{
            myTracker.showSettingAlert();
        }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(myTracker.cangetLocation()){
            myTracker.getLocation();
            setLocationOnMap(myTracker.getLatitude(),myTracker.getLongitude());
        }
        else{
            myTracker.showSettingAlert();
        }*/
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(myTracker.getLatitude(), myTracker.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    public void setLocationOnMap(double lat,double lon){
        LatLng current = new LatLng(lat,lon);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings uiSettings=mMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setZoomControlsEnabled(true);
        CameraPosition cameraPosition= new CameraPosition.Builder()
                .target(current)
                .zoom(15)
                .bearing(90)
                .tilt(60)
                .build();
        Marker marker=this.mMap.addMarker(new MarkerOptions().position(current).title("Current Place"));
        marker.showInfoWindow();
        this.mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        build_retrofit_and_get_response("car_repair");
    }
    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);
        Call<Example> call = service.getNearbyPlaces(type, myTracker.getLatitude() + "," + myTracker.getLongitude(), PROXIMITY_RADIUS);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                //GoogleMap mMap = mMap;
                //mMap.clear();
                // This loop will go through all the results and add marker on each location.
                try {
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                        String placeName = response.body().getResults().get(i).getName();
                        String vicinity = response.body().getResults().get(i).getVicinity();
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(lat, lng);
                        // Position of Marker on Map
                        markerOptions.position(latLng);
                        // Adding Title to the Marker
                        markerOptions.title(placeName);
                        // Adding Marker to the Camera.
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        Marker m = mMap.addMarker(markerOptions);
                        // Adding colour to the marker
                        // move map camera
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                    }
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
                catch (Exception e){
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

   /* @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                if(myTracker.cangetLocation()){
                    myTracker.getLocation();
                    setLocationOnMap(myTracker.getLatitude(),myTracker.getLongitude());
                }
                else{
                    myTracker.showSettingAlert();
                }
            }
        },2000);
    }*/
}
