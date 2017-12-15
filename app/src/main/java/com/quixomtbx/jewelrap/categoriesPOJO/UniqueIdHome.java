package com.quixomtbx.jewelrap.categoriesPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/9/2017.
 */

public class UniqueIdHome {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("firm_name")
    @Expose
    private String firmName;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }


}
