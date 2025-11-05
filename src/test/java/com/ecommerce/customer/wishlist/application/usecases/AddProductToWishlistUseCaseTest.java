package com.ecommerce.customer.wishlist.application.usecases;


import com.ecommerce.customer.wishlist.application.dto.request.AddProductRequest;
import com.ecommerce.customer.wishlist.application.repository.WishlistRepository;
import com.ecommerce.customer.wishlist.domain.ProductItem;
import com.ecommerce.customer.wishlist.domain.Wishlist;
import com.ecommerce.customer.wishlist.domain.exceptions.ProductAlreadyExistsException;
import com.ecommerce.customer.wishlist.domain.exceptions.WishlistLimitExceededException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.ecommerce.customer.wishlist.domain.constants.WishlistMessages.WISHLIST_LIMIT_EXCEEDED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProductToWishlistUseCaseTest {

    @InjectMocks
    private AddProductToWishlistUseCase useCase;

    @Mock
    private WishlistRepository wishlistRepository;

    private final String CUSTOMER_ID = "65b8c387b9e7a20c3a8d1f2e";
    private final Long PRODUCT_ID = 1579758L;
    private final Long ANOTHER_PRODUCT_ID = 8475945L;
    private final String ERROR_MESSAGE = "ERROR";




    @Test
    void givenCustomerWithoutWishlist_whenAddProduct_thenCreateWishlistAndAddProduct() {
        var request = new AddProductRequest(PRODUCT_ID);

        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());
        when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = useCase.execute(CUSTOMER_ID, request);

        assertTrue(result.isAdded());
        assertEquals(PRODUCT_ID, result.productId());

        verify(wishlistRepository).save(any(Wishlist.class));
    }


    @Test
    void givenExistingWishlist_whenAddProduct_thenAddAndSave() {
        var wishlist = new Wishlist(CUSTOMER_ID);
        wishlist.addProduct(new ProductItem(ANOTHER_PRODUCT_ID));
        var request = new AddProductRequest(PRODUCT_ID);

        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);

        var result = useCase.execute(CUSTOMER_ID, request);

        assertTrue(result.isAdded());
        assertEquals(PRODUCT_ID, result.productId());

        verify(wishlistRepository).save(any(Wishlist.class));
    }


    @Test
    void givenWishlistAtLimit_whenAddProduct_thenThrowLimitExceeded() {
        var wishlist = mock(Wishlist.class);
        var request = new AddProductRequest(PRODUCT_ID);

        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        doThrow(new WishlistLimitExceededException(WISHLIST_LIMIT_EXCEEDED))
                .when(wishlist)
                .addProduct(any(ProductItem.class));

        assertThrows(WishlistLimitExceededException.class, () ->
                useCase.execute(CUSTOMER_ID, request));

        verify(wishlistRepository, never()).save(any());
    }


    @Test
    void givenProductAlreadyExists_whenAddProduct_thenThrowProductExistsException() {
        var wishlist = mock(Wishlist.class);
        var request = new AddProductRequest(PRODUCT_ID);

        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        doThrow(new ProductAlreadyExistsException(ERROR_MESSAGE))
                .when(wishlist)
                .addProduct(any(ProductItem.class));

        assertThrows(ProductAlreadyExistsException.class, () ->
                useCase.execute(CUSTOMER_ID, request));

        verify(wishlistRepository, never()).save(any());
    }
}