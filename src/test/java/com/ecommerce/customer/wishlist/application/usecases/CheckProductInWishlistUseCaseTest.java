package com.ecommerce.customer.wishlist.application.usecases;


import com.ecommerce.customer.wishlist.application.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckProductInWishlistUseCaseTest {

    @InjectMocks
    private CheckProductInWishlistUseCase classUnderTest;

    @Mock
    private WishlistRepository wishlistRepository;

    private final String CUSTOMER_ID = "65b8c387b9e7a20c3a8d1f2e";
    private final Long PRODUCT_ID = 1579758L;

    @Test
    void givenProductExistsInWishlist_whenExecute_thenReturnTrue() {
        when(wishlistRepository.existsByCustomerIdAndItemsProductId(CUSTOMER_ID, PRODUCT_ID)).thenReturn(true);

        var result = classUnderTest.execute(CUSTOMER_ID, PRODUCT_ID);

        assertTrue(result);

        verify(wishlistRepository).existsByCustomerIdAndItemsProductId(CUSTOMER_ID, PRODUCT_ID);
    }

    @Test
    void givenProductDoesNotExistInWishlist_whenExecute_thenReturnFalse() {
        when(wishlistRepository.existsByCustomerIdAndItemsProductId(CUSTOMER_ID, PRODUCT_ID)).thenReturn(false);

        var result = classUnderTest.execute(CUSTOMER_ID, PRODUCT_ID);

        assertFalse(result);

        verify(wishlistRepository).existsByCustomerIdAndItemsProductId(CUSTOMER_ID, PRODUCT_ID);
    }
}