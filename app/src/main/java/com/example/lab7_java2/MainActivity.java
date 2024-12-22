package com.example.lab7_java2;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tempView;
    private TextView condView;

    private Button ekbBtn;
    private Button spbBtn;
    private Button mscBtn;

    public static int cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tempView = (TextView) findViewById(R.id.tempView);
        condView = (TextView) findViewById(R.id.conditionView);

        ekbBtn = (Button) findViewById(R.id.ekatBtn);
        spbBtn = (Button) findViewById(R.id.spbBtn);
        mscBtn = (Button) findViewById(R.id.mscBtn);

        ekbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpHandler handler = new OkHttpHandler();
                handler.execute();
                cityId = 1;
            }
        });

        spbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpHandler handler = new OkHttpHandler();
                handler.execute();
                cityId = 2;
            }
        });

        mscBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpHandler handler = new OkHttpHandler();
                handler.execute();
                cityId = 3;
            }
        });
    }

    public class OkHttpHandler extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Void ... params){
            Request.Builder builder = new Request.Builder();

            String url = "";
            if(cityId == 1){
                url = "https://api.openweathermap.org/data/2.5/weather?lat=57&lon=60&appid=f54ec9466f747bf8de6de83719fc2047&lang=ru&units=metric";
            }
            else if (cityId == 2) {
                url = "https://api.openweathermap.org/data/2.5/weather?lat=59&lon=30&appid=f54ec9466f747bf8de6de83719fc2047&lang=ru&units=metric";
            }
            else {
                url = "https://api.openweathermap.org/data/2.5/weather?lat=56&lon=38&appid=f54ec9466f747bf8de6de83719fc2047&lang=ru&units=metric";
            }

            Request request = builder.url(url)
                    .build();

            OkHttpClient client = new OkHttpClient();

            try {
                Response response = client.newCall(request).execute();
                JSONObject object = new JSONObject(response.body().string());
                String stringTemp = object.getJSONObject("main").getString("temp");
                JSONArray weatherArr = object.getJSONArray("weather");
                String mainCondition = weatherArr.getJSONObject(0).getString("description");
                String conditionIcon = weatherArr.getJSONObject(0).getString("icon");

                ArrayList<String> result = new ArrayList<String>();
                result.add(stringTemp);
                result.add(mainCondition);
                result.add(conditionIcon);
                return result;
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> array){
            super.onPostExecute(array);
            tempView.setText(array.get(0));
            condView.setText(array.get(1));
        }
    }
}