package com.example.android01.common;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android01.R;
import com.example.android01.User;

import java.util.ArrayList;

public class SlideTagAdapter extends RecyclerView.Adapter<SlideTagAdapter.TagViewHolder> {
    private Context context;
    private ArrayList<Tag> tags;

    public SlideTagAdapter(Context context, ArrayList<Tag> tags) {
        this.context = context;
        this.tags = tags;
    }

    public void setTags(ArrayList<Tag> tags){
        this.tags = tags;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        Tag tag = tags.get(position);
        holder.tagText.setText(tag.getType() + ": " + tag.getValue());
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class TagViewHolder extends RecyclerView.ViewHolder{
        TextView tagText;
        ImageView trashIcon;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagText = itemView.findViewById(R.id.tagTextView);

            tagText.setOnClickListener(v -> {
                // Toggle visibility of the trash icon
                trashIcon.setVisibility(trashIcon.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            });

            trashIcon = itemView.findViewById(R.id.trash_icon);
            trashIcon.setVisibility(View.GONE);

            trashIcon.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Tag tagToRemove = tags.get(position);
                    tags.remove(tagToRemove);  // Remove the tag from the data set
                    User.getInstance().getTags().remove(tagToRemove);
                    notifyItemRemoved(position);
                    Log.d("works?", "New amount: " + tags.size());
                    notifyItemRangeChanged(position, tags.size());  // Update the positions of remaining items
                    User.saveToFile(itemView.getContext());  // Save updated User data. This assumes you have a User class managing saving.
                }
            });
        }
    }
}
