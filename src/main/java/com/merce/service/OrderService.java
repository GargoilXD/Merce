package com.merce.service;

import com.merce.model.db.Order;
import com.merce.model.db.User;
import com.merce.model.db.enums.OrderStatus;
import com.merce.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Page<Order> getUserOrders(User user, int page, int size) {
        return orderRepository.findByUser(user, PageRequest.of(page, size));
    }

    public Order getOrderWithItems(UUID orderId) {
        return orderRepository.findByIdWithItems(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
    }

    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus(OrderStatus.PENDING);
    }

    public BigDecimal getRevenueReport(Instant start, Instant end) {
        return orderRepository.getTotalRevenueByDateRange(start, end, List.of(OrderStatus.CANCELLED, OrderStatus.REFUNDED));
    }

    public Map<OrderStatus, Long> getOrderStatusCounts() {
        return orderRepository.getCountByStatus().stream().collect(Collectors.toMap(row -> (OrderStatus) row[0], row -> (Long) row[1]));
    }

    public List<Order> getStalePendingOrders() {
        Instant threshold = Instant.now().minus(1, ChronoUnit.DAYS);
        return orderRepository.findOldOrdersByStatus(OrderStatus.PENDING, threshold);
    }
}
