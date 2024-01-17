package com.emdoor.yispace.response;

import com.emdoor.yispace.model.Scene;

import java.util.List;

public class SceneResponse {
    private List<Scene> labels;
    private String message;

    // 构造函数、getter和setter方法
    public List<Scene> getLabels() {
        return labels;
    }

    public void setLabels(List<Scene> labels) {
        this.labels = labels;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return "LabelResponse [scenes=" + labels.get(0).toString() + ", message=" + message + "]";
    }
}
