package com.ecommerce.customer.wishlist.application.usecases;

import com.ecommerce.customer.wishlist.application.dto.response.WishlistResponse;
import com.ecommerce.customer.wishlist.application.repository.WishlistRepository;
import com.ecommerce.customer.wishlist.domain.exceptions.ClientNotFoundException;
import org.springframework.stereotype.Service;

import static com.ecommerce.customer.wishlist.domain.constants.WishlistMessages.CLIENT_NOT_FOUND;

@Service
public class RetrieveAllWishlistUseCase {

    private final WishlistRepository wishlistRepository;

    public RetrieveAllWishlistUseCase(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public WishlistResponse execute(String customerId) {
        var wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(()-> new ClientNotFoundException(String.format(CLIENT_NOT_FOUND, customerId)));

        return WishlistResponse.fromDomain(wishlist);
    }
}
