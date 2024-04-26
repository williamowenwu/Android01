package com.example.android01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android01.common.Album;
import com.example.android01.common.SearchFunc;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private RecyclerView albumRecyclerView;
    private TextInputEditText albumTextField;
    private AlbumsAdapter albumsAdapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User.loadFromFile(this);

        SearchFunc.setupSearchButton(this);

        albumRecyclerView = findViewById(R.id.album_recycler_view);
        albumTextField = findViewById(R.id.album_textfield);

        albumRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        albumsAdapter = new AlbumsAdapter();  // Use the albums from the user data
        albumRecyclerView.setAdapter(albumsAdapter);

        albumTextField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String albumName = albumTextField.getText().toString().trim();
                if (!albumName.isEmpty()) {
                    Album newAlbum = new Album(albumName);
                    albumsAdapter.addAlbum(newAlbum, this);
                    albumsAdapter.notifyDataSetChanged();  // Notify the adapter of the data change
                    albumTextField.setText(""); // Clear the input field

                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(albumTextField.getWindowToken(), 0);

                    User.saveToFile(this);  // Save the updated user data to file
                }
                return true;
            }
            return false;
        });
    }
}
