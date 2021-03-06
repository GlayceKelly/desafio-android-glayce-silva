package com.example.desafio_android_glayce_silva.viewmodel;

import android.app.Application;
import android.util.Log;

import com.example.desafio_android_glayce_silva.data.util.AppUtil;
import com.example.desafio_android_glayce_silva.model.characters.Result;
import com.example.desafio_android_glayce_silva.repository.MarvelRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MarvelViewModel extends AndroidViewModel {

    private MutableLiveData<List<Result>> listaPersona = new MutableLiveData<>();
    private MutableLiveData<List<com.example.desafio_android_glayce_silva.model.comicsid.Result>> listaComics = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private MarvelRepository repository = new MarvelRepository();
    private static final String PUBLIC_API_KEY = "6eb7e8896ec5850c52515a8a23ee97f0";
    private static final String PRIVATE_API_KEY = "0dd0c16fedb8a02985977eafca66b49f5e6a526f";
    String ts = Long.toString(System.currentTimeMillis() / 1000);
    String hash = AppUtil.md5(ts + PRIVATE_API_KEY + PUBLIC_API_KEY);

    public MarvelViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Result>> getListaPersonagens() {
        return this.listaPersona;
    }

    public LiveData<List<com.example.desafio_android_glayce_silva.model.comicsid.Result>> getListaComics() {
        return this.listaComics;
    }

    public LiveData<Boolean> getLoading() {
        return this.loading;
    }

    public void getPersonagens(int pagina) {
        disposable.add(
                repository.getPersonagemRepository(pagina,"name", ts, hash, PUBLIC_API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable1 -> loading.setValue(true))
                        .doOnTerminate(() -> loading.setValue(false))
                        .subscribe(personaResult -> {
                            listaPersona.setValue(personaResult.getData().getResults());
                        }, throwable -> {
                            Log.i("LOG", "Error: " + throwable.getMessage());
                        }));
    }

    public void getComics(Long characterid) {
        disposable.add(
                repository.getComicsRepository(characterid, ts, hash, PUBLIC_API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable1 -> loading.setValue(true))
                        .doOnTerminate(() -> loading.setValue(false))
                        .subscribe(comicsResult -> {
                            listaComics.setValue(comicsResult.getData().getResults());
                        }, throwable -> {
                            Log.i("LOG", "Error: " + throwable.getMessage());
                        }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
