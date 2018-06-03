package com.someapp.vishnu.mymemorableplaces;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();

        String selectedText = intent.getStringExtra("Place");

        Bundle bundle = intent.getExtras();

        LatLng selectedLatLng = (LatLng) bundle.get("LatLng");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm yyyy-MM-dd");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, locationListener);

        } else {

            // This code will ask the user for permission aka if permission wasn't granted
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {

                // This generates the popup to allow access to the location
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {

                        Geocoder geocoder = new Geocoder(getApplicationContext(),
                                Locale.getDefault());

                        try {
                            List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude,
                                    latLng.longitude, 1);

                            String address = "";

                            if (listAddresses != null && listAddresses.size() > 0) {

                                // Address number
                                if (listAddresses.get(0).getSubThoroughfare() != null) {

                                    address += listAddresses.get(0).getSubThoroughfare() + " ";

                                } else {

                                    address += simpleDateFormat.format(new Date()) + " ";

                                }

                                // Street name
                                if (listAddresses.get(0).getThoroughfare() != null) {

                                    address += listAddresses.get(0).getThoroughfare() + " ";

                                }

                                // City name
                                if (listAddresses.get(0).getLocality() != null) {

                                    address += listAddresses.get(0).getLocality();

                                }

                            }

                            mMap.addMarker(new
                                    MarkerOptions().position(latLng).title(address));

                            MainActivity.memorableList.add(address);
                            MainActivity.latLngList.add(latLng);

                            MainActivity.arrayAdapter.notifyDataSetChanged();

                            Toast.makeText(getApplicationContext(), "Location Saved!",
                                    Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                if(selectedText.equals("Add a new place...")) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            0, 0, locationListener);

                    Location lastKnownLocation =
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(),
                            lastKnownLocation.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

                    try {
                        List<Address> listAddresses =
                                geocoder.getFromLocation(lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude(), 1);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));

                        String address = "";

                        if (listAddresses != null && listAddresses.size() > 0) {

                            // Address number
                            if (listAddresses.get(0).getSubThoroughfare() != null) {

                                address += listAddresses.get(0).getSubThoroughfare() + " ";

                            }

                            // Street name
                            if (listAddresses.get(0).getThoroughfare() != null) {

                                address += listAddresses.get(0).getThoroughfare() + " ";

                            }

                            // City name
                            if (listAddresses.get(0).getLocality() != null) {

                                address += listAddresses.get(0).getLocality();

                            }

                        }

                        mMap.addMarker(new
                                MarkerOptions().position(userLocation).title(address));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 10));

                    mMap.addMarker(new
                            MarkerOptions().position(selectedLatLng).title(selectedText));

                }

            }
        }
    }
}
