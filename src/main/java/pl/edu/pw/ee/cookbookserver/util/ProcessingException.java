package pl.edu.pw.ee.cookbookserver.util;

import org.springframework.http.HttpStatus;

public class ProcessingException extends Exception {

    private HttpStatus status;

    public ProcessingException(HttpStatus status) {
        super();
        this.status = status;
    }

    public ProcessingException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
