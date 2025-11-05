package com.ecommerce.customer.wishlist.domain;


import com.ecommerce.customer.wishlist.domain.exceptions.ProductAlreadyExistsException;
import com.ecommerce.customer.wishlist.domain.exceptions.ProductNotFoundException;
import com.ecommerce.customer.wishlist.domain.exceptions.WishlistLimitExceededException;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WishlistTest {

    private final String CUSTOMER_ID = "65b8c387b9e7a20c3a8d1f2e";
    private final Long PRODUCT_ID = 1579758L;
    private final Long ANOTHER_PRODUCT_ID = 8475945L;


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



    @Test
    void givenWishlistWithProduct_whenRemoveProduct_thenProductIsRemoved() {
        var wishlist = new Wishlist(CUSTOMER_ID);
        wishlist.addProduct(new ProductItem(PRODUCT_ID));

        wishlist.removeProduct(PRODUCT_ID);

        assertThat(wishlist.getItems()).isEmpty();
    }

    @Test
    void givenWishlistWithMultipleProducts_whenRemoveProduct_thenOnlyTargetProductIsRemoved() {
        var wishlist = new Wishlist(CUSTOMER_ID);
        wishlist.addProduct(new ProductItem(PRODUCT_ID));
        wishlist.addProduct(new ProductItem(ANOTHER_PRODUCT_ID));

        wishlist.removeProduct(PRODUCT_ID);

        assertThat(wishlist.getItems())
                .extracting(ProductItem::productId)
                .containsExactlyInAnyOrder(ANOTHER_PRODUCT_ID);
    }

    @Test
    void givenWishlistWithoutProduct_whenRemoveProduct_thenThrowProductNotFoundException() {
        var wishlist = new Wishlist(CUSTOMER_ID);
        wishlist.addProduct(new ProductItem(PRODUCT_ID));

        assertThrows(ProductNotFoundException.class, () ->
                wishlist.removeProduct(ANOTHER_PRODUCT_ID)
        );
    }

}
