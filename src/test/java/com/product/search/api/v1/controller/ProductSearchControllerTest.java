package com.product.search.api.v1.controller;

import com.product.search.api.v1.colour.palette.ImgixHexPalette;
import com.product.search.api.v1.dto.Product;
import com.product.search.api.v1.facade.ProductSearchFacade;
import com.product.search.api.v1.image.extractor.ImageColourPaletteExtractor;
import com.product.search.api.v1.service.ProductCatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductSearchControllerTest {
    private final String API_URI = "/api/v1/products/search?keywords={value}";

    @MockBean
    private ProductCatalogService catalogService;
    @MockBean
    private ImageColourPaletteExtractor<ImgixHexPalette> paletteExtractor;

    @Autowired
    private ProductSearchFacade<Product> productSearchFacade;

    @Test
    void successfulSearchRequest(@Autowired WebTestClient webClient) {
        Product[] products =  { generateTestProduct(), generateTestProduct()};
        ImgixHexPalette palette = new ImgixHexPalette();

        when(catalogService.getProducts(anyString(), anyInt())).thenReturn(Flux.just(products));
        when(paletteExtractor.extractPalette(anyString())).thenReturn(Mono.just(palette));

        webClient.get().uri(API_URI, "test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].product_code").isEqualTo(products[0].getProductCode())
                .jsonPath("$[1].product_code").isEqualTo(products[1].getProductCode())
                .jsonPath("$[2]").doesNotExist();
    }

    @Test
    void emptyProductListTest(@Autowired WebTestClient webClient) {
        Product[] products = {};
        when(catalogService.getProducts(anyString(), anyInt())).thenReturn(Flux.just(products));

        webClient.get().uri(API_URI, "test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isEmpty();
    }

    @Test
    void blankQueryParamTest(@Autowired WebTestClient webClient) {
        Product[] products =  { generateTestProduct(), generateTestProduct()};
        ImgixHexPalette palette = new ImgixHexPalette();

        when(catalogService.getProducts(anyString(), anyInt())).thenReturn(Flux.just(products));
        when(paletteExtractor.extractPalette(anyString())).thenReturn(Mono.just(palette));

        webClient.get().uri(API_URI, "")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].product_code").isEqualTo(products[0].getProductCode())
                .jsonPath("$[1].product_code").isEqualTo(products[1].getProductCode())
                .jsonPath("$[2]").doesNotExist();
    }

    @Test
    void notAllowedMethodTest(@Autowired WebTestClient testClient) {
        testClient.post().uri(API_URI, "bike")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);

        testClient.put().uri(API_URI, "bike")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);

        testClient.delete().uri(API_URI, "bike")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);

        testClient.patch().uri(API_URI, "bike")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }


    private Product generateTestProduct() {
        Product product = new Product();
        product.setProductCode(UUID.randomUUID().toString());
        product.setImageUri("http://cdn.mec.ca/stuff.com");
        return product;
    }
}
