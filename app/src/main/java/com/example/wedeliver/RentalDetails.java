package com.example.wedeliver;

class RentalDetails
{
    String name,price,address,services,mobilenumber,imageview;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImageview(String imageview) {
        this.imageview = imageview;
    }

    public String getImageview() {
        return imageview;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getAddress() {
        return address;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getServices() {
        return services;
    }

}
