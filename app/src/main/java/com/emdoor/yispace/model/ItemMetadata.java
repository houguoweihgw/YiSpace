package com.emdoor.yispace.model;
import java.util.Date;

public class ItemMetadata {
    private long id;
    private long userId;
    private String exposure_time;
    private double aperture;
    private int iso;
    private double focal_length;
    private double latitude;
    private double longitude;
    private double altitude;
    private String make;
    private String model;
    private String date_taken;
    private long file_size;
    private int image_width;
    private int image_length;
    private String scene_tags;

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
        return exposure_time;
    }

    public void setExposureTime(String exposureTime) {
        this.exposure_time = exposureTime;
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
        return focal_length;
    }

    public void setFocalLength(double focalLength) {
        this.focal_length = focalLength;
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

    public String getDateTaken() {
        return date_taken;
    }

    public void setDateTaken(String dateTaken) {
        this.date_taken = dateTaken;
    }

    public long getFileSize() {
        return file_size;
    }

    public void setFileSize(long fileSize) {
        this.file_size = fileSize;
    }

    public int getImageWidth() {
        return image_width;
    }

    public void setImageWidth(int imageWidth) {
        this.image_width = imageWidth;
    }

    public int getImageLength() {
        return image_length;
    }

    public void setImageLength(int imageLength) {
        this.image_length = imageLength;
    }

    public String getSceneTags() {
        return scene_tags;
    }

    public void setSceneTags(String sceneTags) {
        this.scene_tags = sceneTags;
    }

    @Override
    public String toString() {
        return "Image{" + "id=" + id + ", exposure_time=" +exposure_time + ", aperture=" + aperture+ ", iso=" +iso+ ", focal_length=" +focal_length + ", latitude=" + latitude + ", longitude=" + longitude + ", altitude=" + altitude + ", make=" + make + ", model=" + model + ", dateTaken=" + date_taken + ", fileSize=" + file_size + ", imageWidth=" + image_width + ", imageLength=" + image_length + ", sceneTags=" + scene_tags + '}';
    }
}
