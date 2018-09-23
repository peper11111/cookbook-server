package pl.edu.pw.ee.cookbookserver.misc;

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
    BANNER_NOT_FOUND(205, HttpStatus.NOT_FOUND, "Banner with the given id not found"),
    CUISINE_NOT_FOUND(206, HttpStatus.NOT_FOUND, "Cuisine with the given id not found"),
    COMMENT_NOT_FOUND(207, HttpStatus.NOT_FOUND, "Comment with the given id not found"),

    MISSING_USERNAME(300, HttpStatus.BAD_REQUEST, "Missing 'username' field in payload"),
    MISSING_PASSWORD(301, HttpStatus.BAD_REQUEST, "Missing 'password' field in payload"),
    MISSING_EMAIL(302, HttpStatus.BAD_REQUEST, "Missing 'email' field in payload"),
    MISSING_UUID(303, HttpStatus.BAD_REQUEST, "Missing 'uuid' field in payload"),
    MISSING_AVATAR_ID(304, HttpStatus.BAD_REQUEST, "missing 'avatarId' field in payload"),
    MISSING_BANNER_ID(305, HttpStatus.BAD_REQUEST, "Missing 'bannerId' in payload"),
    MISSING_TITLE(306, HttpStatus.BAD_REQUEST, "Missing 'title' field in payload"),
    MISSING_CUISINE_ID(307, HttpStatus.BAD_REQUEST, "Missing 'cuisineId' field in payload"),
    MISSING_DIFFICULTY(308, HttpStatus.BAD_REQUEST, "Missing 'difficulty' field in payload"),
    MISSING_PLATES(309, HttpStatus.BAD_REQUEST, "Missing 'plates' field in payload"),
    MISSING_PREPARATION_TIME(310, HttpStatus.BAD_REQUEST, "Missing 'preparationTime' field in payload"),
    MISSING_RECIPE_ID(311, HttpStatus.BAD_REQUEST, "Missing 'recipeId' field in payload"),
    MISSING_CONTENT(312, HttpStatus.BAD_REQUEST, "Missing 'content' field in payload"),
    MISSING_PARENT_ID(313, HttpStatus.BAD_REQUEST, "Missing 'parentId' field id in payload"),
    MISSING_INGREDIENTS(313, HttpStatus.BAD_REQUEST, "Missing 'ingredients' field in payload"),
    MISSING_STEPS(314, HttpStatus.BAD_REQUEST, "Missing 'steps' field in payload"),
    MISSING_LOGIN(315, HttpStatus.BAD_REQUEST, "Missing 'login' field in payload"),
    MISSING_MIN_DIFFICULTY(316, HttpStatus.BAD_REQUEST, "Missing 'minDifficulty' in payload"),
    MISSING_MAX_DIFFICULTY(317, HttpStatus.BAD_REQUEST, "Missing 'maxDifficulty' in payload"),
    MISSING_MIN_PLATES(316, HttpStatus.BAD_REQUEST, "Missing 'minPlates' in payload"),
    MISSING_MAX_PLATES(317, HttpStatus.BAD_REQUEST, "Missing 'maxPlates' in payload"),

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
    EMPTY_CONTENT(410, HttpStatus.BAD_REQUEST, "Comment content can't be empty"),
    EMPTY_INGREDIENTS(411, HttpStatus.BAD_REQUEST, "Ingredients array can't be empty"),
    EMPTY_STEPS(412, HttpStatus.BAD_REQUEST, "Steps array can't be empty"),
    INVALID_FILE_TYPE(413, HttpStatus.BAD_REQUEST, "File type must be one of the following ('gif', 'jpeg', 'png')"),
    INVALID_PARENT_ID(414, HttpStatus.BAD_REQUEST, "Parent comment does not belong to this recipe"),
    INVALID_MIN_DIFFICULTY(415, HttpStatus.BAD_REQUEST, "Minimum difficulty must be in range (1-5)"),
    INVALID_MAX_DIFFICULTY(416, HttpStatus.BAD_REQUEST, "Maximum difficulty must be in range (1-5)"),
    INVALID_MIN_PLATES(417, HttpStatus.BAD_REQUEST, "Minimum plates must be positive value"),
    INVALID_MAX_PLATES(418, HttpStatus.BAD_REQUEST, "Maximum plates must be positive value");

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
