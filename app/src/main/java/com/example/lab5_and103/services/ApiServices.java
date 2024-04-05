package com.example.lab5_and103.services;

import com.example.lab5_and103.model.Distributor;
import com.example.lab5_and103.model.Fruit;
import com.example.lab5_and103.model.Page;
import com.example.lab5_and103.model.Response;
import com.example.lab5_and103.model.User;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiServices {
    public static String IP ="172.16.53.169";
    public static String BASE_URL="http://"+IP+":3000/api/";

    @GET("get-list-distributor")
    Call<Response<List<Distributor>>> getListDistributor();

    @GET("search-distributors")
    Call<Response<List<Distributor>>> getSearchDistributors(@Query("key") String key);

    @DELETE("delete-distributors/{id}")
    Call<Response<Distributor>> getDeleteDistributor(@Path("id") String id);

    @POST("add-distributor")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);
    @PUT("update-distributors/{id}")
    Call<Response<Distributor>> getUpdateDistributor(@Path("id") String id, @Body Distributor distributor);
    @Multipart
    @POST("register-send-email")
    Call<Response<User>> register(@Part("username") RequestBody username,
                                  @Part("password") RequestBody password,
                                  @Part("email") RequestBody email,
                                  @Part("name") RequestBody name,
                                  @Part MultipartBody.Part avatar);
    @POST("login")
    Call<Response<User>> login(@Body User user);
    @GET("get-list-fruit")
    Call<Response<ArrayList<Fruit>>> getListFruit(@Header("Authorization") String token);

    @Multipart
    @POST("add-fruit-with-file-image")
    Call<Response<Fruit>> addFruitWithImage(@PartMap Map<String,RequestBody> requestBodyMap, @Part ArrayList<MultipartBody.Part> lsImage);

    @GET("get-page-fruit")
    Call<Response<Page<ArrayList<Fruit>>>> getPageFruit(@QueryMap Map<String, String> stringMap);

    @Multipart
    @PUT("update-fruit-by-id/{id}")
    Call<Response<Fruit>> updateFruitWithFileImage(@PartMap Map<String, RequestBody> requestBodyMap,
                                                   @Path("id") String id,
                                                   @Part ArrayList<MultipartBody.Part> ds_hinh
    );

    @DELETE("destroy-fruit-by-id/{id}")
    Call<Response<Fruit>> deleteFruits(@Path("id") String id);

    @GET("get-fruit-by-id/{id}")
    Call<Response<Fruit>> getFruitById (@Path("id") String id);
}
