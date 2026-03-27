package com.merce.repository;

import com.merce.model.db.Order;
import com.merce.model.db.OrderItem;
import com.merce.model.db.enums.OrderStatus;
import com.merce.model.db.Product;
import com.merce.model.db.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findByOrder(Order order);
    List<OrderItem> findByOrderId(UUID orderId);
    Page<OrderItem> findByOrder(Order order, Pageable pageable);

    List<OrderItem> findByProduct(Product product);
    List<OrderItem> findByProductId(UUID productId);
    Page<OrderItem> findByProduct(Product product, Pageable pageable);

    long countByOrder(Order order);
    long countByProduct(Product product);

    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.order JOIN FETCH oi.product WHERE oi.order = :order")
    List<OrderItem> findByOrderWithDetails(@Param("order") Order order);

    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.order JOIN FETCH oi.product WHERE oi.id = :id")
    Optional<OrderItem> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT SUM(oi.priceAtPurchase * oi.quantity) FROM OrderItem oi WHERE oi.product = :product")
    BigDecimal getTotalRevenueByProduct(@Param("product") Product product);

    @Query("SELECT SUM(oi.priceAtPurchase * oi.quantity) FROM OrderItem oi WHERE oi.order.createdAt BETWEEN :start AND :end")
    BigDecimal getTotalRevenueByDateRange(@Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT SUM(oi.priceAtPurchase * oi.quantity) FROM OrderItem oi WHERE oi.order.status NOT IN :excludedStatuses")
    BigDecimal getTotalRevenue(@Param("excludedStatuses") List<OrderStatus> excludedStatuses);

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product = :product")
    Long getTotalQuantitySoldByProduct(@Param("product") Product product);

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.order.createdAt BETWEEN :start AND :end")
    Long getTotalQuantitySoldByDateRange(@Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT oi.product, SUM(oi.quantity) as totalQuantity FROM OrderItem oi GROUP BY oi.product ORDER BY totalQuantity DESC")
    Page<Object[]> findTopSellingProducts(Pageable pageable);

    @Query("SELECT oi.product.id, oi.product.name, SUM(oi.quantity), SUM(oi.priceAtPurchase * oi.quantity) FROM OrderItem oi GROUP BY oi.product")
    List<Object[]> getProductSalesStatistics();

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.status = :status")
    List<OrderItem> findByOrderStatus(@Param("status") OrderStatus status);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.user = :user")
    List<OrderItem> findByOrderUser(@Param("user") User user);

    @Query("SELECT AVG(oi.priceAtPurchase) FROM OrderItem oi WHERE oi.product = :product")
    BigDecimal getAveragePriceByProduct(@Param("product") Product product);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order = :order AND oi.product = :product")
    List<OrderItem> findByOrderAndProduct(@Param("order") Order order, @Param("product") Product product);

    void deleteByOrder(Order order);

    void deleteByProduct(Product product);
}