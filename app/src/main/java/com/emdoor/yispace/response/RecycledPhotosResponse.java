package com.emdoor.yispace.response;

import com.emdoor.yispace.model.Photo;

import java.util.List;

public class RecycledPhotosResponse {
    private List<Photo> recycledPhotos;
    private String message;

    // 构造方法

    public RecycledPhotosResponse(List<Photo> recycledPhotos, String message) {
        this.recycledPhotos = recycledPhotos;
        this.message = message;
    }

    // Getter and Setter 方法

    public List<Photo> getPhotos() {
        return recycledPhotos;
    }

    public void setPhotos(List<Photo> photos) {
        this.recycledPhotos = photos;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // 重写 toString 方法

    @Override
    public String toString() {
        return "PhotosResponse{" +
                "photos=" + recycledPhotos.get(0).toString() +
                ", message='" + message + '\'' +
                '}';
    }
}
