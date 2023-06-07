package com.example.geolostfoundtask91;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class add_Item extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    EditText  input_Name,  input_Phone,  input_Description,  input_Date  ;
    Button buttonSaveNewItem, buttonHome, buttonGetCurrentLocation;
    TextView itemID, input_Location;
    RadioGroup postTypeRadioGroup;
    RadioButton rbtn_lost, rbtn_found;
    DatabaseHelper db;

    Geocoder geocoder;
    public String postType = "NotSet";
    private FusedLocationProviderClient fusedlocationClient;

    public String item_Latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), BuildConfig.API_KEY);}

        postTypeRadioGroup = (RadioGroup) findViewById(R.id.postTypeRadioGroup);
        rbtn_lost = (RadioButton) findViewById(R.id.rbtn_lost);
        rbtn_found = (RadioButton) findViewById(R.id.rbtn_found);
        input_Name = (EditText)  findViewById(R.id.input_Name);
        input_Phone = (EditText)  findViewById(R.id.input_Phone);
        input_Description = (EditText)  findViewById(R.id.input_Description);
        input_Date = (EditText)  findViewById(R.id.input_Date);
        input_Location = (TextView)  findViewById(R.id.input_Location);
        buttonSaveNewItem = (Button) findViewById(R.id.buttonSaveNewItem);
        buttonHome = (Button) findViewById(R.id.buttonHome);
        buttonGetCurrentLocation = (Button) findViewById(R.id.buttonGetCurrentLocation);
        db = new DatabaseHelper(this);
        fusedlocationClient = LocationServices.getFusedLocationProviderClient(this);


        // sets the item to either be Lost or Found
        postTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup postTypeRadioGroup, int checkedID) {
                switch(checkedID) {
                    case R.id.rbtn_lost:
                        postType = "Lost";
                        break;
                    case R.id.rbtn_found:
                        postType = "Found";
                        break;
                }
            }
        });

        // autocomplete function for the place
        input_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(getApplicationContext());
                startAutocomplete.launch(intent);

            }
        });

        buttonGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        // This method saves the user input to the database, it also calls the validation method to check for missing fields
        buttonSaveNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValidation()== true){
                    //Commit new entry to DB
                    String item_TypeTEXT = postType;
                    String item_NameTEXT = input_Name.getText().toString();
                    String item_PhoneTEXT = input_Phone.getText().toString();
                    String item_DescriptionTEXT = input_Description.getText().toString();
                    String item_DateTEXT = input_Date.getText().toString();
                    String item_LocationTEXT = input_Location.getText().toString();
                    String item_LatlngTEXT = item_Latlng;

                    Boolean checkInsertSuccess = db.insertData(item_TypeTEXT,item_NameTEXT, item_PhoneTEXT, item_DescriptionTEXT, item_DateTEXT, item_LocationTEXT, item_LatlngTEXT);
                    if(checkInsertSuccess){
                        Toast.makeText(add_Item.this, "Item saved!", Toast.LENGTH_LONG).show();
                        backToHome();
                    }
                    else {
                        Toast.makeText(add_Item.this, "FAILED", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    inputValidation();
                }

            }
        });


        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send user back to homepage
                backToHome();
            }
        });


        buttonGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gets the users current location and adds it to the location field
                getCurrentLocation();
            }
        });
    }

    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        Log.i("TAG", "Place: ${place.getName()}, ${place.getId()}");

                        // Get the location details including lat long for making the map markers
                        String getletlng = String.valueOf(place.getLatLng());
                        item_Latlng = StringUtils.substringBetween(getletlng, "(", ")");
                        input_Location.setText(place.getName());
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i("TAG", "User canceled autocomplete");
                }
            });



    // Takes user back to main page
    public void backToHome(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    // checks for empty fields before saving new database entry
    public Boolean inputValidation(){
        //Check each input field for values and warn if empty
        Boolean formComplete = false;
        if (postType.length() <1 ){
            Toast.makeText(add_Item.this, "Pick a post type", Toast.LENGTH_SHORT).show();
            formComplete = false;
        }
        if (input_Name.length() <1 ){
            Toast.makeText(add_Item.this, "Enter Name", Toast.LENGTH_SHORT).show();
            formComplete = false;
        }
        if (input_Phone.length() <1 ) {
            Toast.makeText(add_Item.this, "Enter Phone", Toast.LENGTH_SHORT).show();
            formComplete = false;
        }
        if (input_Description.length() <1 ) {
            Toast.makeText(add_Item.this, "Enter Description", Toast.LENGTH_SHORT).show();
            formComplete = false;
        }
        if (input_Date.length() <1 ) {
            Toast.makeText(add_Item.this, "Enter Date", Toast.LENGTH_SHORT).show();
            formComplete = false;
        }
        if (input_Location.length() <1 ) {
            Toast.makeText(add_Item.this, "Enter Location", Toast.LENGTH_SHORT).show();
            formComplete = false;
        }
        else if (postType != null && input_Name != null && input_Phone !=null && input_Description !=null && input_Date !=null && input_Location !=null ) {
            // Set number of sets to run through
            formComplete = true;
        }
        return formComplete;
    }
    Context context = this;

    // This method gets the current location of the user. I have set it to return the Local instead of a more granular address for UI space
    private void getCurrentLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            fusedlocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                        geocoder = new Geocoder(context, Locale.getDefault());
                        List<Address> addressess;
                        try {
                            addressess = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        addressess.get(0).getLocality();

                        // Get the location details including lat long for making the map markers
                        item_Latlng = addressess.get(0).getLatitude() + ", " + addressess.get(0).getLongitude();
                        input_Location.setText(addressess.get(0).getLocality());
                    }else{
                        Toast.makeText(add_Item.this, "Error getting location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // gets the permission for the location services
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }


}