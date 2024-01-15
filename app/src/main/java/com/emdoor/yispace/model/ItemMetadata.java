package com.emdoor.yispace.model;
import java.util.Date;

public class ItemMetadata {
    private long id;
    private long userId;
    private String exposureTime;
    private double aperture;
    private int iso;
    private double focalLength;
    private double latitude;
    private double longitude;
    private double altitude;
    private String make;
    private String model;
    private Date dateTaken;
    private long fileSize;
    private int imageWidth;
    private int imageLength;
    private String sceneTags;

    // 构造方法、getter和setter方法

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(String exposureTime) {
        this.exposureTime = exposureTime;
    }

    public double getAperture() {
        return aperture;
    }

    public void setAperture(double aperture) {
        this.aperture = aperture;
    }

    public int getIso() {
        return iso;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }

    public double getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(double focalLength) {
        this.focalLength = focalLength;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageLength() {
        return imageLength;
    }

    public void setImageLength(int imageLength) {
        this.imageLength = imageLength;
    }

    public String getSceneTags() {
        return sceneTags;
    }

    public void setSceneTags(String sceneTags) {
        this.sceneTags = sceneTags;
    }

    @Override
    public String toString() {
        return "Image{" + "id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + ", altitude=" + altitude + ", make=" + make + ", model=" + model + ", dateTaken=" + dateTaken + ", fileSize=" + fileSize + ", imageWidth=" + imageWidth + ", imageLength=" + imageLength + ", sceneTags=" + sceneTags + '}';
    }
}
