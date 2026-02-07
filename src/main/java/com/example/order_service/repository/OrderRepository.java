package com.example.order_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.order_service.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndDeletedFalse(Long id);

    List<Order> findAllByDeletedFalse();
    Page<Order> findAllByDeletedFalse(Pageable pageable);
    boolean existsByIdAndDeletedFalse(Long id);

	Optional<Order> findByIdAndDeletedTrue(Long id);
	Page<Order> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

}
