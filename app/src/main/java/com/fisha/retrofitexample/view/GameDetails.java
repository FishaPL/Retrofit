package com.fisha.retrofitexample.view;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.Rotate;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.fisha.retrofitexample.R;
import com.fisha.retrofitexample.adapter.ScreenshotAdapterListener;
import com.fisha.retrofitexample.adapter.ScreenshotListAdapter;
import com.fisha.retrofitexample.databinding.ActivityGameDetailsBinding;
import com.fisha.retrofitexample.model.Game;
import com.fisha.retrofitexample.model.Screenshot;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.bumptech.glide.Glide.with;

public class GameDetails extends AppCompatActivity implements ScreenshotAdapterListener
{
    private ActivityGameDetailsBinding binding;
    private ScreenshotListAdapter screenshotAdapter;
    private volatile int pos;
    private Bitmap bitmapScreenshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameDetailsBinding.inflate(getLayoutInflater());

        getSupportActionBar().hide();

        screenshotAdapter = new ScreenshotListAdapter(null, this);
        binding.recyclerviewGameDetais.setAdapter(screenshotAdapter);
        binding.recyclerviewGameDetais.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        GetDataFromIntent();

        setContentView(binding.getRoot());
    }

    private void GetDataFromIntent() {
        if (getIntent().hasExtra("game")) {
            Game game = getIntent().getParcelableExtra("game");

            if (game != null)
            {
                binding.setGame(game);

                if (game.getCover() != null) {
                    with(this)
                            .load(game.getCover().getImageUrl("720p"))
                            .into(binding.imgCoverGameDetails);
                }

                CircularProgressBar cpb = binding.getRoot().findViewById(R.id.circularProgressBar);
                cpb.setProgress((float)game.getRating());

                screenshotAdapter.submitList(new ArrayList<>(Arrays.asList(game.getScreenshots())));
            }

        }
    }

    @Override
    public void onScreenshotClick(int position)
    {
        Screenshot screenshot = screenshotAdapter.getCurrentList().get(position);
        if (screenshot.getImageUrl() != null)
        {
            //AtomicInteger pos = new AtomicInteger(position);
            pos = position;
            Dialog screenshotImgDialog;
            screenshotImgDialog = new Dialog(this);
            screenshotImgDialog.setContentView(R.layout.dialog_screenshot);
            screenshotImgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            screenshotImgDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
            //screenshotImgDialog.getWindow().getAttributes().gravity = Gravity.TOP;

            SubsamplingScaleImageView img = screenshotImgDialog.findViewById(R.id.img_screenshot_dialog);
            Glide.with(this)
                    .asBitmap()
                    .load(screenshot.getImageUrl("screenshot_huge"))
                    .transform(new Rotate(90))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            bitmapScreenshot = resource;
                            img.setImage(ImageSource.cachedBitmap(bitmapScreenshot));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

            FloatingActionButton fabClose = screenshotImgDialog.findViewById(R.id.fab_close_screenshot_dialog);
            FloatingActionButton fabPrev = screenshotImgDialog.findViewById(R.id.fab_prev_screenshot_dialog);
            FloatingActionButton fabNext = screenshotImgDialog.findViewById(R.id.fab_next_screenshot_dialog);
            enableDisableFabs(fabPrev, fabNext);

            fabClose.setOnClickListener(view -> {
                screenshotImgDialog.dismiss();
            });

            fabNext.setOnClickListener(view -> {
                pos++;
                if (pos < screenshotAdapter.getCurrentList().size()) {
                    setScreenshotImgToDialog(img);
                }
                enableDisableFabs(fabPrev, fabNext);
            });

            fabPrev.setOnClickListener(view -> {
                pos--;
                if (pos > 0) {
                    setScreenshotImgToDialog(img);
                }
                enableDisableFabs(fabPrev, fabNext);
            });

            screenshotImgDialog.show();
        }
    }

    public void setScreenshotImgToDialog(SubsamplingScaleImageView img)
    {
        Screenshot next = screenshotAdapter.getCurrentList().get(pos);
        Glide.with(this)
                .asBitmap()
                .load(next.getImageUrl("screenshot_huge"))
                .transform(new Rotate(90))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmapScreenshot = resource;
                        img.setImage(ImageSource.cachedBitmap(bitmapScreenshot));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void enableDisableFabs(FloatingActionButton fabPrev, FloatingActionButton fabNext) {
        int size = screenshotAdapter.getItemCount();
        if (pos == 0) {
            fabPrev.setEnabled(false);
            if (size > 1)
                fabNext.setEnabled(true);
            else
                fabNext.setEnabled(false);
        }
        else if (pos == (size - 1)) {
            fabPrev.setEnabled(true);
            fabNext.setEnabled(false);
        }
        else {
            fabPrev.setEnabled(true);
            fabNext.setEnabled(true);
        }
    }
}