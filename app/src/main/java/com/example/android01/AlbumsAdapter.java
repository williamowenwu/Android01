package com.example.android01;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.android01.common.Album;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private User user = User.getInstance();  // Use the User singleton directly.

    public AlbumsAdapter() {
        super();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = user.getAlbums().get(position);
        holder.albumNameTextView.setText(album.getName());
    }

    @Override
    public int getItemCount() {
        return user.getAlbums().size();
    }

    public void addAlbum(Album album, Context context) {
        if (user.addAlbum(album)) {  // Add album through User's method to ensure uniqueness.
            notifyItemInserted(user.getAlbums().size() - 1);
            User.saveToFile(context);
        } else {
            Toast.makeText(context, "Album name already exists :(", Toast.LENGTH_SHORT).show();
        }
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

            gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    // Action on double tap, e.g., open album details.
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Album album = user.getAlbums().get(position);
                        Intent intent = new Intent(itemView.getContext(), PhotosActivity.class);
                        intent.putExtra("albumName", album.getName());
                        itemView.getContext().startActivity(intent);
                    }
                    return true;
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    // Toggle the visibility of the trash icon.
                    trashIcon.setVisibility(trashIcon.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                    return true;
                }
            });

            itemView.setOnTouchListener(this);

            trashIcon.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    user.getAlbums().remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, user.getAlbums().size());
                    User.saveToFile(itemView.getContext());  // Save updated User data.
                }
            });

            albumNameTextView.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Finalize editing of album name
                    String newName = albumNameTextView.getText().toString().trim();
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && !newName.isEmpty() && !containsAlbumName(newName, position)) {
                        Album album = user.getAlbums().get(position);
                        album.setName(newName);
                        notifyItemChanged(position);
                        User.saveToFile(itemView.getContext());  // Save changes
                    } else {
                        Toast.makeText(itemView.getContext(), "Invalid name or name already exists.", Toast.LENGTH_SHORT).show();
                    }
                    InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            });
        }

        private boolean containsAlbumName(String newName, int currentIndex) {
            for (int i = 0; i < user.getAlbums().size(); i++) {
                if (i != currentIndex && user.getAlbums().get(i).getName().equalsIgnoreCase(newName)) {
                    return true;
                }
            }
            return false;
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
            return true;
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            return false;
        }

        // Additional gesture methods as placeholders
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

