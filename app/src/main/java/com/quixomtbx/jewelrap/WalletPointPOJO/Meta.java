package com.quixomtbx.jewelrap.WalletPointPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 12/13/2017.
 */

public class Meta {


    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("next")
    @Expose
    private java.lang.Object next;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("previous")
    @Expose
    private java.lang.Object previous;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public java.lang.Object getNext() {
        return next;
    }

    public void setNext(java.lang.Object next) {
        this.next = next;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public java.lang.Object getPrevious() {
        return previous;
    }

    public void setPrevious(java.lang.Object previous) {
        this.previous = previous;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
