package com.example.dogstest;

import androidx.lifecycle.LiveData;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface ApiService { // ApiService всегда нужно создавать для работы с сетью, где будут прописаны правила использования клиентом интернет запросов

    @GET("random")
    Single<DogImage> loadDogImage();
}
