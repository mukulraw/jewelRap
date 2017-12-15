package com.quixomtbx.jewelrap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/8/2017.
 */

public class loginBean {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("days_left")
    @Expose
    private Integer daysLeft;
    @SerializedName("is_subscribed")
    @Expose
    private Boolean isSubscribed;
    @SerializedName("unqiue_Id")
    @Expose
    private String unqiueId;
    @SerializedName("firm_name")
    @Expose
    private String firmName;
    @SerializedName("solitaire_active")
    @Expose
    private Boolean solitaireActive;
    @SerializedName("LD_active_")
    @Expose
    private Boolean lDActive;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("userrole")
    @Expose
    private String userrole;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("stock_count_ld")
    @Expose
    private Integer stockCountLd;
    @SerializedName("is_verified")
    @Expose
    private Boolean isVerified;
    @SerializedName("stock_count")
    @Expose
    private Integer stockCount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public Integer getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(Integer daysLeft) {
        this.daysLeft = daysLeft;
    }

    public Boolean getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(Boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public String getUnqiueId() {
        return unqiueId;
    }

    public void setUnqiueId(String unqiueId) {
        this.unqiueId = unqiueId;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public Boolean getSolitaireActive() {
        return solitaireActive;
    }

    public void setSolitaireActive(Boolean solitaireActive) {
        this.solitaireActive = solitaireActive;
    }

    public Boolean getLDActive() {
        return lDActive;
    }

    public void setLDActive(Boolean lDActive) {
        this.lDActive = lDActive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStockCountLd() {
        return stockCountLd;
    }

    public void setStockCountLd(Integer stockCountLd) {
        this.stockCountLd = stockCountLd;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

}
