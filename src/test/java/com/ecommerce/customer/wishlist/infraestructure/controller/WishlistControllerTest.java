package com.ecommerce.customer.wishlist.infraestructure.controller;


import com.ecommerce.customer.wishlist.application.dto.response.WishlistResponse;
import com.ecommerce.customer.wishlist.application.usecases.AddProductToWishlistUseCase;
import com.ecommerce.customer.wishlist.application.usecases.CheckProductInWishlistUseCase;
import com.ecommerce.customer.wishlist.application.usecases.RemoveProductFromWishlistUseCase;
import com.ecommerce.customer.wishlist.application.usecases.RetrieveAllWishlistUseCase;
import com.ecommerce.customer.wishlist.domain.ProductItem;
import com.ecommerce.customer.wishlist.domain.exceptions.*;
import com.ecommerce.customer.wishlist.infraestructure.controller.handler.ApiExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.ecommerce.customer.wishlist.application.dto.request.AddProductRequest;
import com.ecommerce.customer.wishlist.application.dto.response.ProductResponse;

import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WishlistControllerTest {


    @InjectMocks
    private WishlistController controller;

    @Mock
    private AddProductToWishlistUseCase addProductUseCase;
    @Mock
    private RemoveProductFromWishlistUseCase removeProductUseCase;
    @Mock
    private CheckProductInWishlistUseCase checkProductUseCase;
    @Mock
    private RetrieveAllWishlistUseCase retrieveAllUseCase;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    private static final String BASE_URL = "/wishlist";
    private final String CUSTOMER_ID = "65b8c387b9e7a20c3a8d1f2e";
    private final Long PRODUCT_ID = 1579758L;
    private final Long OTHER_PRODUCT_ID = 8475945L;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApiExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .build();
    }


    @Test
    void shouldReturn201_whenAddProduct_givenValidRequest() throws Exception {
        var request = new AddProductRequest(PRODUCT_ID);
        var response = new ProductResponse(PRODUCT_ID, true);

        when(addProductUseCase.execute(eq(CUSTOMER_ID), any(AddProductRequest.class))).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/{customerId}/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(PRODUCT_ID));

        verify(addProductUseCase).execute(eq(CUSTOMER_ID), any(AddProductRequest.class));
    }

    @Test
    void shouldReturn400_whenAddProduct_givenLimitExceeded() throws Exception {
        var request = new AddProductRequest(PRODUCT_ID);

        when(addProductUseCase.execute(eq(CUSTOMER_ID), any()))
                .thenThrow(new WishlistLimitExceededException("Limite da Wishlist excedido."));

        mockMvc.perform(post(BASE_URL + "/{customerId}/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Limite da Wishlist excedido."));
    }

    @Test
    void shouldReturn400_whenAddProduct_givenProductAlreadyExists() throws Exception {
        var request = new AddProductRequest(PRODUCT_ID);

        when(addProductUseCase.execute(eq(CUSTOMER_ID), any()))
                .thenThrow(new ProductAlreadyExistsException("Produto já está na Wishlist."));

        mockMvc.perform(post(BASE_URL + "/{customerId}/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Produto já está na Wishlist."));
    }

    @Test
    void shouldReturn204_whenRemoveProduct_givenValidIds() throws Exception {
        doNothing().when(removeProductUseCase).execute(CUSTOMER_ID, PRODUCT_ID);

        mockMvc.perform(delete(BASE_URL + "/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404_whenRemoveProduct_givenProductNotFound() throws Exception {
        doThrow(new ProductNotFoundException("Produto não encontrado na Wishlist."))
                .when(removeProductUseCase).execute(CUSTOMER_ID, PRODUCT_ID);

        mockMvc.perform(delete(BASE_URL + "/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Produto não encontrado na Wishlist."));
    }

    @Test
    void shouldReturn404_whenRemoveProduct_givenClientNotFound() throws Exception {
        doThrow(new ClientNotFoundException("Cliente não encontrado."))
                .when(removeProductUseCase).execute(CUSTOMER_ID, PRODUCT_ID);

        mockMvc.perform(delete(BASE_URL + "/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cliente não encontrado."));
    }

    @Test
    void givenCustomerIdAndProductId_whenWishlistNotFound_thenReturnNotFound() throws Exception {
        doThrow(new WishListNotFoundException("Wishlist not found for customer"))
                .when(removeProductUseCase)
                .execute(CUSTOMER_ID, PRODUCT_ID);

        mockMvc.perform(delete(BASE_URL + "/" + CUSTOMER_ID + "/products/" + PRODUCT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Wishlist not found for customer"));
    }

    @Test
    void givenUnexpectedError_whenRetrieveAll_thenReturnInternalServerError() throws Exception {
        when(retrieveAllUseCase.execute(CUSTOMER_ID)).thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get(BASE_URL + "/{customerId}", CUSTOMER_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));

        verify(retrieveAllUseCase, times(1)).execute(CUSTOMER_ID);
    }

    @Test
    void shouldReturnTrue_whenCheckProduct_givenProductInWishlist() throws Exception {
        when(checkProductUseCase.execute(CUSTOMER_ID, PRODUCT_ID)).thenReturn(true);

        mockMvc.perform(get(BASE_URL + "/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void shouldReturnFalse_whenCheckProduct_givenProductNotInWishlist() throws Exception {
        when(checkProductUseCase.execute(CUSTOMER_ID, PRODUCT_ID)).thenReturn(false);

        mockMvc.perform(get(BASE_URL + "/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }


    @Test
    void shouldReturnWishlist_whenRetrieveAll_givenValidCustomerId() throws Exception {
        var item1 = new ProductItem(PRODUCT_ID);
        var item2 = new ProductItem(OTHER_PRODUCT_ID);
        var response = new WishlistResponse(CUSTOMER_ID, List.of(item1, item2), now(), now());

        when(retrieveAllUseCase.execute(CUSTOMER_ID)).thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/{customerId}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID))
                .andExpect(jsonPath("$.items[0].productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$.items[1].productId").value(OTHER_PRODUCT_ID));
    }

    @Test
    void shouldReturnEmpty_whenRetrieveAll_givenEmptyWishlist() throws Exception {
        var response = new WishlistResponse(CUSTOMER_ID, Collections.emptyList(), now(), now());

        when(retrieveAllUseCase.execute(CUSTOMER_ID)).thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/{customerId}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").doesNotExist());
    }

}
