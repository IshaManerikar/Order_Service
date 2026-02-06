package com.example.order_service.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.order_service.exception.InvalidOrderStatusException;

//import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long userId;

	private String productName;

	private int quantity;

	private double price;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	public long getId() {
		return id;
	}

	public long getUserId() {
		return userId;
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

	public OrderStatus getStatus() {
		return status;
	}

	// setter
	public void setId(long id) {
		this.id = id;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderStatusHistory> historyList = new ArrayList<>();

	public List<OrderStatusHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<OrderStatusHistory> historyList) {
		this.historyList = historyList;
	}

	public void changeStatus(OrderStatus newStatus) {

		if (this.status == newStatus) {
			throw new InvalidOrderStatusException("Order is already in status " + newStatus);
		}

		if (!this.status.canTransitionTo(newStatus)) {
			throw new InvalidOrderStatusException("Cannot change order status from " + status + " to " + newStatus);
		}

		OrderStatusHistory history = new OrderStatusHistory();
		history.setOrder(this);
		history.setOldStatus(this.status);
		history.setNewStatus(newStatus);
		history.setStatusChangedAt(LocalDateTime.now());

		this.historyList.add(history);
		this.status = newStatus;
	}

}
