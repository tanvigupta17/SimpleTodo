package com.example.tanvigupta.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Declare constants for this class
    // code to identify edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    // keys to transfer data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    // Declare fields for this class
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize class variables
        // items = new ArrayList<>();
        readItems(); // get previously saved data

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        // Resolve list view
        lvItems = (ListView) findViewById(R.id.lvItems);

        // Wire list items to adapter
        lvItems.setAdapter(itemsAdapter);

        // Populate with mock data
        // items.add("First item");
        // items.add("Second item");

        // Invoke remove functionality
        setupListViewListener();
    }

    // Handler to allow users to add new items to the list
    public void onAddItem(View v){
        // Resolve edit text
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);

        // Extract value of text as string
        String itemText = etNewItem.getText().toString();

        // Add new item to list via items adapter
        itemsAdapter.add(itemText);

        etNewItem.setText("");

        // Update data file
        writeItems();

        // Display confirmation
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    // Allow users to remove items from list
    // Method is not invoked unless explicitly called
    private void setupListViewListener(){
        Log.i("MainActivity", "Setting up listener on list view"); // called when app is created

        // Set up item listener for long click (remove)
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("MainActivity", "Item removed from list: " + i);

                items.remove(i);
                itemsAdapter.notifyDataSetChanged();

                // Update data file
                writeItems();

                // acknowledge long click
                return true;
            }
        });

        // Set up item listener for regular click (edit)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // create the new activity
                Intent in = new Intent(MainActivity.this, EditItemActivity.class);
                // pass data being edited
                in.putExtra(ITEM_TEXT, items.get(i));
                in.putExtra(ITEM_POSITION, i);
                // display the activity
                startActivityForResult(in, EDIT_REQUEST_CODE);
            }
        });
    }

    // Handle results from edit activity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if edit activity completed
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // extract update item text from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);

            // extract original position of updated item
            int position = data.getExtras().getInt(ITEM_POSITION);

            // update model with new item text at edited position
            items.set(position, updatedItem);

            // notify adapter of changes
            itemsAdapter.notifyDataSetChanged();

            // persist the changed model
            writeItems();

            // notify user of successful completion
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    // Set up new file
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    // Read data from file
    private void readItems() {
        // initialize items array using data pulled from file
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
        }
    }

    // Write data to file
    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing to file", e);
        }
    }
}
