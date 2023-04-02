package com.example.android_lab_7;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView textViewStatus;
    private TextView textViewLat;
    private TextView textViewLon;
    private TextView textViewAddress;
    private TextView textViewTime;
    private LocationManager locationManager;
    private ListView locationsList;
    private DBHandler dbHandler;
    private Cursor cursor;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textViewStatus = findViewById(R.id.status_text);
        textViewLat = findViewById(R.id.lat_text);
        textViewLon = findViewById(R.id.lon_text);
        textViewAddress = findViewById(R.id.address_text);
        textViewTime = findViewById(R.id.time_text);

        locationsList = findViewById(R.id.locations_list);

        Button btnGetLocation = findViewById(R.id.get_location_btn);
        Button btnClearLocation = findViewById(R.id.clear_location_btn);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        textViewStatus.setText("disabled");

        btnGetLocation.setOnClickListener(this::getLocation);
        btnClearLocation.setOnClickListener(view -> {
            locationManager.removeUpdates(this);
            textViewStatus.setText("disabled");
        });

        dbHandler = new DBHandler(this);
    }

    public void getLocation(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            retrieveLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    @SuppressLint("MissingPermission")
    private void retrieveLocation() {
        textViewStatus.setText("enabled");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);

                String latValue = String.format(Locale.getDefault(), "%.4f", lat);
                String lonValue = String.format(Locale.getDefault(), "%.4f", lon);
                String addressValue = addressList.get(0).getAddressLine(0);
                String timeValue = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(location.getTime());

                textViewLat.setText(latValue);
                textViewLon.setText(lonValue);
                textViewAddress.setText(addressValue);
                textViewTime.setText(timeValue);

                dbHandler.addNewLocation(
                        lat,
                        lon,
                        timeValue,
                        addressValue
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            retrieveLocation();
        } else {
            textViewStatus.setText("Permission Denied");
        }
    }

    private void updateListData() {
        cursor = dbHandler.readLocations();

        String[] headers = new String[]{DBHandler.ID_COL, DBHandler.LATITUDE_COL, DBHandler.LONGITUDE_COL, DBHandler.TIME_COL, DBHandler.ADDRESS_COL};

        ListAdapter locationsAdapter = new SimpleCursorAdapter(
                MainActivity.this,
                R.layout.location_row,
                cursor,
                headers,
                new int[]{R.id._id, R.id.latitude, R.id.longitude, R.id.time, R.id.address},
                0
        );
        locationsList.setAdapter(locationsAdapter);
        Toast.makeText(this, "New location was added.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        retrieveLocation();
        updateListData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandler.close();
        cursor.close();
    }
}