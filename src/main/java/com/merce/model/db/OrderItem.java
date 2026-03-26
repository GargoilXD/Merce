package com.merce.model.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = {"order", "product"})
@Entity(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Price At Purchase is required") @Positive(message = "Price At Purchase must be positive")
    @Column(name = "price_at_purchase", nullable = false, precision = 19, scale = 4)
    private BigDecimal priceAtPurchase;

    @NotNull(message = "Quantity is required") @Min(value = 1, message = "quantity minimum is 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public static OrderItem create(Order order, Product product, int quantity, BigDecimal priceAtPurchase) {
        OrderItem item = new OrderItem();
        item.order = order;
        item.product = product;
        item.quantity = quantity;
        item.priceAtPurchase = priceAtPurchase;
        return item;
    }

    public BigDecimal getLineTotal() {
        return this.priceAtPurchase.multiply(BigDecimal.valueOf(this.quantity));
    }
}