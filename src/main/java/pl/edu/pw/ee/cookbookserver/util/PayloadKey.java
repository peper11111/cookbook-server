package pl.edu.pw.ee.cookbookserver.util;

public enum PayloadKey {

    USERNAME("username"),
    PASSWORD("password"),
    UUID("token"),
    EMAIL("email"),
    NAME("name"),
    BIOGRAPHY("biography"),
    AVATAR_ID("avatarId"),
    BANNER_ID("bannerId"),
    RECIPE_ID("recipeId"),
    TITLE("title"),
    LEAD("lead"),
    CUISINE_ID("cuisineId"),
    DIFFICULTY("difficulty"),
    PLATES("plates"),
    PREPARATION_TIME("preparationTime"),
    CONTENT("content"),
    PARENT_ID("parentId");

    private final String value;

    PayloadKey(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
