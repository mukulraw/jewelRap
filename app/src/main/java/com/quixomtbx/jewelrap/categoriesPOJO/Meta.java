package com.quixomtbx.jewelrap.categoriesPOJO;

import com.google.android.gms.nearby.messages.internal.Update;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/9/2017.
 */

public class Meta {

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("unique_id_home")
    @Expose
    private UniqueIdHome uniqueIdHome;
    @SerializedName("has_broker")
    @Expose
    private Boolean hasBroker;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("is_subscribed")
    @Expose
    private Boolean isSubscribed;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("update")
    @Expose
    private Update update;
    @SerializedName("firm_name")
    @Expose
    private String firmName;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("news_count")
    @Expose
    private Integer newsCount;
    @SerializedName("next")
    @Expose
    private java.lang.Object next;
    @SerializedName("unique_id")
    @Expose
    private Integer uniqueId;
    @SerializedName("previous")
    @Expose
    private java.lang.Object previous;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UniqueIdHome getUniqueIdHome() {
        return uniqueIdHome;
    }

    public void setUniqueIdHome(UniqueIdHome uniqueIdHome) {
        this.uniqueIdHome = uniqueIdHome;
    }

    public Boolean getHasBroker() {
        return hasBroker;
    }

    public void setHasBroker(Boolean hasBroker) {
        this.hasBroker = hasBroker;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Boolean getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(Boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(Integer newsCount) {
        this.newsCount = newsCount;
    }

    public java.lang.Object getNext() {
        return next;
    }

    public void setNext(java.lang.Object next) {
        this.next = next;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    public java.lang.Object getPrevious() {
        return previous;
    }

    public void setPrevious(java.lang.Object previous) {
        this.previous = previous;
    }


}
