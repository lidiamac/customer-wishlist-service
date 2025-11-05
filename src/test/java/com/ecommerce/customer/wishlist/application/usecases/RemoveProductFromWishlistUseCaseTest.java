package com.ecommerce.customer.wishlist.application.usecases;

import com.ecommerce.customer.wishlist.application.repository.WishlistRepository;
import com.ecommerce.customer.wishlist.domain.Wishlist;
import com.ecommerce.customer.wishlist.domain.exceptions.ProductNotFoundException;
import com.ecommerce.customer.wishlist.domain.exceptions.WishListNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveProductFromWishlistUseCaseTest {

    @InjectMocks
    private RemoveProductFromWishlistUseCase useCase;

    @Mock
    private WishlistRepository wishlistRepository;

    private final String CUSTOMER_ID = "65b8c387b9e7a20c3a8d1f2e";
    private final Long PRODUCT_ID = 1579758L;
    private final String ANY_ERROR_MESSAGE = "ERROR";


    @Test
    void givenExistingWishlistAndProduct_whenRemoveProduct_thenProductIsRemoved() {
        var wishlist = mock(Wishlist.class);

        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        useCase.execute(CUSTOMER_ID, PRODUCT_ID);

        verify(wishlist, times(1)).removeProduct(PRODUCT_ID);
        verify(wishlistRepository, times(1)).save(wishlist);
    }


    @Test
    void givenExistingWishlistAndNonExistingProduct_whenRemoveProduct_thenThrowProductNotFoundException() {
        var wishlist = mock(Wishlist.class);

        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));
        doThrow(new ProductNotFoundException(ANY_ERROR_MESSAGE)).when(wishlist).removeProduct(PRODUCT_ID);

        assertThrows(ProductNotFoundException.class, () ->
                useCase.execute(CUSTOMER_ID, PRODUCT_ID)
        );

        verify(wishlist, times(1)).removeProduct(PRODUCT_ID);
        verify(wishlistRepository, never()).save(any());
    }


    @Test
    void givenNonExistingWishlist_whenRemoveProduct_thenThrowWishListNotFoundException() {

        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(WishListNotFoundException.class, () ->
                useCase.execute(CUSTOMER_ID, PRODUCT_ID)
        );

        verify(wishlistRepository, never()).save(any());
    }

}