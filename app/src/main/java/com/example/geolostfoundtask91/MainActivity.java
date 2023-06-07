package com.example.geolostfoundtask91;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonCreateNewItem, buttonViewList, buttonViewOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCreateNewItem = (Button) findViewById(R.id.buttonCreateNewItem);
        buttonViewList = (Button) findViewById(R.id.buttonViewList);
        buttonViewOnMap = (Button) findViewById(R.id.buttonViewOnMap);


        buttonCreateNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddItem();
            }
        });
        buttonViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemList();
            }
        });
        buttonViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapAllItems();
            }
        });
    }

    // Opens the add item screen
    public void openAddItem() {
        Intent intent = new Intent(this, add_Item.class);
        startActivity(intent);
    }

    // Opens the List of items
    public void openItemList() {
        Intent intent = new Intent(this, ItemList.class);
        startActivity(intent);
    }
    // Opens map view of all items
    public void openMapAllItems() {
        Intent intent = new Intent(this, MapAll.class);
        startActivity(intent);
    }


}