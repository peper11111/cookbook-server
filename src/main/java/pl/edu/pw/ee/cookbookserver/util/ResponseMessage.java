package pl.edu.pw.ee.cookbookserver.util;

public enum ResponseMessage {

    MISSING_USERNAME("error.missing-username"),
    MISSING_PASSWORD("error.missing-password"),
    MISSING_EMAIL("error.missing-email"),
    MISSING_UUID("error.missing-token"),
    USERNAME_OCCUPIED("error.username-occupied"),
    PASSWORD_TOO_SHORT("error.password-too-short"),
    INVALID_EMAIL("error.invalid-email"),
    EMAIL_OCCUPIED("error.email-occupied"),
    TOKEN_EXPIRED("error.token-expired"),
    INTERNAL_SERVER_ERROR("error.internal-server-error");

    private final String value;

    ResponseMessage(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
