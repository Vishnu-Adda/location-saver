package com.someapp.vishnu.mymemorableplaces;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static ArrayList<String> memorableList;
    static ArrayList<LatLng> latLngList;
    static ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.memorableListView);

        memorableList = new ArrayList<String>();
        latLngList = new ArrayList<LatLng>();

        memorableList.add("Add a new place...");

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, memorableList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                intent.putExtra("Place", memorableList.get(position));
                if(latLngList != null && latLngList.size() > 0) {
                    intent.putExtra("LatLng", latLngList.get(position-1));
                } else {

                    intent.putExtra("LatLng", new LatLng(10, 10));

                }

                startActivityForResult(intent, 1);

            }
        });

    }
}
