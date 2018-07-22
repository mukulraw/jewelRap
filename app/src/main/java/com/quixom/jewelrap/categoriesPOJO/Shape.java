package com.quixom.jewelrap.categoriesPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/9/2017.
 */

public class Shape {

    @SerializedName("is_visible")
    @Expose
    private Boolean isVisible;
    @SerializedName("shape_name")
    @Expose
    private String shapeName;
    @SerializedName("shape_intials")
    @Expose
    private String shapeIntials;
    @SerializedName("for_ld_addin")
    @Expose
    private Boolean forLdAddin;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shape_image")
    @Expose
    private java.lang.Object shapeImage;
    @SerializedName("order")
    @Expose
    private java.lang.Object order;
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public String getShapeIntials() {
        return shapeIntials;
    }

    public void setShapeIntials(String shapeIntials) {
        this.shapeIntials = shapeIntials;
    }

    public Boolean getForLdAddin() {
        return forLdAddin;
    }

    public void setForLdAddin(Boolean forLdAddin) {
        this.forLdAddin = forLdAddin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public java.lang.Object getShapeImage() {
        return shapeImage;
    }

    public void setShapeImage(java.lang.Object shapeImage) {
        this.shapeImage = shapeImage;
    }

    public java.lang.Object getOrder() {
        return order;
    }

    public void setOrder(java.lang.Object order) {
        this.order = order;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

}
