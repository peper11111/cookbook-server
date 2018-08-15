package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.edu.pw.ee.cookbookserver.util.PayloadException;
import pl.edu.pw.ee.cookbookserver.util.ResponseMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PayloadException.class)
    public ResponseEntity handlePayloadException(PayloadException e) {
        e.printStackTrace();
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseMessage.INTERNAL_SERVER_ERROR.value());
    }
}
