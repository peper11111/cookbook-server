package pl.edu.pw.ee.cookbookserver.util;

import org.springframework.http.HttpStatus;

public enum Error {

    UNKNOWN_ERROR(1, HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred"),
    LOGIN_FAILURE(2, HttpStatus.FORBIDDEN, "Login failed"),
    ACCESS_DENIED(3, HttpStatus.FORBIDDEN, "Access denied"),
    MISSING_USERNAME(100, HttpStatus.BAD_REQUEST, "Missing username in payload"),
    MISSING_PASSWORD(101, HttpStatus.BAD_REQUEST, "Missing password in payload"),
    MISSING_EMAIL(102, HttpStatus.BAD_REQUEST, "Missing email in payload"),
    MISSING_UUID(103, HttpStatus.BAD_REQUEST, "Missing uuid in payload"),
    USERNAME_OCCUPIED(104, HttpStatus.CONFLICT, "User with the given username already exists"),
    PASSWORD_TOO_SHORT(105, HttpStatus.BAD_REQUEST, "Password too short"),
    INVALID_EMAIL(106, HttpStatus.BAD_REQUEST, "Incorrect email address format"),
    EMAIL_OCCUPIED(107, HttpStatus.CONFLICT, "User with the given email already exists"),
    TOKEN_EXPIRED(108, HttpStatus.BAD_REQUEST, "Token expired"),
    USER_NOT_FOUND(109, HttpStatus.NOT_FOUND, "User not found"),
    TOKEN_NOT_FOUND(110, HttpStatus.NOT_FOUND, "Token not found"),
    INVALID_USERNAME(114, HttpStatus.BAD_REQUEST, "Username can't be empty"),
    RECIPE_NOT_FOUND(115, HttpStatus.NOT_FOUND, "Recipe not found"),
    UPLOAD_NOT_FOUND(116, HttpStatus.NOT_FOUND, "Upload not found");

    private final int code;
    private final HttpStatus status;
    private final String message;

    Error(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
