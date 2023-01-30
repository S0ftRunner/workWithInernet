package com.example.dogstest;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private static final String logMessage = "MainViewModel";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<DogImage> dogImage = new MutableLiveData<>(); // реагирование на загрузку фото
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(); // реагирование на загрузку для ProgressBar
    private MutableLiveData<Boolean> isInternet = new MutableLiveData<>(); // реагирование на подключение


    public LiveData<DogImage> getDogImage() {
        return dogImage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsInternet() {
        return isInternet;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void downloadDogImage() {
        Disposable disposable = loadDogImageRx() // работа в интернете должна выполняться в фоновом режиме
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true); // здесь состояние загрузки отображается в тот момент, когда загрузка началась
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false); // после загрузки прогрессбар здесь будет исчезать
                    }
                })
                .subscribe(new Consumer<DogImage>() {
                    @Override
                    public void accept(DogImage image) throws Throwable {
                        dogImage.setValue(image);
                        isInternet.setValue(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(logMessage, "Error" + throwable.getMessage()); // если нет интернета
                        isInternet.setValue(false);
                    }
                });
        compositeDisposable.add(disposable);
    }

    private Single<DogImage> loadDogImageRx() {
        return ApiFactory.getApiService().loadDogImage();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
