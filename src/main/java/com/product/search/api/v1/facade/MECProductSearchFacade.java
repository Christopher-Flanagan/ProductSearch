package com.product.search.api.v1.facade;

import com.product.search.api.v1.dto.Product;
import com.product.search.api.v1.service.ProductCatalogService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MECProductSearchFacade implements ProductSearchFacade<Product> {

    private ProductCatalogService productCatalogService;

    public MECProductSearchFacade(ProductCatalogService productCatalogService) {
        this.productCatalogService = productCatalogService;
    }

    @Override
    public Flux<Product> getProducts(String keyword, int limit) {
        Flux<Product> products = productCatalogService.getProducts(keyword, limit);
        return products;
    }
}
