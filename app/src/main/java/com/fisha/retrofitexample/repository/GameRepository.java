package com.fisha.retrofitexample.repository;

import com.fisha.retrofitexample.model.Game;
import com.fisha.retrofitexample.remoteData.GameApiClient;

import java.util.List;

import androidx.lifecycle.LiveData;

public class GameRepository
{
    private static GameRepository INSTANCE;
    private GameApiClient gameApiClient;

    private String mQuery;
    private int mPageNumber;

    private GameRepository() {
        gameApiClient = GameApiClient.getInstance();
    }

    public static GameRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameRepository();
        }
        return INSTANCE;
    }

    public LiveData<List<Game>> getGameListLD() {
        return gameApiClient.getGameListLD();
    }

    public void searchGamesApi(String searchString, int page) {
        mQuery = searchString;
        mPageNumber = page;
        gameApiClient.searchGamesApi(searchString, page);
    }

    public void searchNextPage(boolean isTop){
        if (isTop)
            getTopGames(mPageNumber + 1);
        else
            searchGamesApi(mQuery, mPageNumber + 1);
    }

    public void getTopGames(int page) {
        mPageNumber = page;
        gameApiClient.searchGamesTopApi(page);
    }

    public void clearGameListLD() {
        gameApiClient.clearGameListLD();
    }
}
