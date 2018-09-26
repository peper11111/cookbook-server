package pl.edu.pw.ee.cookbookserver.misc;

public enum SearchDtoType {
    USER("user"),
    RECIPE("recipe");

    private final String value;

    SearchDtoType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
