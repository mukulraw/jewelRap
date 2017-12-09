package com.quixom.jewelrap.categoriesPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/9/2017.
 */

public class Color {

    @SerializedName("color_name")
    @Expose
    private String colorName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("order")
    @Expose
    private Integer order;

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

}
