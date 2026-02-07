package com.example.order_service.controller;
//import com.example.order_service.dto.ApiResponse;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.OrderRequestDTO;
import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.dto.OrderStatusUpdateRequestDTO;
import com.example.order_service.dto.OrderWithUserResponseDTO;
import com.example.order_service.dto.OrderStatusHistoryResponseDTO;
import com.example.order_service.model.OrderStatus;
import com.example.order_service.service.OrderService;
import com.example.user_service.dto.UserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // CREATE ORDER
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(
    		@Valid @RequestBody OrderRequestDTO dto) {

        OrderResponseDTO order = orderService.createOrder(dto);

        ApiResponse<OrderResponseDTO> response =
                new ApiResponse<>(
                        "Order created successfully",
                        201,
                        order
                );

        return ResponseEntity.status(201).body(response);
    }

    // GET ORDER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(
            @PathVariable Long id) 
    {

        OrderResponseDTO order = orderService.getOrderById(id);

        ApiResponse<OrderResponseDTO> response =
                new ApiResponse<>(
                        "Order fetched successfully",
                        200,
                        order
                );

        return ResponseEntity.ok(response);
    }


    // GET ORDER WITH USER DETAILS
    @GetMapping("/{id}/with-user")
    public ResponseEntity<ApiResponse<OrderWithUserResponseDTO>> getOrderWithUser(
            @PathVariable Long id) 
    {

        OrderWithUserResponseDTO data = orderService.getOrderWithUser(id);

        ApiResponse<OrderWithUserResponseDTO> response =
                new ApiResponse<>
        		(
                		"SUCCESS", 
                		200,
                		data
                );

        return ResponseEntity.ok(response);
    }
    
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusUpdateRequestDTO dto) 
    {

        OrderResponseDTO order =
                orderService.updateOrderStatus(id, dto.getStatus());

        return ResponseEntity.ok(
                new ApiResponse<>
                (
                		"Order status updated",
                		200, 
                		order
                )
        );
    }
    
    @GetMapping("/{id}/status-history")
    public ResponseEntity<ApiResponse<List<OrderStatusHistoryResponseDTO>>> history(
            @PathVariable Long id) 
    {
        return ResponseEntity.ok(
                new ApiResponse<>
                (
                        "Order status history",
                        200,
                        orderService.getOrderStatusHistory(id)
                )
        );
    }



    // GET ALL ORDERS (PAGINATED)
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<OrderResponseDTO>>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<OrderResponseDTO> orders = orderService.getOrders(page, size);

        ApiResponse<Page<OrderResponseDTO>> response =
                new ApiResponse<Page<OrderResponseDTO>>(
                        "Orders fetched successfully",
                        200,
                        orders
                );

        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        orderService.deleteOrder(id);

        ApiResponse<Void> response =
                new ApiResponse<>(
                        "Order deleted successfully",
                        200,
                        null
                );

        return ResponseEntity.ok(response);
    }
    
    
    @PatchMapping("restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restoreDelete(@PathVariable Long id) {

        orderService.restoreOrder(id);

        ApiResponse<Void> response =
                new ApiResponse<>(
                        "Order restored successfully",
                        200,
                        null
                );

        return ResponseEntity.ok(response);
    }
    
    
 // Full update
    @PutMapping("/{id}")  
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrder(
            @PathVariable Long id,
            @RequestBody OrderRequestDTO dto) {

        OrderResponseDTO updatedOrder = orderService.updateOrder(id, dto);
        return ResponseEntity.ok(new ApiResponse<>
        (
        		"Order updated",
        		200, 
        		updatedOrder
        ));
    }

    
    //cancel order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponseDTO>>cancelOrder(
    		@PathVariable Long id) {
    	
    	OrderResponseDTO cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(new ApiResponse<>
        (
        		"Order Cancelled",
        		200, 
        		cancelledOrder
        ));
    }

}
