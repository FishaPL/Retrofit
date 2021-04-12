package com.fisha.retrofitexample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Screenshot implements Parcelable {
    private int id;
    @SerializedName("image_id")
    private String imgUrl;

    protected Screenshot(Parcel in) {
        id = in.readInt();
        imgUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imgUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Screenshot> CREATOR = new Creator<Screenshot>() {
        @Override
        public Screenshot createFromParcel(Parcel in) {
            return new Screenshot(in);
        }

        @Override
        public Screenshot[] newArray(int size) {
            return new Screenshot[size];
        }
    };

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
        return getImageUrl("screenshot_med");
    }

    public String getImageUrl(String size) {
        return "https://images.igdb.com/igdb/image/upload/t_" + size+ "/" + imgUrl +".jpg";
    }
}
