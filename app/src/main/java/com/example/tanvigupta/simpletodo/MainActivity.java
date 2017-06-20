package com.example.tanvigupta.simpletodo;

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
