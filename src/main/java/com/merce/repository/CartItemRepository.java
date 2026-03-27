package com.merce.repository;

import com.merce.model.db.CartItem;
import com.merce.model.db.Product;
import com.merce.model.db.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByUser(User user);
    List<CartItem> findByUserId(UUID userId);
    Page<CartItem> findByUser(User user, Pageable pageable);

    Optional<CartItem> findByUserAndProduct(User user, Product product);
    Optional<CartItem> findByUserIdAndProductId(UUID userId, UUID productId);

    boolean existsByUserAndProduct(User user, Product product);
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);

    long countByUser(User user);
    long countByUserId(UUID userId);

    @EntityGraph(attributePaths = {"product"})
    List<CartItem> findByUserWithProduct(User user);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.user = :user")
    List<CartItem> findByUserWithProductDetailed(@Param("user") User user);

    @Query("SELECT SUM(ci.product.price * ci.quantity) FROM CartItem ci WHERE ci.user = :user")
    BigDecimal getCartTotalByUser(@Param("user") User user);

    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.user = :user")
    Long getTotalQuantityInCart(@Param("user") User user);

    @Query("SELECT ci FROM CartItem ci WHERE ci.user = :user AND ci.product.stockQuantity < ci.quantity")
    List<CartItem> findItemsWithInsufficientStock(@Param("user") User user);

    @Query("SELECT ci FROM CartItem ci WHERE ci.user = :user AND ci.product.stockQuantity = 0")
    List<CartItem> findItemsOutOfStock(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("UPDATE CartItem ci SET ci.quantity = :quantity WHERE ci.user = :user AND ci.product = :product")
    int updateQuantity(@Param("user") User user, @Param("product") Product product, @Param("quantity") int quantity);

    void deleteByUserAndProduct(User user, Product product);
    void deleteByUserIdAndProductId(UUID userId, UUID productId);
    void deleteByUser(User user);
    void deleteByUserId(UUID userId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.product = :product")
    List<CartItem> findByProduct(@Param("product") Product product);

    @Query("SELECT ci FROM CartItem ci WHERE ci.createdAt < :threshold")
    List<CartItem> findStaleCartItems(@Param("threshold") Instant threshold);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.createdAt < :threshold")
    int deleteStaleCartItems(@Param("threshold") Instant threshold);
}