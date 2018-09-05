package pl.edu.pw.ee.cookbookserver.util;

import org.springframework.http.HttpStatus;

public enum Error {

    UNKNOWN_ERROR(100, HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred"),
    ACCESS_DENIED(101, HttpStatus.FORBIDDEN, "Access denied"),
    LOGIN_FAILURE(102, HttpStatus.FORBIDDEN, "Login failed"),

    USER_NOT_FOUND(200, HttpStatus.NOT_FOUND, "User not found"),
    TOKEN_NOT_FOUND(201, HttpStatus.NOT_FOUND, "Token not found"),
    UPLOAD_NOT_FOUND(202, HttpStatus.NOT_FOUND, "Upload not found"),
    RECIPE_NOT_FOUND(203, HttpStatus.NOT_FOUND, "Recipe not found"),
    AVATAR_NOT_FOUND(204, HttpStatus.NOT_FOUND, "Avatar with the given id not found"),
    BANNER_NOT_FOUND(205, HttpStatus.NOT_FOUND, "Banner with the giver id not found"),

    MISSING_USERNAME(300, HttpStatus.BAD_REQUEST, "Missing username in payload"),
    MISSING_PASSWORD(301, HttpStatus.BAD_REQUEST, "Missing password in payload"),
    MISSING_EMAIL(302, HttpStatus.BAD_REQUEST, "Missing email in payload"),
    MISSING_UUID(303, HttpStatus.BAD_REQUEST, "Missing uuid in payload"),
    MISSING_AVATAR_ID(304, HttpStatus.BAD_REQUEST, "missing avatar id in payload"),
    MISSING_BANNER_ID(305, HttpStatus.BAD_REQUEST, "Missing banner id in payload"),

    INVALID_USERNAME(400, HttpStatus.BAD_REQUEST, "Username can't be empty"),
    USERNAME_OCCUPIED(401, HttpStatus.CONFLICT, "User with the given username already exists"),
    PASSWORD_TOO_SHORT(402, HttpStatus.BAD_REQUEST, "Password too short"),
    INVALID_EMAIL(403, HttpStatus.BAD_REQUEST, "Incorrect email address format"),
    EMAIL_OCCUPIED(404, HttpStatus.CONFLICT, "User with the given email already exists"),
    TOKEN_EXPIRED(405, HttpStatus.BAD_REQUEST, "Token expired");

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
