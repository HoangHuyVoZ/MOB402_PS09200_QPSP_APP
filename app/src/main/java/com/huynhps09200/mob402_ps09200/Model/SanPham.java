package com.huynhps09200.mob402_ps09200.Model;

public class SanPham {
    public String _id;
    public String Name;
    public String Price;
    public String Description;
    public String Image;

    public SanPham(String name, String price, String description, String image) {
        Name = name;
        Price = price;
        Description = description;
        Image = image;
    }
    public SanPham(String name, String price, String description) {
        Name = name;
        Price = price;
        Description = description;

    }
    public SanPham() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
