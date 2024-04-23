package com.example.android01;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // Get the album name from the intent
        String albumName = getIntent().getStringExtra("albumName");

        // Set the album name to the TextView
        TextView albumTitleTextView = findViewById(R.id.albumTitleTextView);
        albumTitleTextView.setText(albumName);

        // Other setup code...
    }

    public void onAlbumClicked(String selectedAlbumName) {
        Intent intent = new Intent(this, PhotosActivity.class);
        intent.putExtra("albumName", selectedAlbumName);  // Passing the selected album name to the PhotosActivity
        startActivity(intent);
    }

}


