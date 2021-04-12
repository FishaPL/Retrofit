package com.fisha.retrofitexample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class Game implements Parcelable
{
    private int id;
    @SerializedName("name")
    private String title;
    private boolean completed;
    private Cover cover;
    private Genre[] genres;
    @SerializedName("first_release_date")
    private long release;
    private String summary;
    private String storyline;
    private double rating;
    @SerializedName("rating_count")
    private int ratingCount;
    private int follows;
    private Screenshot[] screenshots;
    private Video[] videos;

    public Game(){
        this.completed = false;
    }


    protected Game(Parcel in) {
        id = in.readInt();
        title = in.readString();
        completed = in.readByte() != 0;
        cover = in.readParcelable(Cover.class.getClassLoader());
        genres = in.createTypedArray(Genre.CREATOR);
        release = in.readLong();
        summary = in.readString();
        storyline = in.readString();
        rating = in.readDouble();
        ratingCount = in.readInt();
        follows = in.readInt();
        screenshots = in.createTypedArray(Screenshot.CREATOR);
        videos = in.createTypedArray(Video.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeParcelable(cover, flags);
        dest.writeTypedArray(genres, flags);
        dest.writeLong(release);
        dest.writeString(summary);
        dest.writeString(storyline);
        dest.writeDouble(rating);
        dest.writeInt(ratingCount);
        dest.writeInt(follows);
        dest.writeTypedArray(screenshots, flags);
        dest.writeTypedArray(videos, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public String getGenresString() {
        if (genres == null)
            return "";
        return Arrays.stream(genres).map(Genre::getName).collect(Collectors.joining(", "));
    }

    public String getReleaseString() {
        Date date = new Date(release*1000);
        SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return fmt.format(date);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Cover getCover() {
        return cover;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public long getRelease() {
        return release;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public String getSummary() {
        return summary;
    }

    public String getStoryline() {
        return storyline;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public int getFollows() {
        return follows;
    }

    public Screenshot[] getScreenshots() {
        return screenshots;
    }

    public Video[] getVideos() {
        return videos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id &&
                completed == game.completed &&
                release == game.release &&
                Double.compare(game.rating, rating) == 0 &&
                ratingCount == game.ratingCount &&
                follows == game.follows &&
                Objects.equals(title, game.title) &&
                Objects.equals(cover, game.cover) &&
                Arrays.equals(genres, game.genres) &&
                Objects.equals(summary, game.summary) &&
                Objects.equals(storyline, game.storyline);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, completed, cover, release, summary, storyline, rating, ratingCount, follows);
        result = 31 * result + Arrays.hashCode(genres);
        return result;
    }
}
