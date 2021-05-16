package com.example.wedeliver;

class cartItemDetails
{
    String name,brand,selling_price,actual_price,count,quantity,image,dicount,basic_selling_price,basic_actual_price;
    cartItemDetails()
    {

    }

    public void setBasic_actual_price(String basic_actual_price) {
        this.basic_actual_price = basic_actual_price;
    }

    public String getBasic_selling_price() {
        return basic_selling_price;
    }

    public void setBasic_selling_price(String basic_selling_price) {
        this.basic_selling_price = basic_selling_price;
    }

    public String getBasic_actual_price() {
        return basic_actual_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setDicount(String dicount) {
        this.dicount = dicount;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public String getActual_price() {
        return actual_price;
    }

    public String getBrand() {
        return brand;
    }

    public String getCount() {
        return count;
    }

    public String getDicount() {
        return dicount;
    }

    public String getImage() {
        return image;
    }

    public String getQuantity() {
        return quantity;
    }
}
