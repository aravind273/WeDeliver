package com.example.wedeliver;

import java.io.Serializable;

class orderDetails implements Serializable {
    long orderId,total_amount,saved_amount;
    String order_Details,status,time,date;
    orderDetails()
    {

    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOrder_Details(String order_Details) {
        this.order_Details = order_Details;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setSaved_amount(long saved_amount) {
        this.saved_amount = saved_amount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTotal_amount(long total_amount) {
        this.total_amount = total_amount;
    }

    public long getSaved_amount() {
        return saved_amount;
    }

    public long getTotal_amount() {
        return total_amount;
    }

    public String getDate() {
        return date;
    }

    public String getOrder_Details() {
        return order_Details;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public long getOrderId() {
        return orderId;
    }
}
