package com.product.search.api.v1.exceptions;

import com.product.search.api.v1.dto.ErrorResponse;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ReadTimeoutException.class, WriteTimeoutException.class, ConnectTimeoutException.class})
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ResponseEntity handleReadTimeoutException(ReadTimeoutException ex) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(new ErrorResponse("Request has timed out."));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }
}
