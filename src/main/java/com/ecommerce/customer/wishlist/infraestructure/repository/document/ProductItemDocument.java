package com.ecommerce.customer.wishlist.infraestructure.repository.document;

import com.ecommerce.customer.wishlist.domain.ProductItem;

public  record ProductItemDocument(Long productId)
{
    public static ProductItemDocument fromDomain(ProductItem productItem) {
        return new ProductItemDocument(productItem.productId());
    }

    public ProductItem toDomain() {
        return new ProductItem(productId);
    }
}

