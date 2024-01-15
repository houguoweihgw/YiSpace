package com.emdoor.yispace.model;

import java.io.Serializable;

public class Photo implements Serializable {
    private int id;
    private String title;
    private String description;
    private String upload_date;
    private String file_content;
    private boolean collected;
    private ItemMetadata metadata;

    // 构造方法、getter和setter方法

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadDate() {
        return upload_date;
    }

    public void setUploadDate(String uploadDate) {
        this.upload_date = uploadDate;
    }

    public String getFileContent() {
        return file_content;
    }

    public void setFileContent(String fileContent) {
        this.file_content = fileContent;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public ItemMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ItemMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", upload_date='" + upload_date + '\'' +
                ", file_content='" + file_content + '\'' +
                ", collected=" + collected +
                ", metadata=" + metadata.toString() +
                '}';
    }
}

