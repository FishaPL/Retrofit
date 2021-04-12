package com.fisha.retrofitexample.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fisha.retrofitexample.R;
import com.fisha.retrofitexample.databinding.GameItemBinding;
import com.fisha.retrofitexample.model.Game;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GameViewHolder extends RecyclerView.ViewHolder
{
    private final GameItemBinding binding;

    public GameViewHolder(@NonNull GameItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static GameViewHolder create(ViewGroup parent, GameAdapterListener mListener)
    {
        GameItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.game_item, parent, false);
        binding.setClickListener(mListener);
        return new GameViewHolder(binding);
    }

    public void bind(Game game)
    {
        binding.setGame(game);
        if (game.getCover() != null) {
            Glide.with(binding.imageViewGameCover.getContext())
                    .load(game.getCover().getImageUrl())
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imageViewGameCover);
        }
    }
}
