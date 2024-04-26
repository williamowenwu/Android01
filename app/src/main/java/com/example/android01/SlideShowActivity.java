package com.example.android01;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.android01.common.SlideTagAdapter;
import com.example.android01.common.Tag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {
    private RecyclerView slideRecyclerView;

    private ImageView currImageView;

    private FloatingActionButton nextButton;

    private FloatingActionButton previousButton;

    private FloatingActionButton addTagsButton;

    private int photoPosition; // position within the list of photos in an album

    private Photo currPhoto;

    private Album currAlbum;

    private ArrayList<Photo> photoList;

    SlideTagAdapter tagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_show);

        this.photoPosition = getIntent().getIntExtra("position", 0);
        String albumName = getIntent().getStringExtra("albumName");

        this.currAlbum = User.getInstance().getAlbum(albumName);
        this.currPhoto = currAlbum.getPhotos().get(photoPosition);
        this.photoList = currAlbum.getPhotos();

        if (currPhoto == null) {
            Toast.makeText(this, "Error: Photo not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Previous button
        ImageButton returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            User.getInstance().saveToFile(this);
            finish();
        });

        // initializes UI
        slideRecyclerView = findViewById(R.id.tagRecyclerView);
        slideRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.tagAdapter = new SlideTagAdapter(this, currPhoto.getTags());
        slideRecyclerView.setAdapter(tagAdapter);

        TextView photoTextTitleView = findViewById(R.id.photoTextTitleView);
        photoTextTitleView.setText(currPhoto.getUri());
        currImageView = findViewById(R.id.slideImageView);
        Uri imageUri = Uri.parse(currPhoto.getUri());

        // Sets the image view from the Photo adapter (I know, this is a huge mess)
        PhotoAdapter.setImageFromUri(currImageView, imageUri);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            if(photoPosition >= photoList.size() - 1){
                Toast.makeText(this, "This is the last photo in the album", Toast.LENGTH_SHORT).show();
            }
            else {
                photoPosition++;
                this.currPhoto = photoList.get(photoPosition);
                PhotoAdapter.setImageFromUri(currImageView, Uri.parse(currPhoto.getUri()));
                tagAdapter.setTags(currPhoto.getTags());
                tagAdapter.notifyDataSetChanged();
            }
        });

        previousButton = findViewById(R.id.previousButton);
        previousButton.setOnClickListener(v -> {
            if(photoPosition <= 0){
                Toast.makeText(this, "This is the first photo in the album", Toast.LENGTH_SHORT).show();
            }
            else {
                photoPosition--;
                this.currPhoto = photoList.get(photoPosition);
                PhotoAdapter.setImageFromUri(currImageView, Uri.parse(currPhoto.getUri()));
                tagAdapter.setTags(currPhoto.getTags());
                tagAdapter.notifyDataSetChanged();
            }
        });

        addTagsButton = findViewById(R.id.addTagButton);
        addTagsButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_tag, null);
            builder.setView(dialogView);

            Spinner spinnerTagType = dialogView.findViewById(R.id.spinnerTagType);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, new String[]{"Person", "Location"});
            spinnerTagType.setAdapter(adapter);

            EditText editTextTagValue = dialogView.findViewById(R.id.editTextTagValue);

            builder.setPositiveButton("Add", (dialog, which) -> {
                String tagType = spinnerTagType.getSelectedItem().toString();
                String tagValue = editTextTagValue.getText().toString();

                Tag newTag = new Tag(tagType, tagValue);
                if(!currPhoto.addTag(newTag)){
                    Toast.makeText(this, "Error: Tag already exists on this photo", Toast.LENGTH_SHORT).show();

                } else {
                    // on successful add of a new tag
                    Log.d("SlideTagAdapter", "Adding tag: " + newTag.toString());
                    tagAdapter.notifyItemInserted(currPhoto.getTags().size() - 1);

                    User.saveToFile(this);
                    Log.d("SlideTagAdapter", "Tag added. Total tags now: " + currPhoto.getTags().size());
                }

            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}
