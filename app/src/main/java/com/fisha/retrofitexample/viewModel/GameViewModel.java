package com.fisha.retrofitexample.viewModel;

import com.fisha.retrofitexample.model.Game;
import com.fisha.retrofitexample.repository.GameRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class GameViewModel extends ViewModel
{
    private final GameRepository gameRepository;

    public GameViewModel() {
        this.gameRepository = GameRepository.getInstance();
    }

    public LiveData<List<Game>> getGameListLD() {
        return gameRepository.getGameListLD();
    }

    public void clearGameListLD() {
        gameRepository.clearGameListLD();
    }

    public void searchGamesApi(String searchString, int page) {
        gameRepository.searchGamesApi(searchString, page);
    }

    public void searchNextPage(boolean isTop){
        gameRepository.searchNextPage(isTop);
    }

    public void getTopGames(int page) {
        gameRepository.getTopGames(page);
    }
}
