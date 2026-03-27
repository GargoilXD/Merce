package com.merce.service;

import com.merce.model.db.CartItem;
import com.merce.model.db.Product;
import com.merce.model.db.User;
import com.merce.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> getUserCart(User user) {
        return cartItemRepository.findByUserWithProductDetailed(user);
    }

    public BigDecimal getCartTotal(User user) {
        return cartItemRepository.getCartTotalByUser(user);
    }

    public CartItem addToCart(User user, Product product, int quantity) {
        Optional<CartItem> existing = cartItemRepository.findByUserAndProduct(user, product);

        if (existing.isPresent()) {
            cartItemRepository.updateQuantity(user, product, existing.get().getQuantity() + quantity);
            return existing.get();
        } else {
            CartItem item = CartItem.create(user, product, quantity);
            return cartItemRepository.save(item);
        }
    }

    public void updateQuantity(User user, Product product, int quantity) {
        if (quantity < 1) {
            cartItemRepository.deleteByUserAndProduct(user, product);
        } else {
            cartItemRepository.updateQuantity(user, product, quantity);
        }
    }

    public void removeFromCart(User user, Product product) {
        cartItemRepository.deleteByUserAndProduct(user, product);
    }

    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}
