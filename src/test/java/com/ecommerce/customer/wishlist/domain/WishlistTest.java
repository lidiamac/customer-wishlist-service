package com.ecommerce.customer.wishlist.domain;


import com.ecommerce.customer.wishlist.domain.exceptions.ProductAlreadyExistsException;
import com.ecommerce.customer.wishlist.domain.exceptions.WishlistLimitExceededException;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class WishlistTest {

    private final String CUSTOMER_ID = "65b8c387b9e7a20c3a8d1f2e";
    private final Long PRODUCT_ID = 1579758L;


    @Test
    void givenEmptyWishlist_whenAddProduct_thenProductIsAdded() {
        var wishlist = new Wishlist(CUSTOMER_ID);

        wishlist.addProduct(new ProductItem(PRODUCT_ID));

        assertEquals(1, wishlist.getItems().size());
        assertEquals(PRODUCT_ID, wishlist.getItems().get(0).productId());
        assertNotNull(wishlist.getUpdatedAt());
    }

    @Test
    void givenWishlistWithSameProduct_whenAddProduct_thenThrowAlreadyExistsException() {
        var wishlist = new Wishlist(CUSTOMER_ID);
        wishlist.addProduct(new ProductItem(PRODUCT_ID));

        assertThrows(ProductAlreadyExistsException.class, () ->
                wishlist.addProduct(new ProductItem(PRODUCT_ID))
        );
    }

    @Test
    void givenWishlistAtMaxCapacity_whenAddProduct_thenThrowLimitExceededException() {
        var wishlist = new Wishlist(CUSTOMER_ID);

        IntStream.range(0, 20).forEach(i ->
                wishlist.addProduct(new ProductItem(1000L + i))
        );

        assertThrows(WishlistLimitExceededException.class, () ->
                wishlist.addProduct(new ProductItem(PRODUCT_ID))
        );
    }
}
