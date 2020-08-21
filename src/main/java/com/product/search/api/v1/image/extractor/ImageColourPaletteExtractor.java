package com.product.search.api.v1.image.extractor;

import com.product.search.api.v1.colour.palette.ColourPalette;
import reactor.core.publisher.Mono;

public interface ImageColourPaletteExtractor<T extends ColourPalette> {
    Mono<T> extractPalette(String imageUri);
}
