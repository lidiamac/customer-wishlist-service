package com.ecommerce.customer.wishlist.infraestructure.repository;


import com.ecommerce.customer.wishlist.application.repository.WishlistRepository;
import com.ecommerce.customer.wishlist.domain.Wishlist;
import com.ecommerce.customer.wishlist.infraestructure.repository.document.WishlistDocument;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WishlistRepositoryImpl implements WishlistRepository {

    private final MongoWishlistRepository mongoWishlistRepository;


    public WishlistRepositoryImpl(MongoWishlistRepository mongoWishlistRepository) {
        this.mongoWishlistRepository = mongoWishlistRepository;
    }


    @Override
    public Optional<Wishlist> findByCustomerId(String customerId) {
        return mongoWishlistRepository.findByCustomerId(customerId)
                .map(WishlistDocument::toDomain);
    }

    @Override
    public Wishlist save(Wishlist wishlist) {
        var documentToSave = WishlistDocument.fromDomain(wishlist);
        var savedDocument = mongoWishlistRepository.save(documentToSave);
        return savedDocument.toDomain();
    }

    @Override
    public Boolean existsByCustomerIdAndItemsProductId(String customerId, Long productId) {
        return mongoWishlistRepository.existsByCustomerIdAndItemsProductId(customerId, productId);
    }

}
