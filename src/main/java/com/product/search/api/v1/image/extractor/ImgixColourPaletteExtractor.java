package com.product.search.api.v1.image.extractor;

import com.product.search.api.v1.colour.palette.ImgixHexPalette;
import com.product.search.api.v1.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ImgixColourPaletteExtractor implements ImageColourPaletteExtractor<ImgixHexPalette> {
    private static final String API_MIME_TYPE = "application/json";
    private static final Logger logger = LoggerFactory.getLogger(ImgixColourPaletteExtractor.class);

    private WebClient webClient;

    public ImgixColourPaletteExtractor(@Qualifier("clientBuilder") WebClient.Builder builder) {
        this.webClient = builder
                .defaultHeader(HttpHeaders.ACCEPT, API_MIME_TYPE)
                .build();
    }

    @Override
    public Mono<ImgixHexPalette> extractPalette(String imageUri) {
        if(imageUri.isEmpty() || !imageUri.contains("cdn.mec.ca")) {
            logger.info("Invalid image Uri format : " + imageUri);
            return Mono.empty();
        }

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                .scheme("http")
                .host("mec.imgix.net")
                .path(imageUri.split("cdn.mec.ca")[1])
                .queryParam("palette", "json")
                .build())
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND,
                        response -> Mono.just(new NotFoundException("Not Colour Palette Associated with Image")))
                .onStatus(status -> status == HttpStatus.INTERNAL_SERVER_ERROR,
                        response -> Mono.just(new Exception("Error with external server")))
                .bodyToMono(ImgixHexPalette.class);
    }
}
