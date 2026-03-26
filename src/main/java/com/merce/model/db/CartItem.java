package com.merce.model.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "cart_items")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Quantity is required") @Min(value = 1, message = "quantity minimum is 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public static CartItem create(User user, Product product, int quantity) {
        CartItem item = new CartItem();
        item.user = user;
        item.product = product;
        item.quantity = quantity;
        return item;
    }

    public void addQuantity(@Min(1) int amount) {
        this.quantity += amount;
    }
}