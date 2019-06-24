package com.omen.acer.lightning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

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


public class JSONTask extends AsyncTask<String,String,String> {

    private final MainActivity activity;
    private String weather="",place;
    private Bitmap bitmap;
    private double temp,wind_speed,dewPoint,cloud_height,wind_angle,humidity,pressure;

    public JSONTask(MainActivity mainActivity) {
        this.activity = mainActivity;
    }

    @Override
    protected String doInBackground(String... urls)
    {

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
            JSONObject jo;
            jo = new JSONObject(s);
            place = (String) jo.get("name");
            JSONArray w = (JSONArray) jo.get("weather");

            JSONObject j = (JSONObject) w.get(0);
            weather =  j.getString("main");
            String ic= j.getString("icon");
            JSONObject m = jo.getJSONObject("main");
            temp = (double)Math.round((m.getDouble("temp") - 273.15)*100)/100;
            pressure = (double)Math.round(m.getDouble("pressure"));
            humidity = m.getDouble("humidity");
            JSONObject wind = jo.getJSONObject("wind");
            wind_speed = wind.getDouble("speed");
            wind_angle = wind.getDouble("deg");
            final double a = 6.1121, b = 18.678, c = 257.14, d = 234.5;
            double magnus = Math.log(humidity / 100) + (b - temp / d) * temp / (c + temp);
            dewPoint = c * magnus / (b - magnus);
            dewPoint = (double)Math.round(dewPoint*100)/100;
            cloud_height = Math.abs(dewPoint - temp) * 121.92;
            cloud_height = (double)Math.round(cloud_height*100)/100;
            InputStream st= new java.net.URL("http://openweathermap.org/img/w/"+ ic +".png").openStream();
            bitmap= BitmapFactory.decodeStream(st);
            return s;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
        String detail="Pressure: "+pressure+"hPa\nHumidity: "+humidity+"%\nWind:"+wind_speed+"m/s";
        activity.myIntent2.putExtra("city",place);
        activity.myIntent2.putExtra("weather",weather);
        activity.myIntent2.putExtra("temp",temp);
        activity.myIntent2.putExtra("detail",detail);
        activity.myIntent2.putExtra("bitmap",bitmap);
        activity.myIntent3.putExtra("city",place);
        activity.myIntent3.putExtra("type",weather);
        detail="DewPoint: "+dewPoint+"'C\nCloud Height: "+cloud_height+"m";
        activity.myIntent3.putExtra("detail",detail);
        activity.myIntent3.putExtra("bitmap",bitmap);
    }

}
