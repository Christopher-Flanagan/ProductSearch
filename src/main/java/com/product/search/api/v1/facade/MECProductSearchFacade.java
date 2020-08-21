package com.product.search.api.v1.facade;

import com.product.search.api.v1.dto.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MECProductSearchFacade implements ProductSearchFacade<Product> {
    @Override
    public Flux<Product> getProducts(String keyword, int limit) {
        return Flux.empty();
    }
}
