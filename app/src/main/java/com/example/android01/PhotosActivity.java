package com.example.android01;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android01.common.Album;
import com.example.android01.common.Photo;
import com.example.android01.common.PhotoAdapter;
import com.example.android01.common.SearchFunc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PhotosActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPhotos;
    private PhotoAdapter photoAdapter;
    private ArrayList<Photo> selectedPhotosList;

    private String albumName;

    User user = User.getInstance();
    private ActivityResultLauncher<Intent> selectImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        SearchFunc.setupSearchButton(this);

        setupViews();
        setupRecyclerView();
        setupActivityResultLauncher();
    }

    private void setupViews() {
        TextView albumTitleTextView = findViewById(R.id.albumTitleTextView);

        String parsedAlbumName = getIntent().getStringExtra("albumName");
        albumTitleTextView.setText(getIntent().getStringExtra("albumName"));
        if(parsedAlbumName.equalsIgnoreCase("")){
            Album resAlbum = (Album) getIntent().getSerializableExtra("resultPhotos");
            this.selectedPhotosList = resAlbum.getPhotos();
        } else {
            Album album = User.getInstance().getAlbum(albumTitleTextView.getText().toString()); // from the serialized user
            this.selectedPhotosList = album.getPhotos();
        }

//        System.out.println("album size " + album.getPhotos().size());
        this.albumName = getIntent().getStringExtra("albumName");
        ImageButton previousButton = findViewById(R.id.returnButton);
        previousButton.setOnClickListener(v -> {
            User.getInstance().saveToFile(this);
            finish();
        });

        Button btnAddPhotos = findViewById(R.id.btnAddPhotos);
        btnAddPhotos.setOnClickListener(v -> {
            Log.d("PhotosActivity", "Add Photos button clicked");
            openGallery();
        });

    }

    private void setupRecyclerView() {
        recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos);
        photoAdapter = new PhotoAdapter(selectedPhotosList, findViewById(R.id.fabDelete), findViewById(R.id.fabMove), albumName);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPhotos.setAdapter(photoAdapter);
    }

    private void setupActivityResultLauncher() {
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri;
                        if (result.getData().getClipData() != null) {
                            // Handling multiple selections
                            int count = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                uri = result.getData().getClipData().getItemAt(i).getUri();
                                processSelectedImage(uri);
                            }
                        } else if (result.getData().getData() != null) {
                            // Handling single selection
                            uri = result.getData().getData();
                            processSelectedImage(uri);
                        }
                        photoAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("PhotosActivity", "No data received or operation cancelled");
                    }
                }
        );
    }

    private void processSelectedImage(Uri uri) {
        this.getContentResolver().takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        );
        for (Photo existingPhoto : selectedPhotosList) {
            if (existingPhoto.getUri().equals(uri.toString())) {
                Toast.makeText(this, "Error: Photo already exists in album", Toast.LENGTH_LONG).show();
                return; // This prevents adding the photo if it already exists.
            }
        }
        selectedPhotosList.add(new Photo(uri.toString())); // Add new photo if it doesn't already exist.
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        selectImageLauncher.launch(intent);
    }

}