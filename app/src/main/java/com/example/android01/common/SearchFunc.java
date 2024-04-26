package com.example.android01.common;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android01.PhotosActivity;
import com.example.android01.R;
import com.example.android01.TagSearchAdapter;
import com.example.android01.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SearchFunc {

    public static void setupSearchButton(AppCompatActivity activity) {
        View searchButton = activity.findViewById(R.id.searchButton);
        if (searchButton != null) {
            searchButton.setOnClickListener(view -> showSearchDialog(activity));
        }
    }

    public static void showSearchDialog(Context context) {
        // Initial setup of dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search, null);
        builder.setView(view);

        // Setup of fields
        AutoCompleteTextView firstTag = view.findViewById(R.id.tag1);
        Spinner spinnerOperator = view.findViewById(R.id.spinnerOperator);
        spinnerOperator.setVisibility(View.GONE);
        AutoCompleteTextView secondTag = view.findViewById(R.id.tag2);
        secondTag.setVisibility(View.GONE);

        // Setup Spinner adapters
        List<String> operatorOptions = Arrays.asList("None", "AND", "OR");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, operatorOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperator.setAdapter(adapter);

        // Setup AutoComplete adapters
        List<Tag> allTags = User.getInstance().getTags();
        TagSearchAdapter tagValueAdapter = new TagSearchAdapter(context, allTags);
        firstTag.setAdapter(tagValueAdapter);
        secondTag.setAdapter(tagValueAdapter);

        // Setup of ItemClickListeners for auto complete
        firstTag.setOnItemClickListener((parent, view1, position, id) -> {
            Tag selectedTag = tagValueAdapter.getItem(position);
            firstTag.setTag(selectedTag);
            assert selectedTag != null;
            firstTag.setText(selectedTag.toString());
            spinnerOperator.setVisibility(View.VISIBLE);
        });
        secondTag.setOnItemClickListener((parent, view1, position, id) -> {
            Tag selectedTag = tagValueAdapter.getItem(position);
            secondTag.setTag(selectedTag);
            secondTag.setText(selectedTag.toString());
        });
        spinnerOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Enable or disable autoCompleteTag2 based on the selected Spinner item
                String selectedOperator = spinnerOperator.getSelectedItem().toString();
                secondTag.setVisibility((!selectedOperator.equalsIgnoreCase("None"))?View.VISIBLE:View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Enable or disable autoCompleteTag2 based on the selected Spinner item
                String selectedOperator = spinnerOperator.getSelectedItem().toString();
                secondTag.setVisibility((!selectedOperator.equalsIgnoreCase("None"))?View.VISIBLE:View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        //Setup of OnEditListeners for auto complete
        firstTag.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Every time an input is placed in check if it matches a toString of existing tag if does return that tag
                // Also update the visibility of the spinner
                String enteredText = firstTag.getText().toString().trim();
                Optional<Tag> typedTag = User.getInstance().getTags().stream()
                        .filter(tag -> enteredText.equals(tag.toString())).findFirst();
                if(typedTag.isPresent()){
                    firstTag.setTag(typedTag.get());
                    spinnerOperator.setVisibility(View.VISIBLE);
                }else{
                    firstTag.setTag(null);
                    spinnerOperator.setVisibility(View.GONE);
                }
                return true;
            }
            return false;
        });
        secondTag.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Every time an input is placed in check if it matches a toString of existing tag if does return that tag
                String enteredText = secondTag.getText().toString().trim();
                Optional<Tag> typedTag = User.getInstance().getTags().stream()
                        .filter(tag -> enteredText.equals(tag.toString())).findFirst();
                if(typedTag.isPresent()){
                    secondTag.setTag(typedTag.get());
                }else{
                    secondTag.setTag(null);
                }
                return true;
            }
            return false;
        });

        builder.setPositiveButton("Search", (dialog, which) -> {
            // Retrieve selected tags and operator
            Tag tag1param = (Tag) firstTag.getTag();
            String operator = spinnerOperator.getSelectedItem().toString();
            Tag tag2param = (Tag) secondTag.getTag();

            // search via the tagFilter method
            boolean illegalTag1 = (tag1param == null);
            boolean illegalTag2 = !operator.equals("None") && (tag2param == null);
            if(!illegalTag1 && !illegalTag2){
                // Search for both
                Intent searchIntent = new Intent(context, PhotosActivity.class);
                searchIntent.putExtra("TAG1", tag1param);
                searchIntent.putExtra("OPERATOR", operator);
                searchIntent.putExtra("TAG2",tag2param);
                User.saveToFile(context);

                context.startActivity(searchIntent);
            }else{
                if(illegalTag1){
                    Toast.makeText(context, "Error: Non-valid tag1", Toast.LENGTH_LONG).show();}
                else if (illegalTag2){Toast.makeText(context, "Error: Non-valid tag2", Toast.LENGTH_LONG).show();}
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
