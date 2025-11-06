package com.ecommerce.customer.wishlist.infraestructure.repository;


import com.ecommerce.customer.wishlist.domain.Wishlist;
import com.ecommerce.customer.wishlist.infraestructure.repository.document.WishlistDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistRepositoryImplTest {

    @InjectMocks
    private WishlistRepositoryImpl classUnderTest;

    @Mock
    private MongoWishlistRepository mongoWishlistRepository;

    private final String CUSTOMER_ID = "65b8c387b9e7a20c3a8d1f2e";
    private final Long PRODUCT_ID = 1579758L;



    @Test
    void givenExistingWishlistDocument_whenFindByCustomerId_thenReturnDomainWishlist() {
        var document = new WishlistDocument();
        document.setCustomerId(CUSTOMER_ID);

        when(mongoWishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(document));

        var result = classUnderTest.findByCustomerId(CUSTOMER_ID);

        assertTrue(result.isPresent());
        assertEquals(CUSTOMER_ID, result.get().getCustomerId());

        verify(mongoWishlistRepository).findByCustomerId(CUSTOMER_ID);
    }

    @Test
    void givenNonExistingCustomer_whenFindByCustomerId_thenReturnEmptyOptional() {
        when(mongoWishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        var result = classUnderTest.findByCustomerId(CUSTOMER_ID);

        assertTrue(result.isEmpty());

        verify(mongoWishlistRepository).findByCustomerId(CUSTOMER_ID);
    }


    @Test
    void givenDomainWishlist_whenSave_thenConvertAndReturnSavedDomainWishlist() {
        var domainWishlist = new Wishlist(CUSTOMER_ID);
        var savedDocument = WishlistDocument.fromDomain(domainWishlist);

        when(mongoWishlistRepository.save(any(WishlistDocument.class))).thenReturn(savedDocument);

        var result = classUnderTest.save(domainWishlist);

        assertNotNull(result);
        assertEquals(CUSTOMER_ID, result.getCustomerId());

        verify(mongoWishlistRepository).save(any(WishlistDocument.class));
    }



    @Test
    void givenCustomerAndProduct_whenExistsCheck_thenReturnTrueOrFalse() {
        when(mongoWishlistRepository.existsByCustomerIdAndItemsProductId(CUSTOMER_ID, PRODUCT_ID)).thenReturn(true);

        var result = classUnderTest.existsByCustomerIdAndItemsProductId(CUSTOMER_ID, PRODUCT_ID);

        assertTrue(result);

        verify(mongoWishlistRepository).existsByCustomerIdAndItemsProductId(CUSTOMER_ID, PRODUCT_ID);
    }

}
