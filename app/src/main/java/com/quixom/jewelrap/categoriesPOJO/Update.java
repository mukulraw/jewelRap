package com.quixom.jewelrap.categoriesPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/9/2017.
 */

public class Update {

    @SerializedName("android_version")
    @Expose
    private Double androidVersion;
    @SerializedName("ios_version")
    @Expose
    private Double iosVersion;
    @SerializedName("android_update_message")
    @Expose
    private String androidUpdateMessage;
    @SerializedName("ios_update_message")
    @Expose
    private String iosUpdateMessage;

    public Double getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(Double androidVersion) {
        this.androidVersion = androidVersion;
    }

    public Double getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(Double iosVersion) {
        this.iosVersion = iosVersion;
    }

    public String getAndroidUpdateMessage() {
        return androidUpdateMessage;
    }

    public void setAndroidUpdateMessage(String androidUpdateMessage) {
        this.androidUpdateMessage = androidUpdateMessage;
    }

    public String getIosUpdateMessage() {
        return iosUpdateMessage;
    }

    public void setIosUpdateMessage(String iosUpdateMessage) {
        this.iosUpdateMessage = iosUpdateMessage;
    }

}
