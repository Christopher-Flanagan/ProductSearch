package com.product.search.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductWrapper {
    @JsonProperty("products")
    List<Product> productsList;
}
