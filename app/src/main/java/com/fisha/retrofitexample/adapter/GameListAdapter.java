package com.fisha.retrofitexample.adapter;

import android.view.ViewGroup;

import com.fisha.retrofitexample.model.Game;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class GameListAdapter extends ListAdapter<Game, GameViewHolder>
{
    @Nullable
    private final GameAdapterListener mListener;

    public GameListAdapter(@NonNull DiffUtil.ItemCallback<Game> diffCallback, @Nullable GameAdapterListener mListener) {
        super(diffCallback);
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return GameViewHolder.create(parent, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game current = getItem(position);
        holder.bind(current);
    }

    @Override
    public void submitList(@Nullable List<Game> list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
        //super.submitList(list);
    }

    public static class GameDiff extends DiffUtil.ItemCallback<Game>
    {
        // Called to check whether two objects represent the same item.
        @Override
        public boolean areItemsTheSame(@NonNull Game oldItem, @NonNull Game newItem) {
            return oldItem.getId() == newItem.getId();
            //return oldItem.equals(newItem); //oldItem == newItem;
            //return false;
        }

        // Called to check whether two items have the same data.
        @Override
        public boolean areContentsTheSame(@NonNull Game oldItem, @NonNull Game newItem)
        {
            boolean title = oldItem.getTitle().equals(newItem.getTitle());
            if (!title)
                return false;

            boolean cover = false;
            if (oldItem.getCover() == null) {
                return newItem.getCover() == null;
            } else {
                if (newItem.getCover() != null) {
                    if (oldItem.getCover().getId() == newItem.getCover().getId())
                        return true;
                    else
                        return false;
                }
            }

            return true;
        }
    }
}
