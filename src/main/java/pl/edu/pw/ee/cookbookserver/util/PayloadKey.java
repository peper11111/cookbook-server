package pl.edu.pw.ee.cookbookserver.util;

public enum PayloadKey {

    USERNAME("username"),
    PASSWORD("password"),
    UUID("token"),
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
