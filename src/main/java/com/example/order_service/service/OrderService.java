package com.example.order_service.service;

import java.util.List;
import org.springframework.data.domain.Page;
import com.example.order_service.dto.OrderRequestDTO;
import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.dto.OrderStatusHistoryResponseDTO;
//import com.sun.tools.javac.util.List;
import com.example.order_service.dto.OrderWithUserResponseDTO;
import com.example.order_service.model.OrderStatus;
import com.example.order_service.model.OrderStatusHistory;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto);
    OrderResponseDTO getOrderById(Long id);
    OrderWithUserResponseDTO getOrderWithUser(Long id);
    List<OrderResponseDTO> getAllOrders();
    Page<OrderResponseDTO> getOrders(int page, int size);
//    OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto); 
    OrderResponseDTO updateOrderStatus(Long id, OrderStatus status); // update only status
//    List<OrderStatusHistory> findByOrderIdOrderByStatusChangedAtAsc(Long orderId);
    List<OrderStatusHistoryResponseDTO> getOrderStatusHistory(Long orderId);
    OrderResponseDTO restoreOrder(Long id);

    void deleteOrder(Long id);
	OrderResponseDTO cancelOrder(Long orderId);

}
