package com.omen.acer.lightning;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omen.acer.lightning.R;

public class Main3Activity extends AppCompatActivity {

    TextView city,type,detail,chance;
    ImageView icon;
    Intent myIntent;
    Double lat,lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Bundle bundle= getIntent().getExtras();
        lat=bundle.getDouble("lat");
        lon=bundle.getDouble("lon");
        city =  findViewById(R.id.city_field);
        type = findViewById(R.id.type);
        detail = findViewById(R.id.details_field);
        icon = findViewById(R.id.icon);
        chance = findViewById(R.id.probability);

        setdata(bundle.getString("city"),bundle.getString("type"),bundle.getString("detail"),(Bitmap)bundle.get("bitmap"));
    }

    public void setdata(String place, String weather, String d, Bitmap bitmap) {

        icon.setImageBitmap(bitmap);
        city.setText(place);
        type.setText(weather);
        chance.setText("Show Zones on Map");
        detail.setText(d);
    }
    public void map(View view)
    {
        myIntent = new Intent(getBaseContext(),MapsActivity.class);
        myIntent.putExtra("lat",lat);
        myIntent.putExtra("lon",lon);
        startActivity(myIntent);
    }

}
