package com.emdoor.yispace.request;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadPhotoRequest {
    private String username;
    private MultipartBody.Part file;

    public UploadPhotoRequest(String username, MultipartBody.Part file) {
        this.username = username;
        this.file = file;
    }

    public String getUsername() {
        return username;
    }

    public MultipartBody.Part getFile() {
        return file;
    }

    @Override
    public String toString() {
       return "UploadPhotoRequest{" +
                "username='" + username + '\'' +
                ", file=" + file +
                '}';
    }
}


