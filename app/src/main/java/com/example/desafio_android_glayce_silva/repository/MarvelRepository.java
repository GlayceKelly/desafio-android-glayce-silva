package com.example.desafio_android_glayce_silva.repository;

import com.example.desafio_android_glayce_silva.model.characters.Personagens;
import com.example.desafio_android_glayce_silva.model.comicsid.Comics;

import io.reactivex.Observable;

import static com.example.desafio_android_glayce_silva.data.remote.RetrofitService.getApiService;

public class MarvelRepository {

    public Observable<Personagens> getPersonagemRepository(int pagina, String orderBy, String ts, String hash, String apiKey) {
        return getApiService().getALLPersonagens(pagina, orderBy, ts, hash, apiKey);
    }

    public Observable<Comics> getComicsRepository(Long characterid, String ts, String hash, String apiKey) {
        return getApiService().getIdComics(Integer.parseInt(characterid.toString()), ts, hash, apiKey);
    }
}



