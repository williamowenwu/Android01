package com.example.android01.common;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {
    private String uri;
    private ArrayList<Tag> tags;

    public Photo(String uri) {
        this.uri = uri;
    }

    // Getters and setters for the properties
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "uri='" + uri + '\'' +
                '}';
    }
}
