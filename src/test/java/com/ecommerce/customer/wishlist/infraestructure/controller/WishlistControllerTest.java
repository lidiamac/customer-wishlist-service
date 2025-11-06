package com.ecommerce.customer.wishlist.infraestructure.controller;


import com.ecommerce.customer.wishlist.application.usecases.AddProductToWishlistUseCase;
import com.ecommerce.customer.wishlist.application.usecases.CheckProductInWishlistUseCase;
import com.ecommerce.customer.wishlist.application.usecases.RemoveProductFromWishlistUseCase;
import com.ecommerce.customer.wishlist.application.usecases.RetrieveAllWishlistUseCase;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WishlistControllerTest {

    @InjectMocks
    private WishlistController classUnderTest;

    @Mock
    private AddProductToWishlistUseCase addProductToWishlist;
    @Mock
    private RemoveProductFromWishlistUseCase removeProductFromWishlist;
    @Mock
    private CheckProductInWishlistUseCase checkProductInWishlist;
    @Mock
    private RetrieveAllWishlistUseCase retrieveAllWishlist;

}
