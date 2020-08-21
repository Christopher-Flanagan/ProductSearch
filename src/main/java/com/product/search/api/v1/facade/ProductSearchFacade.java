package com.product.search.api.v1.facade;

import reactor.core.publisher.Flux;

public interface ProductSearchFacade<T> {
    Flux<T> getProducts(String keyword, int limit);
}
