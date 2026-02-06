package com.example.order_service.dto;

import java.time.LocalDateTime;
import com.example.order_service.model.OrderStatus;

public class OrderStatusHistoryResponseDTO {

    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private LocalDateTime statusChangedAt;
    private String changedBy;   // <-- add this

    // getters
    public OrderStatus getOldStatus() {
        return oldStatus;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }

    public LocalDateTime getStatusChangedAt() {
        return statusChangedAt;
    }

    // setters
    public void setOldStatus(OrderStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public void setNewStatus(OrderStatus newStatus) {
        this.newStatus = newStatus;
    }

    public void setStatusChangedAt(LocalDateTime statusChangedAt) {
        this.statusChangedAt = statusChangedAt;
    }
    
    public String getChangedBy() {   // <-- add getter
        return changedBy;
    }

    public void setChangedBy(String changedBy) {  // <-- add setter
        this.changedBy = changedBy;
    }
}
