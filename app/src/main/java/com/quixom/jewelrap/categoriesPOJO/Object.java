package com.quixom.jewelrap.categoriesPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TBX on 12/9/2017.
 */

public class Object {

    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("certification")
    @Expose
    private List<Certification> certification = null;
    @SerializedName("color")
    @Expose
    private List<Color> color = null;
    @SerializedName("badge_count")
    @Expose
    private Integer badgeCount;
    @SerializedName("size")
    @Expose
    private List<Size> size = null;
    @SerializedName("state")
    @Expose
    private List<String> state = null;
    @SerializedName("shape")
    @Expose
    private List<Shape> shape = null;
    @SerializedName("purity")
    @Expose
    private List<Purity> purity = null;
    @SerializedName("Inquiry_category_image")
    @Expose
    private String inquiryCategoryImage;
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public List<Certification> getCertification() {
        return certification;
    }

    public void setCertification(List<Certification> certification) {
        this.certification = certification;
    }

    public List<Color> getColor() {
        return color;
    }

    public void setColor(List<Color> color) {
        this.color = color;
    }

    public Integer getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(Integer badgeCount) {
        this.badgeCount = badgeCount;
    }

    public List<Size> getSize() {
        return size;
    }

    public void setSize(List<Size> size) {
        this.size = size;
    }

    public List<String> getState() {
        return state;
    }

    public void setState(List<String> state) {
        this.state = state;
    }

    public List<Shape> getShape() {
        return shape;
    }

    public void setShape(List<Shape> shape) {
        this.shape = shape;
    }

    public List<Purity> getPurity() {
        return purity;
    }

    public void setPurity(List<Purity> purity) {
        this.purity = purity;
    }

    public String getInquiryCategoryImage() {
        return inquiryCategoryImage;
    }

    public void setInquiryCategoryImage(String inquiryCategoryImage) {
        this.inquiryCategoryImage = inquiryCategoryImage;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
