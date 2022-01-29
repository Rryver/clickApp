package com.example.guidetodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    //ArrayAdapter<String> itemsAdapter;
    ItemAdapter tvItemsAdapter;
    ListView lvItems;
    EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();

        ArrayList<TextView> tvItems = new ArrayList<>();
        TextView tvItem = new TextView(this);
        tvItem.setText("qweasdzxc");

        tvItems.add(tvItem);


        //itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        tvItemsAdapter = new ItemAdapter(this, items);
        etNewItem = (EditText)findViewById(R.id.etNewItem);

        lvItems.setAdapter(tvItemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemText = itemText.trim();
        if (itemText.isEmpty()) {
            Toast.makeText(this, "Empty string", Toast.LENGTH_SHORT).show();
            return;
        }
        //itemsAdapter.add(itemText);
        tvItemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        System.out.println(lvItems.getCount());
        System.out.println(tvItemsAdapter.getCount());
        lvItems.smoothScrollToPosition(tvItemsAdapter.getCount());
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        items.remove(position);
                        tvItemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(parent.getContext(), "Click", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(parent.getContext(), ClickActivity.class);
                        intent.putExtra("item", items.get(position));
                        startActivity(intent);
                    }
                }
        );
    }

    private void writeItems() {
        FileOutputStream fos = null;
        try {
            String itemsToSave = items.toString();

            fos = openFileOutput("todo.txt", MODE_PRIVATE);
            fos.write(itemsToSave.getBytes());
            Toast.makeText(this, "FIle was saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readItems() {
        FileInputStream fin = null;
        try {
            fin = openFileInput("todo.txt");
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String (bytes);
            text = text.substring(1, text.length() - 1);

            items.clear();
            items.addAll(Arrays.asList(text.split(", ")));
        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}