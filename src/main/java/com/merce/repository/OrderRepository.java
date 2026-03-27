package com.merce.repository;

import com.merce.model.db.Order;
import com.merce.model.db.enums.OrderStatus;
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
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
    List<Order> findByUserId(UUID userId);
    Page<Order> findByUser(User user, Pageable pageable);

    List<Order> findByStatus(OrderStatus status);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    long countByStatus(OrderStatus status);

    List<Order> findByUserAndStatus(User user, OrderStatus status);

    List<Order> findByCreatedAtBetween(Instant start, Instant end);
    Page<Order> findByCreatedAtBetween(Instant start, Instant end, Pageable pageable);

    long countByUser(User user);

    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.user = :user")
    List<Order> findByUserWithItems(@Param("user") User user);

    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.status = :status")
    List<Order> findByStatusWithItems(@Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") UUID id);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.createdAt BETWEEN :start AND :end AND o.status NOT IN :excludedStatuses")
    BigDecimal getTotalRevenueByDateRange(@Param("start") Instant start, @Param("end") Instant end, @Param("excludedStatuses") List<OrderStatus> excludedStatuses);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status NOT IN :excludedStatuses")
    BigDecimal getTotalRevenue(@Param("excludedStatuses") List<OrderStatus> excludedStatuses);

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> getCountByStatus();

    @Query("SELECT DATE(o.createdAt), COUNT(o), SUM(o.totalPrice) FROM Order o GROUP BY DATE(o.createdAt)")
    List<Object[]> getDailyOrderStatistics();

    @Query("SELECT o FROM Order o WHERE o.user.email LIKE %:email%")
    Page<Order> findByUserEmailContaining(@Param("email") String email, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt < :threshold")
    List<Order> findOldOrdersByStatus(@Param("status") OrderStatus status, @Param("threshold") Instant threshold);
}