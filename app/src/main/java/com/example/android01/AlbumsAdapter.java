package com.example.android01;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android01.common.Album;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private List<Album> albums;

    public AlbumsAdapter(List<Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albums.get(position);
        holder.albumNameTextView.setText(album.getName());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void addAlbum(Album album, Context context) {
        for (Album existingAlbum : albums) {
            if (existingAlbum.getName().equalsIgnoreCase(album.getName())) {
                Toast.makeText(context, "Album name already exists.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        albums.add(album);
        notifyItemInserted(albums.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnDoubleTapListener {
        TextInputEditText albumNameTextView;
        ImageView trashIcon;
        GestureDetector gestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumNameTextView = itemView.findViewById(R.id.textAlbumItemName);
            trashIcon = itemView.findViewById(R.id.trash_icon);
            trashIcon.setVisibility(View.GONE);

            // Initialize GestureDetector with SimpleOnGestureListener
            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Album album = albums.get(position);
                        Intent intent = new Intent(itemView.getContext(), PhotosActivity.class);
                        intent.putExtra("albumName", album.getName());
                        itemView.getContext().startActivity(intent);
                    }
                    return true;
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    // Toggle the visibility of the trash icon when an item is clicked
                    if (trashIcon.getVisibility() == View.GONE) {
                        trashIcon.setVisibility(View.VISIBLE);
                    } else {
                        trashIcon.setVisibility(View.GONE);
                    }
                    return true;
                }
            });

            itemView.setOnTouchListener(this);

            itemView.setOnClickListener(v -> {
                // Toggle the visibility of the trash icon when an item is clicked
                if (trashIcon.getVisibility() == View.GONE) {
                    trashIcon.setVisibility(View.VISIBLE);
                } else {
                    trashIcon.setVisibility(View.GONE);
                }
            });

            trashIcon.setOnClickListener(v -> {
                // Delete the album and notify the adapter
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    albums.remove(position);
                    notifyItemRemoved(position);
                }
            });

            albumNameTextView.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String newName = albumNameTextView.getText().toString().trim();
                    if (!newName.isEmpty() && !containsAlbumName(newName)) {
                        Album album = albums.get(getAdapterPosition());
                        album.setName(newName);
                        notifyItemChanged(getAdapterPosition());
                    } else {
                        Toast.makeText(itemView.getContext(), "Album name already exists or is empty.", Toast.LENGTH_SHORT).show();
                    }
                    InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            });
        }

        private boolean containsAlbumName(String newName) {
            for (Album album : albums) {
                if (album.getName().equalsIgnoreCase(newName)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Album album = albums.get(position);
                Intent intent = new Intent(itemView.getContext(), PhotosActivity.class);
                intent.putExtra("albumName", album.getName());
                itemView.getContext().startActivity(intent);
            }
            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // Toggle the visibility of the trash icon on single tap
            return true;
        }

//        Just to satisfy the interface below
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        public void onShowPress(MotionEvent e) {}

        public boolean onDown(MotionEvent e) {
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        public void onLongPress(MotionEvent e) {}

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

}

