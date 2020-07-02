package com.example.pawfinder.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtils {

    //10.0.2.2. za emulator
    public static final String SERVICE_API_PATH = "http://adresa:8080/";
   // public static final String SERVICE_API_PATH = "https://pmaheroku.herokuapp.com/";


    public static final String GET_ALL_PETS = "pet/getAll";
    public static final String IMAGES_URL = SERVICE_API_PATH + "/images/";
    public static final String POST_MISSING = "pet/postMissing";
    public static final String GET_ALL_IN_RANGE = "pet/getAllInRange";
    public static final String GET_MISSING = "pet/getMissing";
    public static final String ADD_COMMENT = "comment/add";
    public static final String GET_ALL_COMMENTS_BY_PET = "comment/getAllByPet/{petId}";
    public static final String DELETE_COMMENT ="comment/delete/{id}" ;
    public static final String DELETE_REPORT = "pet/deleteReport/{id}/{email}";
    public static final String PET_FOUND = "pet/petFound/{id}";
    public static final String GET_PETS_BY_OWNER = "pet/getByOwner/{email}";
    public static final String PET_IMAGE_UPLOAD= "pet/uploadPhoto";

    //users
    public static final String GET_ALL_USERS = "user/getAll";
    public static final String CHANGE_PASSWORD = "user/changePassword";
    public static final String PUT_TOKEN = "user/token";
    public static final String USER_LOGIN = "user/login";
    public static final String USER_GOOGLE_LOGIN = "user/googleLogin";
    public static final String USER_REGISTER = "user/register";

    public static OkHttpClient test() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();

    /*
     * Definisemo konkretnu instancu servisa na intnerntu sa kojim
     * vrsimo komunikaciju
     * */
    public static PetService petService = retrofit.create(PetService.class);

    public static UserService userService = retrofit.create(UserService.class);

    public static CommentService commentService = retrofit.create(CommentService.class);
}

