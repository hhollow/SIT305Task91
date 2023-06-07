package com.example.geolostfoundtask91;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> item_Id, item_Type , item_Name, item_Phone, item_Description, item_Date, item_Location;
    DatabaseHelper db;
    AdapterRecyclerView adapter;

    Button buttonHomeFromList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        db = new DatabaseHelper(this);

        buttonHomeFromList = (Button) findViewById(R.id.buttonHomeFromList);

        item_Id = new ArrayList<>();
        item_Type = new ArrayList<>();
        item_Name = new ArrayList<>();
        item_Phone = new ArrayList<>();
        item_Description = new ArrayList<>();
        item_Date = new ArrayList<>();
        item_Location = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerListItems);
        adapter = new AdapterRecyclerView(this, item_Id, item_Type , item_Name, item_Phone, item_Description, item_Date, item_Location);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();

        // sends user home
        buttonHomeFromList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send user back to homepage
                backToHome();
            }
        });

        // Sends the user to the item detail view and displays the data for the card selected
        adapter.setOnItemClickListener(new AdapterRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(ItemList.this, ItemDetailView.class);
                intent.putExtra("CardSelectedPosition", position);
                intent.putExtra("ID",String.valueOf(item_Id.get(position)));
                intent.putExtra("type",String.valueOf(item_Type.get(position)));
                intent.putExtra("name",String.valueOf(item_Name.get(position)));
                intent.putExtra("phone",String.valueOf(item_Phone.get(position)));
                intent.putExtra("description",String.valueOf(item_Description.get(position)));
                intent.putExtra("date",String.valueOf(item_Date.get(position)));
                intent.putExtra("location",String.valueOf(item_Location.get(position)));
                startActivity(intent);

            }
        });
    }

    // shows the data in the recycler
    private void displayData(){
        Cursor cursor = db.getdata();
        if(cursor.getCount() == 0)
        {
            Toast.makeText(ItemList.this, "No items to show", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                item_Id.add(cursor.getString(0));
                item_Type.add(cursor.getString(1));
                item_Name.add(cursor.getString(2));
                item_Phone.add(cursor.getString(3));
                item_Description.add(cursor.getString(4));
                item_Date.add(cursor.getString(5));
                item_Location.add(cursor.getString(6));
            }
        }

    }

    // Takes user back to main page
    public void backToHome(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}

