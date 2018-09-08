package pl.edu.pw.ee.cookbookserver.util;

import org.springframework.http.HttpStatus;

public enum Error {

    SERVER_ERROR(100, HttpStatus.INTERNAL_SERVER_ERROR, "Server error"),
    ACCESS_DENIED(101, HttpStatus.FORBIDDEN, "Access denied"),
    LOGIN_FAILURE(102, HttpStatus.FORBIDDEN, "Login failure"),

    USER_NOT_FOUND(200, HttpStatus.NOT_FOUND, "User not found"),
    TOKEN_NOT_FOUND(201, HttpStatus.NOT_FOUND, "Token not found"),
    UPLOAD_NOT_FOUND(202, HttpStatus.NOT_FOUND, "Upload not found"),
    RECIPE_NOT_FOUND(203, HttpStatus.NOT_FOUND, "Recipe not found"),
    AVATAR_NOT_FOUND(204, HttpStatus.NOT_FOUND, "Avatar with the given id not found"),
    BANNER_NOT_FOUND(205, HttpStatus.NOT_FOUND, "Banner with the giver id not found"),
    CUISINE_NOT_FOUND(206, HttpStatus.NOT_FOUND, "Cuisine with the given id not found"),
    COMMENT_NOT_FOUND(207, HttpStatus.NOT_FOUND, "Comment with the given id not found"),

    MISSING_USERNAME(300, HttpStatus.BAD_REQUEST, "Missing username in payload"),
    MISSING_PASSWORD(301, HttpStatus.BAD_REQUEST, "Missing password in payload"),
    MISSING_EMAIL(302, HttpStatus.BAD_REQUEST, "Missing email in payload"),
    MISSING_UUID(303, HttpStatus.BAD_REQUEST, "Missing uuid in payload"),
    MISSING_AVATAR_ID(304, HttpStatus.BAD_REQUEST, "missing avatar id in payload"),
    MISSING_BANNER_ID(305, HttpStatus.BAD_REQUEST, "Missing banner id in payload"),
    MISSING_TITLE(306, HttpStatus.BAD_REQUEST, "Missing title in payload"),
    MISSING_CUISINE_ID(307, HttpStatus.BAD_REQUEST, "Missing cuisine id in payload"),
    MISSING_DIFFICULTY(308, HttpStatus.BAD_REQUEST, "Missing difficulty in payload"),
    MISSING_PLATES(309, HttpStatus.BAD_REQUEST, "Missing plates in payload"),
    MISSING_PREPARATION_TIME(310, HttpStatus.BAD_REQUEST, "Missing preparation time in payload"),
    MISSING_RECIPE_ID(311, HttpStatus.BAD_REQUEST, "Missing recipe id in payload"),
    MISSING_CONTENT(312, HttpStatus.BAD_REQUEST, "Missing content in payload"),
    MISSING_PARENT_ID(313, HttpStatus.BAD_REQUEST, "Missing parent comment id in payload"),

    EMPTY_USERNAME(400, HttpStatus.BAD_REQUEST, "Username can't be empty"),
    USERNAME_OCCUPIED(401, HttpStatus.CONFLICT, "User with the given username already exists"),
    PASSWORD_TOO_SHORT(402, HttpStatus.BAD_REQUEST, "Password too short"),
    INVALID_EMAIL(403, HttpStatus.BAD_REQUEST, "Incorrect email address format"),
    EMAIL_OCCUPIED(404, HttpStatus.CONFLICT, "User with the given email already exists"),
    TOKEN_EXPIRED(405, HttpStatus.BAD_REQUEST, "Token expired"),
    EMPTY_TITLE(406, HttpStatus.BAD_REQUEST, "Title can't be empty"),
    INVALID_DIFFICULTY(407, HttpStatus.BAD_REQUEST, "Difficulty must be in range (1-5)"),
    INVALID_PLATES(408, HttpStatus.BAD_REQUEST, "Plates number must be positive value"),
    INVALID_PREPARATION_TIME(409, HttpStatus.BAD_REQUEST, "Preparation time must be positive value"),
    EMPTY_CONTENT(410, HttpStatus.BAD_REQUEST, "Comment content can't be empty");

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
