package com.example.akash.myhighway1;

import com.example.akash.myhighway1.data.model.Example;
import com.example.akash.myhighway1.data.model.Setting;
import com.example.akash.myhighway1.reset.RestResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

@Singleton
public interface Api {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDmvpiTTaow0D-BpyQeKGfdBeOTWvaWqWo")
//AIzaSyDN7RJFmImYAca96elyZlE5s_fhX-MMuhk
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

    /* @POST("login")
     Observable<RestResponse<LoginResponse>> login(@Body LoginRequest loginRequest);
 */
    @GET("synSetting")
    Observable<RestResponse<Setting>> synSetting(@Header("token") String token);

}
