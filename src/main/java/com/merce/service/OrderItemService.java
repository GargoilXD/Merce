package com.merce.service;

import com.merce.model.db.Order;
import com.merce.model.db.OrderItem;
import com.merce.model.db.Product;
import com.merce.model.db.User;
import com.merce.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class OrderItemService {
    @Autowired
    OrderItemRepository orderItemRepository;

    public List<OrderItem> getOrderItems(Order order) {
        return orderItemRepository.findByOrderWithDetails(order);
    }

    public Map<String, Object> getProductAnalytics(Product product) {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalRevenue", orderItemRepository.getTotalRevenueByProduct(product));
        analytics.put("totalQuantitySold", orderItemRepository.getTotalQuantitySoldByProduct(product));
        analytics.put("averagePrice", orderItemRepository.getAveragePriceByProduct(product));
        return analytics;
    }

    public Page<Object[]> getTopSellingProducts(Pageable pageable) {
        return orderItemRepository.findTopSellingProducts(pageable);
    }

    public BigDecimal getRevenueReport(Instant start, Instant end) {
        return orderItemRepository.getTotalRevenueByDateRange(start, end);
    }

    public boolean isProductInOrder(Order order, Product product) {
        return !orderItemRepository.findByOrderAndProduct(order, product).isEmpty();
    }

    public List<OrderItem> getUserOrderItems(User user) {
        return orderItemRepository.findByOrderUser(user);
    }
}
