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
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView album_recycler_view;
    private TextInputEditText album_textfield;
    private TextInputEditText currentlyClicked;
    private AlbumsAdapter albumsAdapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
//        AppData.saveToFile(context);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        album_recycler_view = findViewById(R.id.album_recycler_view);
        album_textfield = findViewById(R.id.album_textfield);

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
    }
}