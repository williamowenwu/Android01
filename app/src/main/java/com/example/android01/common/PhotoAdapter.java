package com.example.android01.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android01.R;
import com.example.android01.common.Photo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private ArrayList<Photo> photosList;

    public PhotoAdapter(ArrayList<Photo> photosList) {
        this.photosList = photosList;
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
