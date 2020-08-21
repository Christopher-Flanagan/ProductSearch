package com.product.search.api.v1.image.extractor;

import com.product.search.api.v1.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

class ImgixColourPaletteExtractorTest {

    private ImgixColourPaletteExtractor extractor;

    @Test
    void validMonoResponseTest() {
        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.OK)
                                .header("content-type", "application/json")
                                .body(getRestData())
                                .build())
                );
        extractor = new ImgixColourPaletteExtractor(builder);

        StepVerifier.create(
                extractor.extractPalette("randomImage", Collections.emptyMap()))
                .assertNext(i -> {
                    assert i.getPalette().get(0).getValue().equals("#060505");
                    assert i.getPalette().get(1).getValue().equals("#292727");
                    assert i.getPalette().get(2).getValue().equals("#3e4040");
                    assert i.getPalette().get(3).getValue().equals("#595757");
                    assert i.getPalette().get(4).getValue().equals("#93918c");
                    assert i.getPalette().get(5).getValue().equals("#d3d2cb");
                })
                .expectComplete()
                .verify();
    }

    @Test
    void emptyColourListResponseTest() {
        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.OK)
                                .header("content-type", "application/json")
                                .body("{\n" +
                                        "\"colors\": [\n" +
                                        "\t]\n" +
                                        "}")
                                .build())
                );
        extractor = new ImgixColourPaletteExtractor(builder);

        StepVerifier.create(
                extractor.extractPalette("randomImage", Collections.emptyMap()))
                .assertNext(i -> {
                    assert i.getPalette().isEmpty();
                })
                .expectComplete()
                .verify();

    }

    @Test
    void notFoundStatusTest() {
        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND)
                                .header("content-type", "application/json")
                                .body("{\n" +
                                        "}")
                                .build())
                );
        extractor = new ImgixColourPaletteExtractor(builder);
        StepVerifier.create(
                extractor.extractPalette("randomImage", Collections.emptyMap()))
                .expectError(NotFoundException.class)
                .verify();
    }

    private String getRestData() {
        return "{\n" +
                "    \"colors\": [\n" +
                "        {\n" +
                "            \"red\": 0.0235294,\n" +
                "            \"hex\": \"#060505\",\n" +
                "            \"blue\": 0.0196078,\n" +
                "            \"green\": 0.0196078\n" +
                "        },\n" +
                "        {\n" +
                "            \"red\": 0.160784,\n" +
                "            \"hex\": \"#292727\",\n" +
                "            \"blue\": 0.152941,\n" +
                "            \"green\": 0.152941\n" +
                "        },\n" +
                "        {\n" +
                "            \"red\": 0.243137,\n" +
                "            \"hex\": \"#3e4040\",\n" +
                "            \"blue\": 0.25098,\n" +
                "            \"green\": 0.25098\n" +
                "        },\n" +
                "        {\n" +
                "            \"red\": 0.34902,\n" +
                "            \"hex\": \"#595757\",\n" +
                "            \"blue\": 0.341176,\n" +
                "            \"green\": 0.341176\n" +
                "        },\n" +
                "        {\n" +
                "            \"red\": 0.576471,\n" +
                "            \"hex\": \"#93918c\",\n" +
                "            \"blue\": 0.54902,\n" +
                "            \"green\": 0.568627\n" +
                "        },\n" +
                "        {\n" +
                "            \"red\": 0.827451,\n" +
                "            \"hex\": \"#d3d2cb\",\n" +
                "            \"blue\": 0.796078,\n" +
                "            \"green\": 0.823529\n" +
                "        }\n" +
                "    ],\n" +
                "    \"average_luminance\": 0.881608,\n" +
                "    \"dominant_colors\": {\n" +
                "        \"muted_light\": {\n" +
                "            \"red\": 0.756863,\n" +
                "            \"hex\": \"#c1bdb7\",\n" +
                "            \"blue\": 0.717647,\n" +
                "            \"green\": 0.741176\n" +
                "        },\n" +
                "        \"muted_dark\": {\n" +
                "            \"red\": 0.262745,\n" +
                "            \"hex\": \"#43413e\",\n" +
                "            \"blue\": 0.243137,\n" +
                "            \"green\": 0.254902\n" +
                "        },\n" +
                "        \"muted\": {\n" +
                "            \"red\": 0.529412,\n" +
                "            \"hex\": \"#877771\",\n" +
                "            \"blue\": 0.443137,\n" +
                "            \"green\": 0.466667\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }
}
