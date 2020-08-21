package com.product.search.api.v1.service;

import com.product.search.api.v1.dto.Product;
import reactor.core.publisher.Flux;

public interface ProductCatalogService {
    Flux<Product> getProducts(String keyword, int limit);
}
