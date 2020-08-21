package com.product.search.api.v1.controller;

import com.product.search.api.v1.dto.Product;
import com.product.search.api.v1.facade.ProductSearchFacade;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/products/search")
public class ProductSearchController {

    private ProductSearchFacade<Product> searchFacade;

    public ProductSearchController(ProductSearchFacade<Product> searchFacade) {
        this.searchFacade = searchFacade;
    }

    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Search For Products"),
            @ApiResponse(responseCode = "404", description = "Products Not Found") })
    @GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE })
    public Flux<Product> getProduct(@RequestParam String keywords) {
        return searchFacade.getProducts(keywords, 5);
    }
}
