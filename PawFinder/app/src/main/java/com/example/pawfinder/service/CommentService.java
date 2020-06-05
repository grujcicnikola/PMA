package com.example.pawfinder.service;

import com.example.pawfinder.model.Comment;
import com.example.pawfinder.model.Pet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentService {


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("comment/getAllByPet/{petId}")
    Call<List<Comment>> getCommentsByPetsId(@Path("petId") Long petId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST(ServiceUtils.ADD_COMMENT)
    Call<Comment> addComment(@Body Comment comment);
}