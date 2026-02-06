package com.example.order_service.dto;


import com.example.order_service.model.OrderStatus;

import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateRequestDTO {
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}



