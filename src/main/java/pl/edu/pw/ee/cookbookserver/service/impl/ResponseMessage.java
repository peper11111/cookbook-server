package pl.edu.pw.ee.cookbookserver.service.impl;

public enum ResponseMessage {

    MISSING_USERNAME("error.missing-username"),
    MISSING_PASSWORD("error.missing-password"),
    MISSING_TOKEN("error.missing-token"),
    TOKEN_EXPIRED("error.token-expired");

    private final String value;

    ResponseMessage(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
