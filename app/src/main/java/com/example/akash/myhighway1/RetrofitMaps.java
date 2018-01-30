package com.example.akash.myhighway1;

import com.example.akash.myhighway1.POJO.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vishal on 1/24/2018.
 */

public interface RetrofitMaps {
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyB5SsrjH6pxsHMj3CNN4J5qN1gRsMUyw8g")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
}
