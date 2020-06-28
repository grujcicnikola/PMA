package com.example.pawfinder.service;

import com.example.pawfinder.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("user/register")
    Call<ResponseBody> register(@Body User user);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("user/login")
    Call<ResponseBody> login(@Body User user);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT(ServiceUtils.PUT_TOKEN)
    Call<ResponseBody> token(@Body User user);

    @PUT(ServiceUtils.CHANGE_PASSWORD)
    Call<ResponseBody> changePassword(@Body User user);

}
