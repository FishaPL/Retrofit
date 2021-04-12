package com.fisha.retrofitexample.adapter;

import android.view.ViewGroup;

import com.fisha.retrofitexample.model.Screenshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class ScreenshotListAdapter extends ListAdapter<Screenshot, ScreenshotViewHolder>
{
    @Nullable
    private final ScreenshotAdapterListener mListener;

    public ScreenshotListAdapter(@NonNull DiffUtil.ItemCallback<Screenshot> diffCallback, @Nullable ScreenshotAdapterListener mListener) {
        super(diffCallback);
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ScreenshotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ScreenshotViewHolder.create(parent, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreenshotViewHolder holder, int position) {
        Screenshot screenshot = getItem(position);
        holder.bind(screenshot, position);
    }
}
