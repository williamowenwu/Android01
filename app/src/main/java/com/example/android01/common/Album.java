package com.example.android01.common;

// Album.java
import java.util.ArrayList;
import java.util.List;

public class Album {
    private String name;
    private List<String> photos;

    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void addPhoto(String photo) {
        photos.add(photo);
    }
}


