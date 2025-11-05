package com.ecommerce.customer.wishlist.domain.exceptions;

public class WishListNotFoundException extends RuntimeException {
    public WishListNotFoundException(String message) {
        super(message);
    }
}
