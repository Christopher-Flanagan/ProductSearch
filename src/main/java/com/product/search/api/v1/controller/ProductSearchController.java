package com.product.search.api.v1.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/products/search")
public class ProductSearchController {

    @GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE })
    public Flux getProduct(@RequestParam String keyword) {
        return Flux.empty();
    }
}
