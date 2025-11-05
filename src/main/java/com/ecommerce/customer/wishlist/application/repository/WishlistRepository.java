package com.ecommerce.customer.wishlist.application.repository;

import com.ecommerce.customer.wishlist.domain.Wishlist;

import java.util.Optional;

public interface WishlistRepository {
    Optional<Wishlist> findByCustomerId(String customerId);
    Wishlist save(Wishlist wishlist);
    Boolean existsByCustomerIdAndItemsProductId(String customerId, Long productId);

}
