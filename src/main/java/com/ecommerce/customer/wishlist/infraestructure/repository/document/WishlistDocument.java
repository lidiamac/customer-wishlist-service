package com.ecommerce.customer.wishlist.infraestructure.repository.document;


import com.ecommerce.customer.wishlist.domain.ProductItem;
import com.ecommerce.customer.wishlist.domain.Wishlist;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Document("wishlist")
public class WishlistDocument {
    @Id
    private String customerId;
    private List<ProductItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static WishlistDocument fromDomain(Wishlist domain) {
        var doc = new WishlistDocument();
        doc.setCustomerId(domain.getCustomerId());
        doc.setItems(domain.getItems());
        doc.setCreatedAt(domain.getCreatedAt());
        doc.setUpdatedAt(domain.getUpdatedAt());
        return doc;
    }

    public Wishlist toDomain() {
        return new Wishlist(
                this.customerId,
                this.items,
                this.createdAt,
                this.updatedAt
        );
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<ProductItem> getItems() {
        return items;
    }

    public void setItems(List<ProductItem> items) {
        this.items = items;
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
