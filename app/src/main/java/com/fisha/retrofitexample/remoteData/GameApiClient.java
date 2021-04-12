package com.fisha.retrofitexample.remoteData;

import android.util.Log;

import com.fisha.retrofitexample.utils.AppExecutors;
import com.fisha.retrofitexample.model.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class GameApiClient
{
    private static GameApi gameApi;
    private static GameApiClient INSTANCE;
    private final MutableLiveData<List<Game>> gameListLD;

    private RetrieveGamesRunnable retrieveGamesRunnable;
    private RetrieveGamesTopRunnable retrieveGamesTopRunnable;

    private static final String FIELDS = "fields name, cover.image_id, genres.name, first_release_date, summary, storyline, rating, rating_count, follows, videos.name, videos.video_id, screenshots.image_id;";
    /*  "category"
        main_game	0
        dlc_addon	1
        expansion	2
        bundle	    3
        standalone_expansion	4
        mod	        5
        episode	    6
        season	    7
        remake	    8
        remaster	9
        expanded_game	10
        port	    11
        fork	    12
    */
    private static final String WHERE = " where version_parent = null & platforms = (48, 167)"; // & category = 0

    public GameApiClient() {
        this.gameListLD = new MutableLiveData<>();
        gameApi = GameRetrofitService.getInterface();
    }

    public static GameApiClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameApiClient();
        }
        return INSTANCE;
    }

    public LiveData<List<Game>> getGameListLD()
    {
        return gameListLD;
    }

    public void searchGamesApi(String searchString, int page)
    {
        if (retrieveGamesRunnable != null) {
            retrieveGamesRunnable = null;
        }

        retrieveGamesRunnable = new RetrieveGamesRunnable(searchString, page);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveGamesRunnable);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            // Cancelling retofit call
            myHandler.cancel(true);
        }, 5000, TimeUnit.MILLISECONDS);
    }

    public void searchGamesTopApi(int page)
    {
        if (retrieveGamesTopRunnable != null) {
            retrieveGamesTopRunnable = null;
        }

        retrieveGamesTopRunnable = new RetrieveGamesTopRunnable(page);

        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveGamesTopRunnable);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            myHandler2.cancel(true);
        }, 5000, TimeUnit.MILLISECONDS);
    }

    public void clearGameListLD() {
        gameListLD.setValue(null);
    }

    private class RetrieveGamesRunnable implements Runnable
    {
        private final String searchString;
        private final int page;
        boolean cancelRequest;

        public RetrieveGamesRunnable(String searchString, int page) {
            this.searchString = searchString;
            this.page = page;
            this.cancelRequest = false;
        }

        @Override
        public void run()
        {
            try {
                Response response = searchGames(searchString, page).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<Game> pojo;
                    if (gameListLD.getValue() == null)
                        pojo = new ArrayList<>();
                    else {
                        pojo = gameListLD.getValue();
                    }
                    List<Game> searchList = (List<Game>) response.body();
                    for (Game s : searchList) {
                        if (s != null)
                            pojo.add(s);
                    }
                    //pojo.sort((o1, o2) -> Long.compare(o2.getRelease(), o1.getRelease()));
                    gameListLD.postValue(pojo);
                }
                else {
                    Log.e("TAG_GAMES", "Error: " + response.errorBody().string());
                    gameListLD.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Call<List<Game>> searchGames(String searchString, int page) {
            int limit = 10;
            int offset = (page-1) * limit;

            String bodyString = FIELDS +
                    " search \"" + searchString + "\";" +
                    " limit " + limit + ";" +
                    " offset " + offset + ";" +
                    WHERE + ";";

            RequestBody body = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), (bodyString));
            return gameApi.searchGames(body);
        }

        private void cancelRequest() {
            Log.i("G_API_CLIENT", "Cancelling Search");
            cancelRequest = true;
        }
    }

    private class RetrieveGamesTopRunnable implements Runnable
    {
        private final int page;
        boolean cancelRequest;

        public RetrieveGamesTopRunnable(int page) {
            this.page = page;
            this.cancelRequest = false;
        }

        @Override
        public void run()
        {
            try {
                Response<List<Game>> response2 = getTopGames(page).execute();
                if (cancelRequest) {
                    return;
                }
                if (response2.code() == 200) {
                    List<Game> pojo;
                    if (gameListLD.getValue() == null)
                        pojo = new ArrayList<>();
                    else {
                        pojo = gameListLD.getValue();
                    }
                    List<Game> searchList = (List<Game>) response2.body();
                    for (Game s : searchList) {
                        if (s != null)
                            pojo.add(s);
                    }
                    //pojo.sort((o1, o2) -> Long.compare(o2.getRelease(), o1.getRelease()));
                    gameListLD.postValue(pojo);
                }
                else {
                    Log.e("TAG_GAMES", "Error: " + response2.errorBody().string());
                    gameListLD.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Call<List<Game>> getTopGames(int page) {
            int limit = 10;
            int offset = (page - 1) * limit;

            String bodyString = FIELDS +
                    " limit " + limit + ";" +
                    " offset " + offset + ";" +
                    WHERE + " & rating != null & rating_count > 100;" +
                    " sort rating desc;";

            RequestBody body = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), (bodyString));
            return gameApi.getTopGames(body);
        }

        private void cancelRequest() {
            Log.i("G_API_CLIENT", "Cancelling Search");
            cancelRequest = true;
        }
    }
}