package com.merce.service;

import com.merce.model.db.Product;
import com.merce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchByNameOrDescription(keyword, pageable);
    }

    public Page<Product> filterProducts(BigDecimal min, BigDecimal max, Pageable pageable) {
        return productRepository.findAvailableProductsInPriceRange(min, max, pageable);
    }

    public boolean isProductNameTaken(String name) {
        return productRepository.existsByNameIgnoringCase(name);
    }

    public List<Product> getLowStockAlerts() {
        return productRepository.findLowStockProducts(10);
    }

    public List<Product> getInStockProducts() {
        return productRepository.findByStockQuantityGreaterThan(0);
    }
}
