package pl.edu.pw.ee.cookbookserver.misc;

public enum PayloadKey {

    LOGIN("login"),
    USERNAME("username"),
    PASSWORD("password"),
    UUID("uuid"),
    EMAIL("email"),
    NAME("name"),
    BIOGRAPHY("biography"),
    AVATAR_ID("avatarId"),
    BANNER_ID("bannerId"),
    RECIPE_ID("recipeId"),
    TITLE("title"),
    DESCRIPTION("description"),
    CUISINE_ID("cuisineId"),
    DIFFICULTY("difficulty"),
    MIN_DIFFICULTY("minDifficulty"),
    MAX_DIFFICULTY("maxDifficulty"),
    PLATES("plates"),
    MIN_PLATES("minPlates"),
    MAX_PLATES("maxPlates"),
    PREPARATION_TIME("preparationTime"),
    CONTENT("content"),
    PARENT_ID("parentId"),
    INGREDIENTS("ingredients"),
    STEPS("steps"),
    PAGE("page");

    private final String value;

    PayloadKey(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
