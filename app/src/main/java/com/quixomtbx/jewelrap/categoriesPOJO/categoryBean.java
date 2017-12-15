package com.quixomtbx.jewelrap.categoriesPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TBX on 12/9/2017.
 */

public class categoryBean {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("objects")
    @Expose
    private List<Object> objects = null;
    @SerializedName("jr_stocks")
    @Expose
    private List<JrStock> jrStocks = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public List<JrStock> getJrStocks() {
        return jrStocks;
    }

    public void setJrStocks(List<JrStock> jrStocks) {
        this.jrStocks = jrStocks;
    }

}
