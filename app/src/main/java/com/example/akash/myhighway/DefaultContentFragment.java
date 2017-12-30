package com.example.akash.myhighway;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hdodenhof.circleimageview.CircleImageView;


public class DefaultContentFragment extends Fragment {
    Button b1,updateLocation,getPlaceButton;
    TextView t1,t2,prEmail,prName;
    CircleImageView prPhoto;
    Toolbar toolbar;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;
    GPSTracker myTracker;
    GeoDataClient geoDataClient;
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*t1=(TextView) getActivity().findViewById(R.id.longitudeText);
        t2=(TextView) getActivity().findViewById(R.id.latitudeText);
        updateLocation=(Button) getActivity().findViewById(R.id.getLocation);
        myTracker=new GPSTracker(getActivity());
         supportMapFragment=((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapFragment));
         supportMapFragment.getMapAsync(this);

        updateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myTracker.cangetLocation()){
                    myTracker.getLocation();
                    t1.setText(myTracker.getLongitude()+"");
                    t2.setText(myTracker.getLatitude()+"");
                    setLocationOnMap(myTracker.getLatitude(),myTracker.getLongitude());
                    onMapReady(googleMap);
                }
                else{
                    myTracker.showSettingAlert();
                }
            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_default_content, container, false);
    }
   /* @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if(myTracker.cangetLocation()){
            myTracker.getLocation();
        }
    }
    public void setLocationOnMap(double lat,double lon){
        LatLng current = new LatLng(lat,lon);
        this.googleMap.addMarker(new MarkerOptions().position(current).title("Current Place"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,9));
    }*/

}
