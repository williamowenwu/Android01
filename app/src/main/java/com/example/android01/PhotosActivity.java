package com.example.android01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android01.common.PhotoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import other.PhotoItem;

public class PhotosActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GET = 1;

    //attempt to get photos from gallery
    private Button btnAddPhotos;
    private RecyclerView recyclerViewPhotos;
    private List<PhotoItem> selectedPhotosList;
    private PhotoAdapter photoAdapter;//attempt to get photos from gallery

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // Get the album name from the intent
        String albumName = getIntent().getStringExtra("albumName");

        // Set the album name to the TextView
        TextView albumTitleTextView = findViewById(R.id.albumTitleTextView);
        albumTitleTextView.setText(albumName);

      /*  Button addPhotoButton = findViewById(R.id.btnAddPhotos);
        addPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });
*/
        //back button
        ImageButton previousButton = findViewById(R.id.returnButton);
        previousButton.setOnClickListener(v -> finish());

        //add photos button - T
        btnAddPhotos = findViewById(R.id.btnAddPhotos);
        recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos);

        selectedPhotosList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(selectedPhotosList);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPhotos.setAdapter(photoAdapter);

        btnAddPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle add photos button click
                Log.d("PhotosActivity", "Gallery selection successful"); // Log gallery selection
                openGallery();
            }
        });
    }
    public void onAlbumClicked(String selectedAlbumName) {
        Intent intent = new Intent(this, PhotosActivity.class);
        intent.putExtra("albumName", selectedAlbumName);  // Passing the selected album name to the PhotosActivity
        startActivity(intent);
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK && data != null) {
            Log.d("PhotosActivity", "Gallery selection successful"); // Log gallery selection
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedPhotosList.add(new PhotoItem(imageUri.toString()));
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                selectedPhotosList.add(new PhotoItem(imageUri.toString()));
            }
            Log.d("PhotosActivity", "Number of selected photos: " + selectedPhotosList.size()); // Log photo count
            photoAdapter.notifyDataSetChanged();
            for (PhotoItem photoItem : selectedPhotosList) {
                Log.d("ImagePath", "Path: " + photoItem.getImagePath());
            }
        }
    }


}


