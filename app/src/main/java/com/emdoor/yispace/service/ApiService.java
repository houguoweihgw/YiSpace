// 示例：放置在名为"network"的包下
package com.emdoor.yispace.service;


import com.emdoor.yispace.request.DeletePhotoRequest;
import com.emdoor.yispace.response.FaceResponse;
import com.emdoor.yispace.response.LoginResponse;
import com.emdoor.yispace.response.PhotosResponse;
import com.emdoor.yispace.response.RecycledPhotosResponse;
import com.emdoor.yispace.response.Response;
import com.emdoor.yispace.response.SceneResponse;
import com.emdoor.yispace.response.TotalPhotosCountResponse;
import com.emdoor.yispace.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {
    // 登陆
    @POST("/api/login")
    Call<LoginResponse> login(@Body User loginRequest);

    // 查询所有照片
    @GET("/home/photos")
    Call<PhotosResponse> photos(@Query("username") String username, @Query("page") int page,@Query("perPage") int perPage);

    // 查询所有照片数量
    @GET("/home/totalPhotosCount")
    Call<TotalPhotosCountResponse> totalPhotosCount(@Query("username") String username);

    // 查询收藏照片
    @GET("/home/myCollected")
    Call<PhotosResponse> collectedPhotos(@Query("username") String username);

    // 查询回收站照片
    @GET("/home/recycledPhotos")
    Call<RecycledPhotosResponse> recycledPhotos(@Query("username") String username);

    // 查询所有场景分类
    @GET("/home/sceneLabels")
    Call<SceneResponse> sceneLabels(@Query("username") String username);

    // 查询某个场景分类照片数量
    @GET("/home/labelPhotoCount")
    Call<TotalPhotosCountResponse> labelPhotoCount(@Query("username") String username,@Query("label") String label);

    // 查询某个场景分类照片
    @GET("/home/labelPhotos")
    Call<PhotosResponse> labelPhotos(@Query("username") String username, @Query("page") int page,@Query("perPage") int perPage,@Query("label") String label);

    // 查询所有人脸聚类
    @GET("/home/faceClusters")
    Call<FaceResponse> faceClusters(@Query("username") String username);

    // 查询某个人脸聚类照片数量
    @GET("/home/clusterPhotoCount")
    Call<TotalPhotosCountResponse> clusterPhotoCount(@Query("username") String username,@Query("cluster") String cluster);

    // 查询某个人脸聚类照片
    @GET("/home/clusterPhotos")
    Call<PhotosResponse> clusterPhotos(@Query("username") String username, @Query("page") int page,@Query("perPage") int perPage,@Query("cluster") String cluster);

    // 更改某张照片收藏状态
    @PUT("/home/toggleCollected")
    Call<Response> toggleCollected(@Query("username") String username, @Query("photo") String photo);

    // 批量删除某些照片
    @POST("/home/deleteSelectedPhotos")
    Call<Response> deleteSelectedPhotos(@Body DeletePhotoRequest photos);
}
