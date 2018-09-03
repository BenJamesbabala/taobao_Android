package com.example.taobao;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 袁依吉 on 2018/7/5.
 */

public class MyData extends DataSupport implements Serializable {
    private Integer id;
    private String image;
    private String price;
    private String sold;
    private String title;
    private String location;
    private String item_id;

    public MyData(String image, String price, String sold, String title, String location, String item_id) {

        this.image = image;
        this.price = price;
        this.sold = sold;
        this.title = title;
        this.location = location;
        this.item_id = item_id;
    }

    public Integer getId() {
        return id;
    }

    public MyData() {
    }

    @Override
    public String toString() {
        return "MyData{" +
                "id=" + id +
                ", image=" + image +
                ", price='" + price + '\'' +
                ", sold='" + sold + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", item_id='" + item_id + '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}






