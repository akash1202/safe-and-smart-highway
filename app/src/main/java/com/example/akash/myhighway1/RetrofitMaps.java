package com.example.akash.myhighway1;

import com.example.akash.myhighway1.data.model.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vishal on 1/24/2018.
 */

public interface RetrofitMaps { //AIzaSyCxlCKaWSr9r7k9w1INI0kOSvSJTfZU9Ew
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDmvpiTTaow0D-BpyQeKGfdBeOTWvaWqWo")
        //AIzaSyDN7RJFmImYAca96elyZlE5s_fhX-MMuhk
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
}
