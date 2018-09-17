package pl.edu.pw.ee.cookbookserver.misc;

import org.springframework.http.HttpStatus;

public class ProcessingException extends Exception {

    private int code;
    private HttpStatus status;
    private String message;

    public ProcessingException(Error error) {
        super();
        this.code = error.code();
        this.status = error.status();
        this.message = error.message();
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
