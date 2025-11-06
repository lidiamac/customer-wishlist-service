package com.ecommerce.customer.wishlist.application.dto.response;

import com.ecommerce.customer.wishlist.domain.ProductItem;
import com.ecommerce.customer.wishlist.domain.Wishlist;

import java.time.LocalDateTime;
import java.util.List;

public record WishlistResponse(String customerId,
                               List<ProductItem> items,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt

){
    public static WishlistResponse fromDomain(Wishlist domain) {
        return new WishlistResponse(
                domain.getCustomerId(),
                domain.getItems(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
