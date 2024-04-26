package com.example.android01.common;

import java.io.Serializable;
import java.util.Objects;

public class Tag implements Serializable {
    private String type; // "Person" or "Location"
    private String value;

    // Constructor with parameters
    public Tag(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    // Setter for value
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(type, tag.type) &&
                Objects.equals(value, tag.value);
    }
}