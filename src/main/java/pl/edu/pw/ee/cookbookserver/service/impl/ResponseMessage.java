package pl.edu.pw.ee.cookbookserver.service.impl;

public enum ResponseMessage {

    MISSING_USERNAME("error.missing-username");

    private final String value;

    ResponseMessage(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
