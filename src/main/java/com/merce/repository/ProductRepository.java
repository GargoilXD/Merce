package com.merce.repository;

import com.merce.model.db.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByName(String name);
    boolean existsByNameIgnoringCase(String name);

    List<Product> findByStockQuantityGreaterThan(int quantity);
    List<Product> findByStockQuantity(int quantity);
    long countByStockQuantityGreaterThan(int quantity);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
    List<Product> findByPriceLessThan(BigDecimal price);
    List<Product> findByPriceGreaterThan(BigDecimal price);

    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Product> searchByNameOrDescription(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0 AND p.price BETWEEN :min AND :max")
    Page<Product> findAvailableProductsInPriceRange(@Param("min") BigDecimal min, @Param("max") BigDecimal max, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity BETWEEN 1 AND :threshold")
    List<Product> findLowStockProducts(@Param("threshold") int threshold);

    Page<Product> findByOrderByPriceAsc(Pageable pageable);
    Page<Product> findByOrderByPriceDesc(Pageable pageable);

    @EntityGraph(attributePaths = {})
    List<Product> findAllWithDetails();
}