package com.example.geolostfoundtask91;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailView extends AppCompatActivity {

    Button buttonRemoveItem, buttonBackToList;
    TextView showItemType,showItemName,  showItemDescription, showItemPhone, showItemDate, showItemLocation;
    DatabaseHelper db;
    int CardSelected ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail_view);
        buttonRemoveItem = (Button) findViewById(R.id.buttonRemoveItem);
        buttonBackToList = (Button) findViewById(R.id.buttonBackToList);
        showItemType = (TextView) findViewById(R.id.showItemType);
        showItemName = (TextView) findViewById(R.id.showItemName);
        showItemDescription = (TextView) findViewById(R.id.showItemDescription);
        showItemPhone = (TextView) findViewById(R.id.showItemPhone);
        showItemDate = (TextView) findViewById(R.id.showItemDate);
        showItemLocation = (TextView) findViewById(R.id.showItemLocation);

        db = new DatabaseHelper(this);
        String ID = null, type = null, name = null, phone = null, description = null, date = null, location = null;

        // Gets the data from the previous activity
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            CardSelected = extras.getInt("CardSelectedPosition");
            ID = String.valueOf(extras.get("ID"));
            type = String.valueOf(extras.get("type"));
            name = String.valueOf(extras.get("name"));
            phone = String.valueOf(extras.get("phone"));
            description = String.valueOf(extras.get("description"));
            date = String.valueOf(extras.get("date"));
            location = String.valueOf(extras.get("location"));
            CardSelected = extras.getInt("CardSelectedPosition");

        }

        // sets the text view withs with the recived data
        showItemType.setText(type);
        showItemName.setText(name);
        showItemPhone.setText(phone);
        showItemDescription.setText(description);
        showItemDate.setText(date);
        showItemLocation.setText(location);

        buttonBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send user back to homepage
                buttonBackToList();
            }
        });

        // Deletes the card being viewed and returns the user to the item list
        String finalID = ID;
        buttonRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete the item currently being viewed and send user back to the item list
                db.deleteItem(String.valueOf(finalID));
                buttonBackToList();
            }
        });
    }

    // returns user to item list
    public void buttonBackToList(){
        Intent intent = new Intent(this,ItemList.class);
        startActivity(intent);
    }

}