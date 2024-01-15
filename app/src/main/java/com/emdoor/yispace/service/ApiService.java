// 示例：放置在名为"network"的包下
package com.emdoor.yispace.service;


import com.emdoor.yispace.model.LoginResponse;
import com.emdoor.yispace.model.PhotosResponse;
import com.emdoor.yispace.model.TotalPhotosCountResponse;
import com.emdoor.yispace.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {
    // 登陆
    @POST("/api/login")
    Call<LoginResponse> login(@Body User loginRequest);

    @GET("/home/photos")
    Call<PhotosResponse> photos(@Query("username") String username, @Query("page") int page,@Query("perPage") int perPage);

    @GET("/home/totalPhotosCount")
    Call<TotalPhotosCountResponse> totalPhotosCount(@Query("username") String username);

}
