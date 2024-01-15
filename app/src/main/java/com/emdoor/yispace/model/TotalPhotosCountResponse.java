package com.emdoor.yispace.model;

public class TotalPhotosCountResponse {
    private int total;
    private String message;

    // 构造函数、getter和setter方法
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TotalPhotosCountResponse{" +
                "total=" + total +
                ", message='" + message + '\'' +
                '}';
    }
}
