package com.quixom.jewelrap.Model;

import org.json.JSONObject;

public class JewelRapItem {
    String categoryName, categoryImage, shapeTitle, date, id, status, firstName, lastName, itemName, color, discount, meas,
            purity, size, certificate, cut, polish, sym, fluor, shade, certificateNumber, city;
    int icon;

    String price;
    JSONObject jsonObject;
    JSONObject jsonObjectCategory;
    String favorite;
    String name;
    String badgecount, firmname;
    String newsTitle, newDescription;
    String address;
    String email;
    String website;
    String mobile;
    boolean clicked;
    boolean has_broker, has_qbc;
    boolean unread;


    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getShade() {
        return shade;
    }

    public void setShade(String shade) {
        this.shade = shade;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public boolean isHas_qbc() {
        return has_qbc;
    }

    public void setHas_qbc(boolean has_qbc) {
        this.has_qbc = has_qbc;
    }

    public boolean isHas_broker() {
        return has_broker;
    }

    public void setHas_broker(boolean has_broker) {
        this.has_broker = has_broker;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

    public String getFirmname() {
        return firmname;
    }

    public void setFirmname(String firmname) {
        this.firmname = firmname;
    }

    public String getBadgecount() {
        return badgecount;
    }

    public void setBadgecount(String badgecount) {
        this.badgecount = badgecount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public String getFluor() {
        return fluor;
    }

    public void setFluor(String fluor) {
        this.fluor = fluor;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }


    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
    }

    public String getMeas() {
        return meas;
    }

    public void setMeas(String meas) {
        this.meas = meas;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getPurity() {
        return purity;
    }

    public void setPurity(String purity) {
        this.purity = purity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getShapeTitle() {
        return shapeTitle;
    }

    public void setShapeTitle(String shapeTitle) {
        this.shapeTitle = shapeTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public JSONObject getJsonObjectCategory() {
        return jsonObjectCategory;
    }

    public void setJsonObjectCategory(JSONObject jsonObjectCategory) {
        this.jsonObjectCategory = jsonObjectCategory;
    }
}
