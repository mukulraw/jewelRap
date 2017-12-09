package com.quixom.jewelrap.categoriesPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/9/2017.
 */

public class Purity {

    @SerializedName("purity_name")
    @Expose
    private String purityName;
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order")
    @Expose
    private Integer order;

    public String getPurityName() {
        return purityName;
    }

    public void setPurityName(String purityName) {
        this.purityName = purityName;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

}
