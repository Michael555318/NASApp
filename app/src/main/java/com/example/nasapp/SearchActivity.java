package com.example.nasapp;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private TextView title1;
    private TextView date1;
    private TextView explanation1;
    private ImageView pic;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        wireWidgets();

        getAPOD();
    }

    private void wireWidgets() {
        title1 = findViewById(R.id.textview_title);
        date1 = findViewById(R.id.textview_date);
        explanation1 = findViewById(R.id.textview_explanation);
        pic = findViewById(R.id.imageView_apod);
    }

    private void getAPOD() {
        // need GSON and converter-gson libraries for this step
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NasaService service =
                retrofit.create(NasaService.class);

        Call<NasaResponse> nasaResponseCall =
                service.searchToday(Credentials.api_key);

//        final NasaResponse[] response1 = {new NasaResponse()};

        nasaResponseCall.enqueue(new Callback<NasaResponse>() {
            @Override
            public void onResponse(Call<NasaResponse> call,
                                   Response<NasaResponse> response) {
                date1.setText(response.body().getDate());
                title1.setText(response.body().getTitle());
                explanation1.setText(response.body().getExplanation());
                Log.d(TAG, response.body().getUrl());
                Picasso.get()
                        .load(response.body().getUrl())
                        .into(pic);
            }

            @Override
            public void onFailure(Call<NasaResponse> call,
                    Throwable t) {
                Toast.makeText(SearchActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
