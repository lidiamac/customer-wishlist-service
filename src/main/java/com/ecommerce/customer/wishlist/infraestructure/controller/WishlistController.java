package com.ecommerce.customer.wishlist.infraestructure.controller;

import com.ecommerce.customer.wishlist.application.dto.request.AddProductRequest;
import com.ecommerce.customer.wishlist.application.dto.response.ProductResponse;
import com.ecommerce.customer.wishlist.application.usecases.AddProductToWishlistUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final AddProductToWishlistUseCase addProductToWishlist;

    public WishlistController(AddProductToWishlistUseCase addProductToWishlist) {
        this.addProductToWishlist = addProductToWishlist;
    }

    @PostMapping("/{customerId}/products")
    public ResponseEntity<ProductResponse> addProductToWishlist(@PathVariable String customerId,
                                                                @RequestBody @Valid AddProductRequest request) {

        var response = addProductToWishlist.execute(customerId, request);
        return ResponseEntity.status(CREATED).body(response);
    }

}
