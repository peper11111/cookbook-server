package pl.edu.pw.ee.cookbookserver.service.impl;

public enum PayloadKey {

    USERNAME("username"),
    PASSWORD("password"),
    TOKEN("token"),
    EMAIL("email"),
    NAME("name");

    private final String value;

    PayloadKey(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
