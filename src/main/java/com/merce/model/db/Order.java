package com.merce.model.db;

import com.merce.model.db.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "orders")
@Table(indexes = @Index(name = "idx_order_status", columnList = "status"))
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "OrderStatus is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @NotNull(message = "Total Price is required") @Positive(message = "Total Price must be positive")
    @Column(name = "total_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalPrice;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.PRIVATE)
    private List<OrderItem> items = new ArrayList<>();

    public static Order create(User user, OrderStatus status, BigDecimal totalPrice) {
        Order order = new Order();
        order.user = user;
        order.status = status;
        order.totalPrice = totalPrice;
        return order;
    }

    public void updateStatus(@NotNull OrderStatus newStatus) {
        boolean canTransition = switch (this.status) {
            case PENDING -> newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED;
            case CONFIRMED -> newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.CANCELLED;
            case PROCESSING -> newStatus == OrderStatus.SHIPPED || newStatus == OrderStatus.CANCELLED;
            case SHIPPED -> newStatus == OrderStatus.DELIVERED || newStatus == OrderStatus.RETURNED;
            case DELIVERED -> newStatus == OrderStatus.RETURNED || newStatus == OrderStatus.REFUNDED;
            case CANCELLED, REFUNDED, RETURNED -> false;
        };
        if (!canTransition) throw new IllegalStateException(String.format("Cannot transition from %s to %s", this.status, newStatus));
        this.status = newStatus;
    }

    public void addItem(@NotNull OrderItem item) {
        this.items.add(item);
        item.setOrder(this);
    }
}