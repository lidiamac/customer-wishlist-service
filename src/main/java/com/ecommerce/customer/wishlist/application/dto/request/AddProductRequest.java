package com.ecommerce.customer.wishlist.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddProductRequest(@NotNull(message = "ProductId não pode estar vazio")
                                 Long productId)
{}
