package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.edu.pw.ee.cookbookserver.dto.ErrorDto;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProcessingException.class)
    public ResponseEntity handleProcessingException(ProcessingException e) {
        e.printStackTrace();
        ErrorDto errorDto = new ErrorDto(e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        e.printStackTrace();
        ErrorDto errorDto = new ErrorDto(1, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
