package com.quixomtbx.jewelrap.categoriesPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/9/2017.
 */

public class Certification {

    @SerializedName("certified_name")
    @Expose
    private String certifiedName;
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("certified_values")
    @Expose
    private String certifiedValues;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order")
    @Expose
    private Integer order;

    public String getCertifiedName() {
        return certifiedName;
    }

    public void setCertifiedName(String certifiedName) {
        this.certifiedName = certifiedName;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getCertifiedValues() {
        return certifiedValues;
    }

    public void setCertifiedValues(String certifiedValues) {
        this.certifiedValues = certifiedValues;
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
