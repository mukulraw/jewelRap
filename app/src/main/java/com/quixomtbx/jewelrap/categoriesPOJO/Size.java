package com.quixomtbx.jewelrap.categoriesPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/9/2017.
 */

public class Size {

    @SerializedName("is_visible")
    @Expose
    private Boolean isVisible;
    @SerializedName("size_name")
    @Expose
    private String sizeName;
    @SerializedName("size_image")
    @Expose
    private java.lang.Object sizeImage;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order")
    @Expose
    private Integer order;

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public java.lang.Object getSizeImage() {
        return sizeImage;
    }

    public void setSizeImage(java.lang.Object sizeImage) {
        this.sizeImage = sizeImage;
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
