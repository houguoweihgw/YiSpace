package com.emdoor.yispace.model;

public class Scene {
    private String label_name;
    private int label_count;
    private String label_cover;

    //getter and setter
    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {

        this.label_name = label_name;
    }

    public int getLabel_count() {
        return label_count;
    }

    public void setLabel_count(int label_count) {
        this.label_count = label_count;
    }

    public String getLabel_cover() {
        return label_cover;
    }

    public void setLabel_cover(String label_cover) {
        this.label_cover = label_cover;
    }
    @Override
    public String toString() {
        return "Scene{" +
                "label_name='" + label_name + '\'' +
                ", label_count=" + label_count +
                ", label_cover='" + label_cover + '\'' +
                '}';
    }
}