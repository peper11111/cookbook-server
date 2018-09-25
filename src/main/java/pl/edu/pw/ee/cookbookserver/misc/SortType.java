package pl.edu.pw.ee.cookbookserver.misc;

public enum SortType {
    ASC("asc"),
    DESC("desc");

    private final String value;

    SortType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
