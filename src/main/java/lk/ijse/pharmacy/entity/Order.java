package lk.ijse.pharmacy.entity;

import java.sql.Time;
import java.util.Date;

public class Order {
    private int orderId;
    private int customerId;
    private int userId;
    private double total;
    private Date orderDate;
    private Time orderTime;

    public Order() {
    }

    public Order(int orderId, int customerId, int userId, double total, Date orderDate, Time orderTime) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.userId = userId;
        this.total = total;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Time getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Time orderTime) {
        this.orderTime = orderTime;
    }
}
