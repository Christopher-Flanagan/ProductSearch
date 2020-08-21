package com.product.search.api.v1.facade;

import com.product.search.api.v1.colour.Colour;
import com.product.search.api.v1.colour.palette.ImgixHexPalette;
import com.product.search.api.v1.dto.Product;
import com.product.search.api.v1.image.extractor.ImgixColourPaletteExtractor;
import com.product.search.api.v1.service.MecProductCatalogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MECProductSearchFacadeTest {

    @Mock
    private ImgixColourPaletteExtractor colourPaletteExtractor;
    @Mock
    private MecProductCatalogService productCatalogService;

    private MECProductSearchFacade mecProductSearchFacade;

    @Test
    void ApiMergeTest() {
        Product[] products = {generateTestProduct(), generateTestProduct()};

        List<Colour> colours = new ArrayList<>();
        colours.add(new Colour("#123", "hex"));
        colours.add(new Colour("#1234", "hex"));
        colours.add(new Colour("#12345", "hex"));

        ImgixHexPalette palette = new ImgixHexPalette();
        palette.setPalette(colours);

        when(productCatalogService.getProducts(anyString(), anyInt())).thenReturn(Flux.just(products));
        when(colourPaletteExtractor.extractPalette(anyString())).thenReturn(Mono.just(palette));

        mecProductSearchFacade = new MECProductSearchFacade(productCatalogService, colourPaletteExtractor);

        StepVerifier.create(
                mecProductSearchFacade.getProducts("random", 5))
                .expectNext(products)
                .expectComplete()
                .verify();
    }

    @Test
    void emptyColourPaletteListTest() {
        Product[] products = {generateTestProduct(), generateTestProduct()};

        when(productCatalogService.getProducts(anyString(), anyInt())).thenReturn(Flux.just(products));
        when(colourPaletteExtractor.extractPalette(anyString())).thenReturn(Mono.empty());

        mecProductSearchFacade = new MECProductSearchFacade(productCatalogService, colourPaletteExtractor);

        StepVerifier.create(
                mecProductSearchFacade.getProducts("random", 5))
                .expectNext(products)
                .expectComplete()
                .verify();
    }

    @Test
    void emptyProductListTest() {
        when(productCatalogService.getProducts(anyString(), anyInt())).thenReturn(Flux.empty());
        mecProductSearchFacade = new MECProductSearchFacade(productCatalogService, colourPaletteExtractor);

        StepVerifier.create(
                mecProductSearchFacade.getProducts("random", 5))
                .expectComplete()
                .verify();
    }

    private Product generateTestProduct() {
        Product product = new Product();
        product.setProductCode(UUID.randomUUID().toString());
        product.setImageUri("http://thisistest.com");
        return product;
    }

}
