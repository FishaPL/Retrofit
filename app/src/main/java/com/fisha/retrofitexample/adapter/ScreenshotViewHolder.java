package com.fisha.retrofitexample.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fisha.retrofitexample.R;
import com.fisha.retrofitexample.databinding.ScreenshotItemBinding;
import com.fisha.retrofitexample.model.Screenshot;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ScreenshotViewHolder extends RecyclerView.ViewHolder
{
    private final ScreenshotItemBinding binding;

    public ScreenshotViewHolder(ScreenshotItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static ScreenshotViewHolder create(ViewGroup parent, ScreenshotAdapterListener mListener)
    {
        ScreenshotItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.screenshot_item, parent, false);
        binding.setClickListener(mListener);
        return new ScreenshotViewHolder(binding);
    }

    public void bind(Screenshot screenshot, int position)
    {
        binding.setScreenshot(screenshot);
        binding.setPosition(position);

        if (screenshot.getImageUrl() != null) {
            Glide.with(binding.imgScreenshotItem.getContext())
                    .load(screenshot.getImageUrl())
                    .into(binding.imgScreenshotItem);
        }
    }
}
