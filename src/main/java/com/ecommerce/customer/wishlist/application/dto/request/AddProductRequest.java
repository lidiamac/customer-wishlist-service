package com.ecommerce.customer.wishlist.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddProductRequest(@NotNull Long productId)
{}
