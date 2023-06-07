package com.example.geolostfoundtask91;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapAll extends AppCompatActivity implements OnMapReadyCallback {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_all);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = new DatabaseHelper(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        db.getdata().getColumnName(7);
        Cursor cursor = db.getdata();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLngBounds bounds = null;
        if (cursor.getCount() == 0) {
            Toast.makeText(MapAll.this, "No items to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {

                //  Get the string value of the lat long field from the database and convert it to coordinates to get markers onto map
                String[] latLng = cursor.getString(7).split(",");
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);

                LatLng locationToMark = new LatLng(latitude, longitude);

                // add the markers to the map
                googleMap.addMarker(new MarkerOptions().position(locationToMark));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationToMark));

                // record all the markers in the builder to set the view and zoom of the map to fit all the points
                builder.include(locationToMark);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        }
    }
}

