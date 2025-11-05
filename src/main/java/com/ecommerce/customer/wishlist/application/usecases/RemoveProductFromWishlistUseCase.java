package com.ecommerce.customer.wishlist.application.usecases;

import com.ecommerce.customer.wishlist.application.repository.WishlistRepository;
import com.ecommerce.customer.wishlist.domain.exceptions.WishListNotFoundException;
import org.springframework.stereotype.Service;

import static com.ecommerce.customer.wishlist.domain.constants.WishlistMessages.WISHLIST_NOT_FOUND;
import static java.lang.String.format;


@Service
public class RemoveProductFromWishlistUseCase {
    private final WishlistRepository wishlistRepository;

    public RemoveProductFromWishlistUseCase(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public void execute(String customerId, Long productId) {
        var wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishListNotFoundException(format(WISHLIST_NOT_FOUND, customerId)));

        wishlist.removeProduct(productId);
        wishlistRepository.save(wishlist);
    }
}

