package com.example.wedeliver;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.View;

public class itemDetails {
    public String discount,actual_price,selling_price,product_name,product_brand,capacity,booked_items_count,no_of_items;
    public String imageView;
    itemDetails()
    {

    }
    itemDetails(String discount,String actual_price,String selling_price,String product_name,String product_brand,String capacity,String booked_items_count,String imageView)
    {
        this.imageView=imageView;
        this.discount=discount;
        this.actual_price=actual_price;
        this.selling_price=selling_price;
        this.product_name=product_name;
        this.product_brand=product_brand;
        this.capacity=capacity;
        this.booked_items_count=booked_items_count;

    }

    public void setNo_of_items(String no_of_items) {
        this.no_of_items = no_of_items;
    }

    public String getNo_of_items() {
        return no_of_items;
    }

    public void setBooked_items_count(String booked_items_count) {
        this.booked_items_count = booked_items_count;
    }
    public String getBooked_items_count() {
        return booked_items_count;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public String getImageView() {
        return imageView;
    }

    public String getActual_price() {
        return actual_price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public String getProduct_name() {
        return product_name;
    }
    public void addIntoCart()
    {
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("user_aravind").child("cart").child("Item_" + getProduct_name());
            databaseReference.child("name").setValue(getProduct_name());
            databaseReference.child("brand_name").setValue(getProduct_brand());
            databaseReference.child("link").setValue(getImageView());
            databaseReference.child("selling_price").setValue(getSelling_price());
            databaseReference.child("actual_price").setValue(getActual_price());
            databaseReference.child("booked_items").setValue(getBooked_items_count());

        }
        catch (Exception e)
        {
            Log.d("fuck",""+e.getMessage());
        }

    }
//    public void decreaseItemCount()
//    {
//        try {
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Items").child("Item_" + getProduct_name()).child("capacity");
//            databaseReference.setValue(getCapacity());
//        }
//        catch (Exception e)
//        {
//            Log.d("fuck",""+e.getMessage());
//
//        }
//    }


}
