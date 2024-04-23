package com.example.android01;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private List<String> albumNames;

    public AlbumsAdapter(List<String> albumNames) {
        this.albumNames = albumNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.albumNameTextView.setText(albumNames.get(position));
    }

    @Override
    public int getItemCount() {
        return albumNames.size();
    }

    public void addAlbum(String albumName) {
        albumNames.add(albumName);
        notifyItemInserted(albumNames.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView albumNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumNameTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
