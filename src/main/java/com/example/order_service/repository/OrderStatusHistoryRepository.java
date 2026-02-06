package com.example.order_service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.order_service.model.OrderStatusHistory;

public interface OrderStatusHistoryRepository
        extends JpaRepository<OrderStatusHistory, Long> {

    List<OrderStatusHistory> findByOrderIdOrderByStatusChangedAtAsc(Long orderId);
}
