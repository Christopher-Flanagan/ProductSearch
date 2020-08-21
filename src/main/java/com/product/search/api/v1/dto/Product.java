package com.product.search.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.product.search.api.v1.colour.palette.ColourPalette;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("name")
    private String productName;

    private String imageUri;

    private ColourPalette palette;

    @JsonProperty("default_image_urls")
    private void unpackNested(Map<String,Object> urls) {
        if(urls.containsKey("main_image_url")) {
            this.imageUri = (String) urls.get("main_image_url");
        }
    }
}
