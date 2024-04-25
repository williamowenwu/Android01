package com.example.android01;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import com.example.android01.common.Album;
import com.example.android01.common.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private static final String FILENAME = "userdata.ser";
    private static User instance;
    private ArrayList<Album> albums;
    private ArrayList<Tag> tags;

    // Private constructor to prevent instantiation from outside this class
    private User() {
        albums = new ArrayList<>();
        tags = new ArrayList<>();
    }

    // Singleton pattern to get the instance of User
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public static void setInstance(User instance){
        User.instance = instance;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public Album getAlbum(String name){
        for(Album myAlbum: this.albums){
            if(myAlbum.getName().equalsIgnoreCase(name)){
                return myAlbum;
            }
        }
        return null;
    }

    public boolean addAlbum(Album album){
        for(Album existingAlbum: this.albums){
            if (existingAlbum.getName().equalsIgnoreCase(album.getName())) {
                return false;
            }
        }
        this.albums.add(album);
        return true;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    // Save the User instance to a file
    public static void saveToFile(Context context) {
        File directory = new File(context.getFilesDir(), "data");
        if (!directory.exists()) {
            directory.mkdirs();  // Create directory if it doesn't exist
        }
        File file = new File(directory, "userdata.ser");

        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(getInstance());
            oos.close();
            fos.close();
        } catch (IOException e) {
            Log.e("User", "Error during serialization", e);
        }
    }

    // Load the User instance from a file
    public static void loadFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            instance = (User) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            instance = getInstance();
        }
    }
}
