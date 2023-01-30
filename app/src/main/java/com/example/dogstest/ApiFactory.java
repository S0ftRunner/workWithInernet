package com.example.dogstest;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory { // здесь происходит подключение к API

    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/";

    private static ApiService apiService; // используем Singletone

    public static ApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // указываем базовый URL
                    .addConverterFactory(GsonConverterFactory.create()) // договариваемся о том, что преобразование JSON объектов экземпляров нашего класса мы будем преобразовывать через Gson
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // показываем, что используем RxJava
                    .build();
           apiService = retrofit.create(ApiService.class);
        }
        return apiService;

    }

}
