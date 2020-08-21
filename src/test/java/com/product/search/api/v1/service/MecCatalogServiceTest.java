package com.product.search.api.v1.service;

import com.product.search.api.v1.dto.Product;
import com.product.search.api.v1.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class MecCatalogServiceTest {

    private MecProductCatalogService catalogService;

    @Test
    void validApiResponseTest () {
        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.OK)
                                .header("content-type", "application/json")
                                .body(getRestData())
                                .build())
                );
        catalogService = new MecProductCatalogService(builder);

        StepVerifier.create(
                catalogService.getProducts("test", 5))
                .expectNext(new Product("6006-020",
                        "2020 Habit 6 Bicycle",
                        "https://cdn.mec.ca/medias/sys_master/fallback/fallback/9058549202974/6006020-BK000-fallback.jpg",
                        null))
                .expectComplete()
                .verify();
    }

    @Test
    void EmptyProductListTest() {
        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.OK)
                                .header("content-type", "application/json")
                                .body("{\n" +
                                        "\"products\": [\n" +
                                        "\t]\n" +
                                        "}")
                                .build())
                );
        catalogService = new MecProductCatalogService(builder);

        StepVerifier.create(
                catalogService.getProducts("test", 5))
                .expectComplete()
                .verify();

    }

    @Test
    void resourceNotFoundTest() {
        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND)
                                .header("content-type", "application/json")
                                .body("")
                                .build())
                );
        catalogService = new MecProductCatalogService(builder);
        StepVerifier.create(
                catalogService.getProducts("test", 5))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void ExternalServerFaultTest() {
        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(clientRequest ->
                        Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR)
                                .header("content-type", "application/json")
                                .body("{\n" +
                                        "}")
                                .build())
                );
        catalogService = new MecProductCatalogService(builder);
        StepVerifier.create(
                catalogService.getProducts("test", 5))
                .expectError(Exception.class)
                .verify();
    }


    private Product generateTestProduct() {
        Product product = new Product();
        product.setProductCode("6006-020");
        return product;
    }

    private String getRestData() {
        return "{\n" +
                "    \"products\": [\n" +
                "        {\n" +
                "            \"list_price\": {\n" +
                "                \"amount\": 2700,\n" +
                "                \"currency\": \"CAD\"\n" +
                "            },\n" +
                "            \"all_skus_clearance\": false,\n" +
                "            \"default_image_urls\": {\n" +
                "                \"small_image_url\": \"https://cdn.mec.ca/medias/sys_master/placeholder/placeholder/9058548678686/6006020-BK000-placeholder.jpg\",\n" +
                "                \"large_image_url\": \"https://cdn.mec.ca/medias/sys_master/fallback/fallback/9058549202974/6006020-BK000-fallback.jpg\",\n" +
                "                \"main_image_url\": \"https://cdn.mec.ca/medias/sys_master/fallback/fallback/9058549202974/6006020-BK000-fallback.jpg\",\n" +
                "                \"zoom_image_url\": \"https://cdn.mec.ca/medias/sys_master/fallback/fallback/9058549202974/6006020-BK000-fallback.jpg\"\n" +
                "            },\n" +
                "            \"product_code\": \"6006-020\",\n" +
                "            \"full_name\": \"Cannondale 2020 Habit 6 Bicycle - Unisex\",\n" +
                "            \"name\": \"2020 Habit 6 Bicycle\",\n" +
                "            \"brand\": \"Cannondale\",\n" +
                "            \"review_count\": 0,\n" +
                "            \"review_rating\": 0,\n" +
                "            \"can_add_to_cart\": true,\n" +
                "            \"can_add_to_wish_list\": true,\n" +
                "            \"new_to_mec\": false,\n" +
                "            \"default_colour_code\": \"BK000\",\n" +
                "            \"web_url\": \"https://www.mec.ca/product/6006-020/2020-Habit-6-Bicycle\",\n" +
                "            \"clearance\": false,\n" +
                "            \"tracking\": {\n" +
                "                \"erp_category\": \"982-30-20-20\",\n" +
                "                \"category\": \"cycling/bikes/mountain bikes/(not set)/\"\n" +
                "            },\n" +
                "            \"product_details_url\": \"https://www.mec.ca/api/v1/products/6006-020\"\n" +
                "        }\n" +
                "\t]\n" +
                "}";
    }
}
