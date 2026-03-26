package com.merce.model.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "products")
@ToString(exclude = {"description"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = false, length = 2048)
    String description;

    @Positive(message = "Price must be positive") @NotNull(message = "Price is required")
    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    BigDecimal price;

    @NotNull(message = "Stock Quantity is required") @PositiveOrZero(message = "Stock Quantity must be positive or zero")
    @Column(name = "stock_quantity", nullable = false)
    Integer stockQuantity;

    @NotBlank(message = "Image Url is required") @URL(message = "Invalid URL")
    @Column(name = "image_url", nullable = false)
    String imageUrl;

    public static Product create(String name, String description, BigDecimal price, int stock, String imageUrl) {
        Product product = new Product();
        product.name = name;
        product.description = description;
        product.price = price;
        product.stockQuantity = stock;
        product.imageUrl = imageUrl;
        return product;
    }

    public void reserveStock(@Min(1) int quantity) {
        if (this.stockQuantity < quantity) throw new IllegalStateException("Insufficient stock");
        this.stockQuantity -= quantity;
    }
}
