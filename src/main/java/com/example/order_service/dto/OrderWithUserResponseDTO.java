package com.example.order_service.dto;

import com.example.user_service.dto.UserResponseDTO;

public class OrderWithUserResponseDTO {

    private Long orderId;
    private String productName;
    private int quantity;
    private double price;
    private UserResponseDTO user;

    // ✅ GETTERS

    public Long getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    // ✅ SETTERS

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
}
