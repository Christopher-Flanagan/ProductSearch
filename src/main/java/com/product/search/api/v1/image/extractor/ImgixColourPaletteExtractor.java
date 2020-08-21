package com.product.search.api.v1.image.extractor;

import com.product.search.api.v1.colour.palette.ImgixHexPalette;
import com.product.search.api.v1.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

public class ImgixColourPaletteExtractor implements ImageColourPaletteExtractor<ImgixHexPalette> {
    private static final String API_MIME_TYPE = "application/json";
    private static final Logger logger = LoggerFactory.getLogger(ImgixColourPaletteExtractor.class);

    private WebClient webClient;

    public ImgixColourPaletteExtractor(WebClient.Builder builder) {
        this.webClient = builder
                .defaultHeader(HttpHeaders.ACCEPT, API_MIME_TYPE)
                .build();
    }

    @Override
    public Mono<ImgixHexPalette> extractPalette(String imageUri, Map<String, String> params) {
        if(imageUri.isEmpty()) {
            return Mono.empty();
        }

        String param = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                .scheme("http")
                .host("mec.imgix.net")
                .path(imageUri)
                .queryParam(param)
                .build())
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND,
                        response -> Mono.just(new NotFoundException("Not Colour Palette Associated with Image")))
                .bodyToMono(ImgixHexPalette.class);
    }
}
