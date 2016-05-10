package com.shenshenff.headportrait.modes;

import java.io.Serializable;

/**
 * Created by shenshen on 16/3/18.
 */
public class SortItem implements Serializable {

    private int id;
    private String name;           //名字
    private int quantity;          //数量
    private String coverImgUrl;    //封面图URL
    private String contentUrl;     //内容URL
    private String contentJsonStr; //内容的json字符串

    public SortItem() {
    }

    public SortItem(int id, String name, String coverImgUrl, String contentUrl) {
        this.id = id;
        this.name = name;
        this.coverImgUrl = coverImgUrl;
        this.contentUrl = contentUrl;
    }

    public SortItem(int id, String name, String coverImgUrl, String contentUrl,String contentJsonStr) {
        this.id = id;
        this.name = name;
        this.coverImgUrl = coverImgUrl;
        this.contentUrl = contentUrl;
        this.contentJsonStr = contentJsonStr;
    }

    @Override
    public String toString() {
        return "SortItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", contentJsonStr='" + contentJsonStr + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContentJsonStr() {
        return contentJsonStr;
    }

    public void setContentJsonStr(String contentJsonStr) {
        this.contentJsonStr = contentJsonStr;
    }
}
