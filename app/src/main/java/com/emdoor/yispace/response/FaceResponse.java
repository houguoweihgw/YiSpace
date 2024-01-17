package com.emdoor.yispace.response;

import com.emdoor.yispace.model.Face;

import java.util.List;

public class FaceResponse {
    private List<Face> faces;
    private String message;

    // 构造函数、getter和setter方法
    public List<Face> getFaces() {
        return faces;
    }

    public void setFaces(List<Face> faces) {
        this.faces = faces;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return "FaceResponse [faces=" + faces.get(0).toString() + ", message=" + message + "]";
    }
}
