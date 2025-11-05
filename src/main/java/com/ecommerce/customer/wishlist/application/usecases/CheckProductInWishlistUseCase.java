package com.ecommerce.customer.wishlist.application.usecases;

import com.ecommerce.customer.wishlist.application.repository.WishlistRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckProductInWishlistUseCase {

    private final WishlistRepository wishlistRepository;

    public CheckProductInWishlistUseCase(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public Boolean execute(String customerId, Long productId) {
        return wishlistRepository.existsByCustomerIdAndItemsProductId(customerId, productId);
    }


}
