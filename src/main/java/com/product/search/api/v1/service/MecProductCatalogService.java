package com.product.search.api.v1.service;

import com.product.search.api.v1.dto.Product;
import com.product.search.api.v1.dto.ProductWrapper;
import com.product.search.api.v1.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MecProductCatalogService implements ProductCatalogService {
    private static final String API_MIME_TYPE = "application/json";
    private static final String API_BASE_URL = "?keywords={}";
    private static final Logger logger = LoggerFactory.getLogger(MecProductCatalogService.class);

    private WebClient webClient;

    public MecProductCatalogService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .defaultHeader(HttpHeaders.ACCEPT, API_MIME_TYPE)
                .build();
    }

    @Override
    public Flux<Product> getProducts(String keyword, int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("www.mec.ca")
                        .path("/api/v1/products/search")
                        .queryParam("keyword", keyword)
                        .build())
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND,
                        response -> Mono.just(new NotFoundException(keyword)))
                .bodyToMono(ProductWrapper.class)
                .flatMapMany(i -> {
                    if(i.getProductsList() == null) {
                        throw new NotFoundException(keyword);
                    }
                    else {
                        return Flux.fromStream(i.getProductsList().stream().limit(limit));
                    }
                });
    }
}
