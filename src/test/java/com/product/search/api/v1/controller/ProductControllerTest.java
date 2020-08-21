package com.product.search.api.v1.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductControllerTest {

    @Test
    void verifyEndpointTest(@Autowired WebTestClient webClient) {
        webClient.get().uri("/api/v1/products/search?keyword=test")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
