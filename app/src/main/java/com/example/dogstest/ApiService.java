package com.example.dogstest;

import androidx.lifecycle.LiveData;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface ApiService {

    @GET("random")
    Single<DogImage> loadDogImage();
}
