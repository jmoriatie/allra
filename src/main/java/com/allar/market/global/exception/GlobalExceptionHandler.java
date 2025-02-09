package com.allar.market.global.exception;

import com.allar.market.global.exception.exceptions.OrderCancelException;
import com.allar.market.global.exception.exceptions.ProductQuantityNotEnoughException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductQuantityNotEnoughException.class)
    public ResponseEntity<ErrorResponse> handleOrderCancelException(ProductQuantityNotEnoughException e){
        log.error(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
    }
    @ExceptionHandler(OrderCancelException.class)
    public ResponseEntity<ErrorResponse> handleOrderCancelException(OrderCancelException e){
        log.error(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e){
        log.error(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleOrderCancelException(RuntimeException e){
        log.error(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
    }
}
