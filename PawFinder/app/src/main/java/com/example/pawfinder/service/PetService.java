package com.example.pawfinder.service;

import com.example.pawfinder.model.Pet;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PetService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(ServiceUtils.GET_ALL_PETS)
    Call<List<Pet>> getAll();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("pet/getByOwner/{email}")
    Call<List<Pet>> getPetsByUser(@Path("email") String email);

    @GET(ServiceUtils.GET_MISSING)
    Call<List<Pet>> getMissing();

    @GET(ServiceUtils.GET_ALL_IN_RANGE + "/{lon}/{lat}")
    Call<List<Pet>> getAllInRange(@Path("lon") Double lon, @Path("lat")Double lat);

    @POST(ServiceUtils.POST_MISSING)
    Call<Pet> postMissing(@Body Pet pet);
}
