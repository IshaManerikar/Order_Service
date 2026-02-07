package com.example.order_service.service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.example.order_service.exception.InvalidOrderStatusException;
import com.example.order_service.exception.OrderNotFoundException;
import com.example.order_service.dto.ApiResponse;
import com.example.order_service.dto.OrderRequestDTO;
import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.dto.OrderStatusHistoryResponseDTO;
import com.example.order_service.dto.OrderWithUserResponseDTO;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderStatus;
import com.example.order_service.model.OrderStatusHistory;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.repository.OrderStatusHistoryRepository;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.exception.UserNotFoundException;


import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import jakarta.transaction.*;


@Service
@Transactional
public class OrderServiceImpl implements OrderService{
	@Autowired
	private WebClient webClient;

	@Autowired
    private OrderRepository orderRepository;
	
	@Autowired
	private OrderStatusHistoryRepository orderStatusHistoryRepo;


	@Override
	public OrderResponseDTO createOrder(OrderRequestDTO dto) {
		UserResponseDTO user = webClient
		        .get()
		        .uri("http://localhost:9091/users/{id}", dto.getUserId())
		        .retrieve()
		        .bodyToMono(UserResponseDTO.class)
		        .timeout(Duration.ofSeconds(3))
		        .block();


		Order order = new Order();
		order.setUserId(dto.getUserId());
		order.setProductName(dto.getProductName());
		order.setQuantity(dto.getQuantity());
		order.setPrice(dto.getPrice());
		order.setStatus(OrderStatus.CREATED);
		
		Order savedOrder = orderRepository.save(order);
		
		OrderResponseDTO response = new OrderResponseDTO();
	        response.setId(savedOrder.getId());
	        response.setUserId(savedOrder.getUserId());
	        response.setProductName(savedOrder.getProductName());
	        response.setQuantity(savedOrder.getQuantity());
	        response.setPrice(savedOrder.getPrice());
	        response.setStatus(savedOrder.getStatus().name());
	        response.setCreatedAt(savedOrder.getCreatedAt());
	        response.setUpdatedAt(savedOrder.getUpdatedAt());


	        return response;		
	}
	
	@Override
	public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {
	    Order existingOrder = orderRepository.findByIdAndDeletedFalse(id)
	            .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + id));

	    existingOrder.setUserId(dto.getUserId());
	    existingOrder.setProductName(dto.getProductName());
	    existingOrder.setQuantity(dto.getQuantity());
	    existingOrder.setPrice(dto.getPrice());
	    // status remains unchanged

	    Order savedOrder = orderRepository.save(existingOrder);
	    return mapToResponse(savedOrder);
	}
	
	@Override
	public OrderWithUserResponseDTO getOrderWithUser(Long id) {

	    Order order = orderRepository.findById(id)
	            .orElseThrow(() ->
	                    new OrderNotFoundException("Order not found with id " + id));

	    ApiResponse<UserResponseDTO> userResponse =
	            webClient.get()
	                    .uri("http://localhost:9091/users/{id}", order.getUserId())
	                    .retrieve()
	                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserResponseDTO>>() {})
	                    .block();

	    UserResponseDTO user = userResponse.getData();
	    
	    OrderWithUserResponseDTO response = new OrderWithUserResponseDTO();
	    response.setOrderId(order.getId());
	    response.setProductName(order.getProductName());
	    response.setQuantity(order.getQuantity());
	    response.setPrice(order.getPrice());
	    response.setUser(user);

	    return response;

	}

	@Override
	public OrderResponseDTO getOrderById(Long id) {

	    Order order = orderRepository.findByIdAndDeletedFalse(id)
	            .orElseThrow(() ->
	                    new OrderNotFoundException("Order not found with id " + id));

	    return mapToResponse(order);
	}

	
	@Override
    public List<OrderResponseDTO> getAllOrders() {

        return orderRepository.findAllByDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
	

	
	@Override
	public Page<OrderResponseDTO> getOrders(int page, int size) {

	    PageRequest pageable = PageRequest.of(page, size);

	    return orderRepository.findAllByDeletedFalse(pageable)
	            .map(this::mapToResponse);
	}

    
	@Override
	public void deleteOrder(Long id) {
	    Order order = orderRepository.findByIdAndDeletedFalse(id)
	            .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + id));

	    order.setDeleted(true);  // Soft delete
	    orderRepository.save(order);
	}

	@Override
	public OrderResponseDTO restoreOrder(Long id) {
	    Order order = orderRepository.findByIdAndDeletedTrue(id)
	            .orElseThrow(() -> new OrderNotFoundException(" Deleted order not found with id " + id));

	    order.setDeleted(false);  // Soft delete
	    orderRepository.save(order);
	    return mapToResponse(order);
	}


    
	@Override
	public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {

	    Order order = orderRepository.findByIdAndDeletedFalse(orderId)
	            .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + orderId));

	    order.changeStatus(newStatus);

	    orderRepository.save(order);
	    return mapToResponse(order);
	}


	@Override
	public List<OrderStatusHistoryResponseDTO> getOrderStatusHistory(Long orderId) {
	    if (!orderRepository.existsByIdAndDeletedFalse(orderId)) {
	        throw new OrderNotFoundException("Order not found with id " + orderId);
	    }

	    return orderStatusHistoryRepo
	            .findByOrderIdOrderByStatusChangedAtAsc(orderId)
	            .stream()
	            .map(h -> {
	                OrderStatusHistoryResponseDTO dto = new OrderStatusHistoryResponseDTO();
	                dto.setOldStatus(h.getOldStatus());
	                dto.setNewStatus(h.getNewStatus());
	                dto.setStatusChangedAt(h.getStatusChangedAt());
	                dto.setChangedBy(h.getChangedBy());
	                return dto;
	            })
	            .toList();
	}


	@Override 
	public OrderResponseDTO cancelOrder(Long orderId) {

	    Order order = orderRepository.findByIdAndDeletedFalse(orderId)
	            .orElseThrow(() ->
	                new OrderNotFoundException("Order not found with id " + orderId)
	            );

	    order.changeStatus(OrderStatus.CANCELLED);

	    Order saved = orderRepository.save(order);

	    return mapToResponse(saved);
	}

    

    
	private OrderResponseDTO mapToResponse(Order order) {
	    OrderResponseDTO dto = new OrderResponseDTO();
	    dto.setId(order.getId());
	    dto.setUserId(order.getUserId());
	    dto.setProductName(order.getProductName());
	    dto.setQuantity(order.getQuantity());
	    dto.setPrice(order.getPrice());
	    dto.setStatus(order.getStatus().name());
	    dto.setCreatedAt(order.getCreatedAt());
	    dto.setUpdatedAt(order.getUpdatedAt());
	    return dto;
	}
	
	
	public Page<OrderResponseDTO> getOrdersByUser(Long userId, int page, int size) {

	    Pageable pageable = PageRequest.of(page, size);

	    return orderRepository
	            .findByUserIdAndDeletedFalse(userId, pageable)
	            .map(this::mapToResponse);
	}


	
			
}
