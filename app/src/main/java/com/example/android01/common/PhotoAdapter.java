    package com.example.android01.common;

    import android.content.Context;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.net.Uri;
    import android.util.Log;
    import android.view.GestureDetector;
    import android.view.LayoutInflater;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.PopupMenu;
    import android.widget.Toast;

    import androidx.core.content.ContextCompat;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.android01.R;
    import com.example.android01.User;
    import com.example.android01.common.Photo;
    import com.google.android.material.floatingactionbutton.FloatingActionButton;

    import java.io.FileNotFoundException;
    import java.io.IOException;
    import java.io.InputStream;
    import java.util.ArrayList;


    public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

        private ArrayList<Photo> photosList;

        private FloatingActionButton deleteIcon;
        private FloatingActionButton moveIcon;
        private static final long DOUBLE_CLICK_TIME_THRESHOLD = 300; // in milliseconds
        private long lastClickTime = 0;

        private String albumName;

        public PhotoAdapter(ArrayList<Photo> photosList, FloatingActionButton deleteIcon, FloatingActionButton moveIcon, String albumName) {
            this.photosList = photosList;
            this.deleteIcon = deleteIcon;
            this.moveIcon = moveIcon;
            this.albumName = albumName;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Photo photo = photosList.get(position);
            Uri imageUri = Uri.parse(photo.getUri());  // Access URI from Photo object
            setImageFromUri(holder.imageView, imageUri);
            try {
                handlePhotoViewHolder(holder, position);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void setImageFromUri(ImageView imageView, Uri imageUri) {
            InputStream inputStream = null;
            try {
                inputStream = imageView.getContext().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("PhotoAdapter", "Image file not found", e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("PhotoAdapter", "Error closing image file stream", e);
                    }
                }
            }
        }

        private void handlePhotoViewHolder(ViewHolder holder, int position) throws IOException {
            Photo photo = photosList.get(position);
            holder.imageView.setOnClickListener(v -> {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_THRESHOLD) {
                    // Double click logic
//                    context.startActivity(PhotoActivity.newIntent(context, position, currAlbumPosition, currAlbum));
                    Log.d("double", "doubly wubbly");
                }
                else {
                    // Single click logic
                    Log.d("single", "single ladiessss");
                    deleteIcon.setVisibility(View.VISIBLE);
                    moveIcon.setVisibility(View.VISIBLE);
                    lastClickTime = System.currentTimeMillis();

                    deleteIcon.setOnClickListener(i -> deletePhoto(photo));
                    moveIcon.setOnClickListener(i -> onMoveClick(i, photo));
                }
            });
        }

        // Method to delete an album
        private void deletePhoto(Photo photoToDelete) {
            photosList.remove(photoToDelete);
            deleteIcon.setVisibility(View.GONE);
            moveIcon.setVisibility(View.GONE);
            notifyDataSetChanged();
        }

        public void onMoveClick(View view, Photo movePhoto) {
            showMovePhotoPopup(view, movePhoto);
            deleteIcon.setVisibility(View.GONE);
            moveIcon.setVisibility(View.GONE);
        }

        private void showMovePhotoPopup(View view, Photo movePhoto) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
//             Inflate menu with album names
            for (int i = 0; i < User.getInstance().getAlbums().size(); i++) {
                Album currentAlbum = User.getInstance().getAlbums().get(i);
                if(!currentAlbum.getName().equals(albumName)){
                    popupMenu.getMenu().add(0, i, i, currentAlbum.getName());
                }
            }

            // Set menu item click listener
            popupMenu.setOnMenuItemClickListener(item -> {
                // 'item.getItemId()' is the index of the selected album
                Album selectedAlbum = User.getInstance().getAlbums().get(item.getItemId());

                //check if the selected album has the photo
                if(User.getInstance().getAlbums().size()==1){
                    Toast.makeText(view.getContext(),"Cannot move photos because no other albums exist.",Toast.LENGTH_SHORT).show();
                } else if(!selectedAlbum.getPhotos().contains(movePhoto)){
                    photosList.remove(movePhoto);
                    selectedAlbum.addPhoto(movePhoto);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(view.getContext(),"This photo already exists in " + selectedAlbum.getName(),Toast.LENGTH_SHORT).show();
                }
                return true;
            });
            // Show the popup menu
            popupMenu.show();
        }

        @Override
        public int getItemCount() {
            return photosList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageViewPhoto);  // Ensure ID matches your layout
            }
        }
    }
