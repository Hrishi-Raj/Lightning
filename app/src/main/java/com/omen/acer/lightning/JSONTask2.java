package com.omen.acer.lightning;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONTask2 extends AsyncTask<String,String,String> {


    private final MapsActivity activity;

    public JSONTask2(MapsActivity mapsActivity) {
        this.activity=mapsActivity;
    }

    @Override
    protected String doInBackground(String... urls) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url= new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line ;
            while((line= reader.readLine())!=null)
            {
                buffer.append(line);
            }
            String s=buffer.toString();

            return s;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (connection != null)
                connection.disconnect();
            try{
                if(reader!=null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        JSONObject jo;
        long green=0;
        double max=-95364.259320854908, min= -121785.42284043931;
        double gradient= (255/(max-min));
        double d=0.00009030667;
        try {
            jo = new JSONObject(s);
            JSONArray jsonArray=jo.getJSONArray("array");
            for(int i=0;i<jsonArray.length();i++)
            {

                JSONObject obj= new JSONObject();
                obj = (JSONObject) jsonArray.get(i);
                String type= obj.getString("type");
                if(type.equals("t"))
                    green=255;
                else
                {
                    green=  Math.round((obj.getDouble("prob")-min)*gradient);
                    if(green>255)
                        green=255;

                }
                int tone= (int) green;
                double lat=obj.getDouble("lat"),lon=obj.getDouble("lon");
                Polygon polygon = activity.mMap.addPolygon(new PolygonOptions()
                        .add(new LatLng(lat+d,lon+d), new LatLng(lat-d,lon+d), new LatLng(lat-d,lon-d), new LatLng(lat+d,lon-d))
                        .strokeColor(Color.rgb(255,tone,0))
                        .fillColor(Color.rgb(255,tone,0)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
