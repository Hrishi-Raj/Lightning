package com.omen.acer.lightning;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.omen.acer.lightning.JSONTask;
import com.omen.acer.lightning.R;

public class MainActivity extends AppCompatActivity {

    private Double lat,lon;
    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    private FusedLocationProviderClient client;
    Intent myIntent2,myIntent3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        client = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission
                new AlertDialog.Builder(this).setTitle("Required Location Permission").setMessage("Necessary for this feature").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSIONS_REQUEST_READ_LOCATION);
                    }

                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            client.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                lat = location.getLatitude();
                                lon= location.getLongitude();
                                myIntent2 = new Intent(getBaseContext(),   Main2Activity.class);
                                myIntent3 = new Intent(getBaseContext(),   Main3Activity.class);
                                myIntent3.putExtra("lat",lat);
                                myIntent3.putExtra("lon",lon);
                                JSONTask task= new JSONTask(MainActivity.this);
                                task.execute("https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=72211993449697fef818ed78213ff2c1");

                            }
                        }
                    });
        }


    }

    public void onbuttonclick(View v){
        if(myIntent2.getExtras()!=null)
            startActivity(myIntent2);
    }
    public void lightning(View v){
        if(myIntent3.getExtras()!=null)
            startActivity(myIntent3);
    }

}
