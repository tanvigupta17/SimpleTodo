package com.example.tanvigupta.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.example.tanvigupta.simpletodo.MainActivity.ITEM_POSITION;
import static com.example.tanvigupta.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    // track edit text being used
    EditText etItemText;
    // track position of item in list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Resolve edit text from layout
        etItemText = (EditText) findViewById(R.id.etItemText);

        // Set edit value from intent
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));

        // Update position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION, 0);

        // Update title bar of activity
        getSupportActionBar().setTitle("Edit Item");
    }

    // Handler for save button
    public void onSaveItem(View v) {
        // prepare new Intent for result
        Intent in = new Intent();

        // pass updated item description to intent
        in.putExtra(ITEM_TEXT, etItemText.getText().toString());

        // pass old item position to intent
        in.putExtra(ITEM_POSITION, position);

        // set intent as result of the activity
        setResult(RESULT_OK, in);

        // navigate to previous page
        finish();
    }
}
