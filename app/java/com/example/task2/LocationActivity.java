package com.example.task2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker selectedLocationMarker;
    private LatLng selectedLocation;

    private TextView locationInfoText;
    private Button saveLocationButton;
    private FloatingActionButton myLocationFab;

    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initializeViews();
        initializeServices();
        setupMapFragment();
        setupClickListeners();
    }

    private void initializeViews() {
        locationInfoText = findViewById(R.id.location_info_text);
        saveLocationButton = findViewById(R.id.save_location_button);
        myLocationFab = findViewById(R.id.my_location_fab);

        // Initially disable save button
        saveLocationButton.setEnabled(false);
    }

    private void initializeServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());
    }

    private void setupMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setupClickListeners() {
        saveLocationButton.setOnClickListener(v -> saveSelectedLocation());

        myLocationFab.setOnClickListener(v -> getCurrentLocation());

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set default location (you can change this to your preferred default)
        LatLng defaultLocation = new LatLng(37.7749, -122.4194); // San Francisco
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));

        // Enable map controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false); // We'll use our custom button

        // Set map click listener
        mMap.setOnMapClickListener(this::onMapClick);

        // Check for location permission and enable my location
        checkLocationPermission();
    }

    private void onMapClick(LatLng latLng) {
        // Remove previous marker
        if (selectedLocationMarker != null) {
            selectedLocationMarker.remove();
        }

        // Add new marker
        selectedLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Selected Location"));

        selectedLocation = latLng;

        // Get address for the selected location
        getAddressFromLocation(latLng);

        // Enable save button
        saveLocationButton.setEnabled(true);
    }

    private void getAddressFromLocation(LatLng latLng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = formatAddress(address);
                locationInfoText.setText(addressText);
            } else {
                locationInfoText.setText(String.format(Locale.getDefault(),
                        "Lat: %.6f, Lng: %.6f", latLng.latitude, latLng.longitude));
            }
        } catch (IOException e) {
            e.printStackTrace();
            locationInfoText.setText(String.format(Locale.getDefault(),
                    "Lat: %.6f, Lng: %.6f", latLng.latitude, latLng.longitude));
        }
    }

    private String formatAddress(Address address) {
        StringBuilder addressText = new StringBuilder();

        if (address.getFeatureName() != null) {
            addressText.append(address.getFeatureName()).append(", ");
        }
        if (address.getThoroughfare() != null) {
            addressText.append(address.getThoroughfare()).append(", ");
        }
        if (address.getLocality() != null) {
            addressText.append(address.getLocality()).append(", ");
        }
        if (address.getAdminArea() != null) {
            addressText.append(address.getAdminArea()).append(", ");
        }
        if (address.getCountryName() != null) {
            addressText.append(address.getCountryName());
        }

        return addressText.toString().replaceAll(", $", "");
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        onMapClick(currentLocation); // Automatically select current location
                    } else {
                        Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveSelectedLocation() {
        if (selectedLocation != null) {
            // Save location to SharedPreferences or Firebase
            saveLocationToPreferences();

            // Return result to ProfileActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("latitude", selectedLocation.latitude);
            resultIntent.putExtra("longitude", selectedLocation.longitude);
            resultIntent.putExtra("address", locationInfoText.getText().toString());
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "Location saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveLocationToPreferences() {
        getSharedPreferences("user_location", MODE_PRIVATE)
                .edit()
                .putFloat("latitude", (float) selectedLocation.latitude)
                .putFloat("longitude", (float) selectedLocation.longitude)
                .putString("address", locationInfoText.getText().toString())
                .putLong("saved_at", System.currentTimeMillis())
                .apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}