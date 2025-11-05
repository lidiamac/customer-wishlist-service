package com.ecommerce.customer.wishlist.infraestructure.controller;

import com.ecommerce.customer.wishlist.application.dto.request.AddProductRequest;
import com.ecommerce.customer.wishlist.application.dto.response.ProductResponse;
import com.ecommerce.customer.wishlist.application.usecases.AddProductToWishlistUseCase;
import com.ecommerce.customer.wishlist.application.usecases.CheckProductInWishlistUseCase;
import com.ecommerce.customer.wishlist.application.usecases.RemoveProductFromWishlistUseCase;
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
    private final RemoveProductFromWishlistUseCase removeProductFromWishlist;
    private final CheckProductInWishlistUseCase checkProductInWishlist;


    public WishlistController(AddProductToWishlistUseCase addProductToWishlist, RemoveProductFromWishlistUseCase removeProductFromWishlist, CheckProductInWishlistUseCase checkProductInWishlist) {
        this.addProductToWishlist = addProductToWishlist;
        this.removeProductFromWishlist = removeProductFromWishlist;
        this.checkProductInWishlist = checkProductInWishlist;
    }

    @PostMapping("/{customerId}/products")
    public ResponseEntity<ProductResponse> addProductToWishlist(@PathVariable String customerId,
                                                                @RequestBody @Valid AddProductRequest request) {

        var response = addProductToWishlist.execute(customerId, request);
        return ResponseEntity.status(CREATED).body(response);
    }

    @DeleteMapping("/{customerId}/products/{productId}")
    public ResponseEntity<Void> removeProductFromWishlist( @PathVariable String customerId,
                                                           @PathVariable Long productId) {

        removeProductFromWishlist.execute(customerId, productId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{customerId}/products/{productId}")
    public ResponseEntity<Boolean> checkProductInWishlist(@PathVariable String customerId,
                                                          @PathVariable Long productId) {

        var response = checkProductInWishlist.execute(customerId, productId);
        return ResponseEntity.ok(response);
    }

}
