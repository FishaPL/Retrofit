package com.fisha.retrofitexample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Cover implements Parcelable
{
    private int id;
    @SerializedName("image_id")
    private String imageUrl;

    protected Cover(Parcel in) {
        id = in.readInt();
        imageUrl = in.readString();
    }

    public static final Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel in) {
            return new Cover(in);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };

    public int getId() {
        return id;
    }

    /*
    cover_small	    90 x 128	Fit
    screenshot_med	569 x 320	Lfill, Center gravity
    cover_big	    264 x 374	Fit
    logo_med	    284 x 160	Fit
    screenshot_big	889 x 500	Lfill, Center gravity
    screenshot_huge	1280 x 720	Lfill, Center gravity
    thumb	        90 x 90	    Thumb, Center gravity
    micro	        35 x 35	    Thumb, Center gravity
    720p	        1280 x 720	Fit, Center gravity
    1080p	        1920 x 1080	Fit, Center gravity
     */
    public String getImageUrl() {
        return getImageUrl("cover_small");
    }

    public String getImageUrl(String size) {
        return "https://images.igdb.com/igdb/image/upload/t_" + size+ "/" + imageUrl +".jpg";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(imageUrl);
    }
}
