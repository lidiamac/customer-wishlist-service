package com.ecommerce.customer.wishlist.application.usecases;


import com.ecommerce.customer.wishlist.application.repository.WishlistRepository;
import com.ecommerce.customer.wishlist.domain.Wishlist;
import com.ecommerce.customer.wishlist.domain.exceptions.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ecommerce.customer.wishlist.domain.constants.WishlistMessages.CLIENT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveAllWishlistUseCaseTest {

    @InjectMocks
    private RetrieveAllWishlistUseCase useCase;

    @Mock
    private WishlistRepository wishlistRepository;

    private final String CUSTOMER_ID = "65b8c387b9e7a20c3a8d1f2e";


    @Test
    void givenExistingWishlist_whenRetrieveAll_thenReturnWishlistResponse() {
        var wishlist = new Wishlist(CUSTOMER_ID);

        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        var result = useCase.execute(CUSTOMER_ID);

        assertNotNull(result);
        assertEquals(CUSTOMER_ID, result.customerId());

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
    }


    @Test
    void givenNonExistingWishlist_whenRetrieveAll_thenThrowClientNotFoundException() {
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        var exception = assertThrows(ClientNotFoundException.class, () ->
                useCase.execute(CUSTOMER_ID));

        assertTrue(exception.getMessage().contains(String.format(CLIENT_NOT_FOUND, CUSTOMER_ID)));

        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
    }
}
