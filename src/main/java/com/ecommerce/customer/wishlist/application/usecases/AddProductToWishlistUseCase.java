package com.ecommerce.customer.wishlist.application.usecases;

import com.ecommerce.customer.wishlist.application.dto.request.AddProductRequest;
import com.ecommerce.customer.wishlist.application.dto.response.ProductResponse;
import com.ecommerce.customer.wishlist.application.repository.WishlistRepository;
import com.ecommerce.customer.wishlist.domain.ProductItem;
import com.ecommerce.customer.wishlist.domain.Wishlist;
import org.springframework.stereotype.Service;

@Service
public class AddProductToWishlistUseCase {

    private final WishlistRepository wishlistRepository;

    public AddProductToWishlistUseCase(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }


    public ProductResponse execute(String customerId, AddProductRequest request) {
            var wishlist = wishlistRepository.findByCustomerId(customerId)
                    .orElseGet(() -> new Wishlist(customerId));

            wishlist.addProduct(new ProductItem(request.productId()));
            wishlistRepository.save(wishlist);
            return new ProductResponse(request.productId());
        }
    }
