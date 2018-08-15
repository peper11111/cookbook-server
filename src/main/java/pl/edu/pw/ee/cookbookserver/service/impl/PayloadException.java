package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.http.HttpStatus;

public class PayloadException extends Exception {

    private HttpStatus status;

    public PayloadException(HttpStatus status) {
        super();
        this.status = status;
    }

    public PayloadException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
