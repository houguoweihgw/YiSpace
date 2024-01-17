package com.emdoor.yispace.model;

import java.io.Serializable;

public class Face implements Serializable {
    private String face_name;
    private int face_count;
    private String face_cover;

    //getter and setter
    public String getFace_name() {
        return face_name;
    }

    public void setFace_name(String face_name) {
        this.face_name = face_name;
    }

    public int getFace_count() {
        return face_count;
    }

    public void setFace_count(int face_count) {
        this.face_count = face_count;
    }

    public String getFace_cover() {
        return face_cover;
    }

    public void setFace_cover(String face_cover) {
        this.face_cover = face_cover;
    }
    @Override
    public String toString() {
        return "Face{" +
                "face_name='" + face_name + '\'' +
                ", face_count=" + face_count +
                ", face_cover='" + face_cover + '\'' +
                '}';
    }
}