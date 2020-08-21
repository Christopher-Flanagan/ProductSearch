package com.product.search.api.v1.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String keyword) {
        super("No Products associated with the keyword :" + keyword);
    }
}
