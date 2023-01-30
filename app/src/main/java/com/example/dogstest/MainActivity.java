package com.example.dogstest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private ProgressBar progressBar;
    private Button buttonGetImage;
    private ImageView imageViewdogs;

    private static final String logMessage = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();


        viewModel = new ViewModelProvider(this).get(MainViewModel.class); // подключаем вью модель
        viewModel.downloadDogImage(); // скачиваем фото

        viewModel.getIsLoading().observe(this, new Observer<Boolean>() { // нужно для того, чтобы показывать прогрессбар
            @Override
            public void onChanged(Boolean loading) {
                if (loading)
                    progressBar.setVisibility(View.VISIBLE);
                else
                    progressBar.setVisibility(View.GONE);

            }
        });

        viewModel.getIsInternet().observe(this, new Observer<Boolean>() { // проверяем подключение интернета
            @Override
            public void onChanged(Boolean isInternetOn) {
                if (!isInternetOn)
                    Toast.makeText(
                                    MainActivity.this,
                                    R.string.error_connection,
                                    Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getDogImage().observe(this, new Observer<DogImage>() { // получаем фото
            @Override
            public void onChanged(DogImage dogImage) {
                Glide.with(MainActivity.this) // загрузка фото в ImageView
                        .load(dogImage.getMessage())
                        .into(imageViewdogs);
            }
        });

        buttonGetImage.setOnClickListener(new View.OnClickListener() { // реагирование на нажатие кнопки
            @Override
            public void onClick(View v) {
                viewModel.downloadDogImage();
            }
        });



    }


    private void initViews() {
         progressBar = findViewById(R.id.progressBarLoad);
         buttonGetImage = findViewById(R.id.buttonGetImage);
         imageViewdogs = findViewById(R.id.imageViewDogs);
    }

}