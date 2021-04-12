package com.fisha.retrofitexample.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.fisha.retrofitexample.R;
import com.fisha.retrofitexample.adapter.GameAdapterListener;
import com.fisha.retrofitexample.adapter.GameListAdapter;
import com.fisha.retrofitexample.databinding.ActivityMainBinding;
import com.fisha.retrofitexample.model.Game;
import com.fisha.retrofitexample.viewModel.GameViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements GameAdapterListener
{
    private ActivityMainBinding binding;
    private GameViewModel gameViewModel;
    private GameListAdapter gameAdapter;
    private boolean isTop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameAdapter = new GameListAdapter(new GameListAdapter.GameDiff(), this);
        binding.recyclerview.setAdapter(gameAdapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    int state_0 = RecyclerView.SCROLL_STATE_IDLE;       // The RecyclerView is not currently scrolling.
                    int state_1 = RecyclerView.SCROLL_STATE_DRAGGING;   // The RecyclerView is currently being dragged by outside input such as user touch input.
                    int state_2 = RecyclerView.SCROLL_STATE_SETTLING;   // The RecyclerView is currently animating to a final position while not under outside control.

                    // TODO - SwipeRefreshLayout żeby dodać ikonke ładowania podczas pobierania nowych wartości
                    if (newState == RecyclerView.SCROLL_STATE_IDLE)
                        gameViewModel.searchNextPage(isTop);
                }
            }
        });

        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        gameViewModel.getGameListLD().observe(this, list -> gameAdapter.submitList(list));

        gameViewModel.clearGameListLD();
        gameViewModel.getTopGames(1);
    }

    @Override
    public void onGameClick(Game game) {
        Intent intent = new Intent(this, GameDetails.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                isTop = false;
                gameViewModel.clearGameListLD();
                gameViewModel.searchGamesApi(s, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search_games_dots));

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (!isTop) {
                    isTop = true;
                    gameViewModel.clearGameListLD();
                    gameViewModel.getTopGames(1);
                }
                return false;
            }
        });

        return true;
    }
}