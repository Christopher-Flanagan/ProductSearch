package com.product.search.api.v1.facade;

import com.product.search.api.v1.colour.palette.ColourPalette;
import com.product.search.api.v1.colour.palette.ImgixHexPalette;
import com.product.search.api.v1.dto.Product;
import com.product.search.api.v1.image.extractor.ImageColourPaletteExtractor;
import com.product.search.api.v1.service.ProductCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class MECProductSearchFacade implements ProductSearchFacade<Product> {
    private static final Logger logger = LoggerFactory.getLogger(MECProductSearchFacade.class);

    private ProductCatalogService productCatalogService;
    private ImageColourPaletteExtractor<ImgixHexPalette> colourPaletteExtractor;

    public MECProductSearchFacade(ProductCatalogService productCatalogService, ImageColourPaletteExtractor<ImgixHexPalette> colourPaletteExtractor) {
        this.productCatalogService = productCatalogService;
        this.colourPaletteExtractor = colourPaletteExtractor;
    }

    @Override
    public Flux<Product> getProducts(String keyword, int limit) {
        Flux<Product> products = productCatalogService.getProducts(keyword, limit);
        return products.flatMap( obj -> {
            if(obj.getImageUri() == null) {
                return Flux.just(obj);
            }
            else {
                Mono<ImgixHexPalette> colourPalette = colourPaletteExtractor.extractPalette(obj.getImageUri());
                colourPalette = colourPalette.switchIfEmpty(Mono.just(new ImgixHexPalette()));

                return colourPalette.zipWith(Mono.just(obj))
                        .map(tuple -> {
                            ColourPalette palette = tuple.getT1();
                            Product product = tuple.getT2();
                            product.setPalette(palette);
                            return product;
                        });
            }
        });
    }
}
