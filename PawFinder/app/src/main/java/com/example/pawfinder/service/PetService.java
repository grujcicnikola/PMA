package com.example.pawfinder.service;

import com.example.pawfinder.model.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface PetService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(ServiceUtils.GET_ALL_PETS)
    Call<List<Pet>> getAll();
}
