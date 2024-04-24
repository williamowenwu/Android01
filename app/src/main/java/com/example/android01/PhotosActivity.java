package com.example.android01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;

public class PhotosActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // Get the album name from the intent
        String albumName = getIntent().getStringExtra("albumName");

        // Set the album name to the TextView
        TextView albumTitleTextView = findViewById(R.id.albumTitleTextView);
        albumTitleTextView.setText(albumName);

        FloatingActionButton addPhotoButton = findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // Use this URI to work with the selected image
        }
    }

    public void onAlbumClicked(String selectedAlbumName) {
        Intent intent = new Intent(this, PhotosActivity.class);
        intent.putExtra("albumName", selectedAlbumName);  // Passing the selected album name to the PhotosActivity
        startActivity(intent);
    }

    public static class PhotoItem implements Serializable {
        private String imagePath;

        public PhotoItem(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }
    }
}


