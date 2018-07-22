package com.quixom.jewelrap.Model;

import org.json.JSONObject;

/**
 * Created by Quixom on 11/05/17.
 */

public class Inventory {
    String stockInfo, cut, polish, symmetry, fluor, meas, certNumber, image,id;
    JSONObject stockObject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONObject getStockObject() {
        return stockObject;
    }

    public void setStockObject(JSONObject stockObject) {
        this.stockObject = stockObject;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStockInfo() {
        return stockInfo;
    }

    public void setStockInfo(String stockInfo) {
        this.stockInfo = stockInfo;
    }

    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }

    public String getSymmetry() {
        return symmetry;
    }

    public void setSymmetry(String symmetry) {
        this.symmetry = symmetry;
    }

    public String getFluor() {
        return fluor;
    }

    public void setFluor(String fluor) {
        this.fluor = fluor;
    }

    public String getMeas() {
        return meas;
    }

    public void setMeas(String meas) {
        this.meas = meas;
    }

    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }
}
