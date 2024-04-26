package com.example.android01;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import com.example.android01.common.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagSearchAdapter extends ArrayAdapter<Tag> implements Filterable {

    private List<Tag> allTags;
    private List<Tag> filteredTags;

    public TagSearchAdapter(@NonNull Context context, List<Tag> tags) {
        super(context, android.R.layout.simple_dropdown_item_1line, tags);
        this.allTags = tags;
        this.filteredTags = new ArrayList<>(tags);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Tag> filteredValues = new ArrayList<>();

                if (constraint != null) {
                    for (Tag tag : allTags) {
                        if (tag.getValue().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredValues.add(tag);
                        }
                    }
                }

                filterResults.values = filteredValues;
                filterResults.count = filteredValues.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredTags.clear();
                if (results.values != null) {
                    filteredTags.addAll((List<Tag>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getCount() {
        return filteredTags.size();
    }

    @Override
    public Tag getItem(int position) {
        return filteredTags.get(position);
    }
}
