package com.ecommerce.customer.wishlist.infraestructure.repository;

import com.ecommerce.customer.wishlist.infraestructure.repository.document.WishlistDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoWishlistRepository extends MongoRepository<WishlistDocument, String> {
    Optional<WishlistDocument> findByCustomerId(String customerId);
    boolean existsByCustomerIdAndItemsProductId(String customerId, Long productId);

}
