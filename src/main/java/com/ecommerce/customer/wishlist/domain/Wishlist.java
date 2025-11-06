package com.ecommerce.customer.wishlist.domain;

import com.ecommerce.customer.wishlist.domain.exceptions.ProductAlreadyExistsException;
import com.ecommerce.customer.wishlist.domain.exceptions.ProductNotFoundException;
import com.ecommerce.customer.wishlist.domain.exceptions.WishlistLimitExceededException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ecommerce.customer.wishlist.domain.constants.WishlistMessages.*;
import static com.ecommerce.customer.wishlist.domain.constants.WishlistMessages.PRODUCT_NOT_FOUND;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

public class Wishlist {
    private static final int MAX_ITEMS = 20;
    private String customerId;
    private List<ProductItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Wishlist(String customerId, List<ProductItem> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.customerId = customerId;
        this.items = ofNullable(items).orElse(new ArrayList<>());
        this.createdAt = nonNull(createdAt) ? createdAt : now();
        this.updatedAt = nonNull(updatedAt) ? updatedAt : now();
    }

    public Wishlist(String customerId) {
        this(customerId, new ArrayList<>(), now(), now());
    }

    public void addProduct(ProductItem product){
        if(this.items.size() >= MAX_ITEMS){
            throw new WishlistLimitExceededException(WISHLIST_LIMIT_EXCEEDED);
        }
        if(containsProduct(product.productId())) {
            throw new ProductAlreadyExistsException(format(PRODUCT_ALREADY_EXISTS, product.productId(), this.customerId));
        }

        this.items.add(product);
        this.updatedAt = now();

    }

    public void removeProduct(Long productId){
        boolean removed = this.items.removeIf(item -> item.productId().equals(productId));

        if (!removed) {
            throw new ProductNotFoundException(format(PRODUCT_NOT_FOUND, productId, this.customerId)
            );
        }
        this.updatedAt = now();
    }

    private boolean containsProduct(Long productId) {
        return this.items.stream()
                .anyMatch(p -> p.productId().equals(productId));
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<ProductItem> getItems() {
        return items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
