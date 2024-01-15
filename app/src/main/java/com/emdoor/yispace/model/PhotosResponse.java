package com.emdoor.yispace.model;

import java.util.List;

public class PhotosResponse {
    private List<Photo> photos;
    private String message;

    // 构造方法

    public PhotosResponse(List<Photo> photos, String message) {
        this.photos = photos;
        this.message = message;
    }

    // Getter and Setter 方法

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
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
                "photos=" + photos.get(0).toString() +
                ", message='" + message + '\'' +
                '}';
    }
}
