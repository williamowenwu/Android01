package com.example.android01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android01.common.Album;
import com.example.android01.common.PhotoAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import other.PhotoItem;


public class MainActivity extends AppCompatActivity {

    private RecyclerView album_recycler_view;
    private TextInputEditText album_textfield;
    private TextInputEditText currentlyClicked;
    private AlbumsAdapter albumsAdapter;

    //attempt to get photos from gallery
    private static final int REQUEST_IMAGE_GET = 1;
    private Button btnAddPhotos;
    private RecyclerView recyclerViewPhotos;
    private List<PhotoItem> selectedPhotosList;
    private PhotoAdapter photoAdapter;


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
//        AppData.saveToFile(context);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddPhotos = findViewById(R.id.btnAddPhotos);
        album_recycler_view = findViewById(R.id.album_recycler_view);
        album_textfield = findViewById(R.id.album_textfield);
        recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos); // Add this line

        album_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        albumsAdapter = new AlbumsAdapter(new ArrayList<>());
        album_recycler_view.setAdapter(albumsAdapter);

        album_textfield.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String albumName = album_textfield.getText().toString().trim();
                if (!albumName.isEmpty()) {
                    Album newAlbum = new Album(albumName);
                    albumsAdapter.addAlbum(newAlbum, this);
                    album_textfield.setText(""); // Clear the input field

                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(album_textfield.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
        //more open photos stuff
        selectedPhotosList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(selectedPhotosList);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPhotos.setAdapter(photoAdapter);

        btnAddPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle add photos button click
                openGallery();
            }
        });
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
            photoAdapter.notifyDataSetChanged();
        }
    }
}

