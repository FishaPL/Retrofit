package com.fisha.retrofitexample.remoteData;

import com.fisha.retrofitexample.model.Game;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GameApi
{
    @POST("games")
    Call<List<Game>> searchGames(@Body RequestBody body);

    @POST("games")
    Call<List<Game>> getTopGames(@Body RequestBody body);
}
