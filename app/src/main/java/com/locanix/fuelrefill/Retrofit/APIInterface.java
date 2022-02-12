package com.locanix.fuelrefill.Retrofit;

import com.google.gson.JsonObject;
import com.locanix.fuelrefill.FuelrefillAddModel;
import com.locanix.fuelrefill.Model.CarDriverModel;
import com.locanix.fuelrefill.Model.EnterFuelRefilleModel;
import com.locanix.fuelrefill.Model.GetfuelEntry.GetFuelEntryModel;
import com.locanix.fuelrefill.Model.ScanModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("vehicles")
    Call<ScanModel> getVehicleResponse(@Header("token") String token, @Header("Content-Type") String content_type);

    @Multipart
    @POST("fuel/upload/file?photoType=CARDRIVER")
    Call<CarDriverModel> uploadCar(@Header("token") String token, @Part MultipartBody.Part file);

    @Multipart
    @POST("fuel/upload/file?photoType=BEFOREREFILL")
    Call<CarDriverModel> uploadBeforeRefill(@Header("token") String token, @Part MultipartBody.Part file);

    @Multipart
    @POST("fuel/upload/file?photoType= AFTERREFILL")
    Call<CarDriverModel> uploadAfterRefill(@Header("token") String token, @Part MultipartBody.Part file);

    @POST("fuel/add/entry")
    Call<FuelrefillAddModel> uploadRefillData(@Header("token") String token, @Header("Content-Type") String content_type, @Header("Accept") String accept, @Body JsonObject object);


    @GET("fuel/entry")
    Call<GetFuelEntryModel> getfuelentryresponse(@Header("token") String token, @Query("vehicleId") String vehicleId, @Query("entryId") String entryId);


}
