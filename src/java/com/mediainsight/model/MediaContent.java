package com.mediainsight.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class MediaContent {

    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("type")
    private String type; // video, image, audio, text, document

    @JsonProperty("file_path")
    private String filePath; // Шлях до файлу

    @JsonProperty("user_id")
    private Integer userId; // ID користувача-автора

    // Конструктори
    public MediaContent() {}

    public MediaContent(String title, String type, String filePath, Integer userId) {
        this.title = title;
        this.type = type;
        this.filePath = filePath;
        this.userId = userId;
    }

    // Getters та Setters
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MediaContent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", filePath='" + filePath + '\'' +
                ", userId=" + userId +
                '}';
    }
}