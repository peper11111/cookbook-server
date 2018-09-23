package pl.edu.pw.ee.cookbookserver.helper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.entity.*;
import pl.edu.pw.ee.cookbookserver.repository.*;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.PayloadKey;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class PayloadHelper {

    private CommentRepository commentRepository;
    private CuisineRepository cuisineRepository;
    private RecipeRepository recipeRepository;
    private UploadRepository uploadRepository;
    private UserRepository userRepository;

    @Autowired
    public PayloadHelper(CommentRepository commentRepository, CuisineRepository cuisineRepository,
                         RecipeRepository recipeRepository, UploadRepository uploadRepository,
                         UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.cuisineRepository = cuisineRepository;
        this.recipeRepository = recipeRepository;
        this.uploadRepository = uploadRepository;
        this.userRepository = userRepository;
    }

    public List JSONArrayToList(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.opt(i));
        }
        return list;
    }

    public String getValidKey(JSONObject payload, PayloadKey payloadKey, Error error) throws ProcessingException {
        String key = payloadKey.value();
        if (!payload.has(key)) {
            throw new ProcessingException(error);
        }
        return key;
    }

    public String getValidString(JSONObject payload, PayloadKey payloadKey, Error error) throws ProcessingException {
        return payload.optString(getValidKey(payload, payloadKey, error));
    }

    public long getValidLong(JSONObject payload, PayloadKey payloadKey, Error error) throws ProcessingException {
        return payload.optLong(getValidKey(payload, payloadKey, error));
    }

    public int getValidInt(JSONObject payload, PayloadKey payloadKey, Error error) throws ProcessingException {
        return payload.optInt(getValidKey(payload, payloadKey, error));
    }

    public JSONArray getValidJSONArray(JSONObject payload, PayloadKey payloadKey, Error error) throws ProcessingException {
        return payload.optJSONArray(getValidKey(payload, payloadKey, error));
    }

    public String getValidUuid(JSONObject payload) throws ProcessingException {
        return getValidString(payload, PayloadKey.UUID, Error.MISSING_UUID);
    }

    public String getValidLogin(JSONObject payload) throws ProcessingException {
        return getValidString(payload, PayloadKey.LOGIN, Error.MISSING_LOGIN);
    }

    public String getValidEmail(JSONObject payload) throws ProcessingException {
        String email = getValidString(payload, PayloadKey.EMAIL, Error.MISSING_EMAIL);
        if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
            throw new ProcessingException(Error.INVALID_EMAIL);
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ProcessingException(Error.EMAIL_OCCUPIED);
        }
        return email;
    }

    public String getValidUsername(JSONObject payload) throws ProcessingException {
        String username = getValidString(payload, PayloadKey.USERNAME, Error.MISSING_USERNAME);
        if (username.isEmpty()) {
            throw new ProcessingException(Error.EMPTY_USERNAME);
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ProcessingException(Error.USERNAME_OCCUPIED);
        }
        return username;
    }

    public String getValidPassword(JSONObject payload) throws ProcessingException {
        String password = getValidString(payload, PayloadKey.PASSWORD, Error.MISSING_PASSWORD);
        if (password.length() < 8) {
            throw new ProcessingException(Error.PASSWORD_TOO_SHORT);
        }
        return password;
    }

    public Upload getValidAvatar(JSONObject payload) throws ProcessingException {
        long avatarId = getValidLong(payload, PayloadKey.AVATAR_ID, Error.MISSING_AVATAR_ID);
        Upload avatar = uploadRepository.findById(avatarId).orElse(null);
        if (avatarId != 0 && avatar == null) {
            throw new ProcessingException(Error.AVATAR_NOT_FOUND);
        }
        return avatar;
    }

    public Upload getValidBanner(JSONObject payload) throws ProcessingException {
        long bannerId = getValidLong(payload, PayloadKey.BANNER_ID, Error.MISSING_BANNER_ID);
        Upload banner = uploadRepository.findById(bannerId).orElse(null);
        if (bannerId != 0 && banner == null) {
            throw new ProcessingException(Error.BANNER_NOT_FOUND);
        }
        return banner;
    }

    public String getValidTitle(JSONObject payload) throws ProcessingException {
        String title = getValidString(payload, PayloadKey.TITLE, Error.MISSING_TITLE);
        if (title.isEmpty()) {
            throw new ProcessingException(Error.EMPTY_TITLE);
        }
        return title;
    }

    public Cuisine getValidCuisine(JSONObject payload) throws ProcessingException {
        long cuisineId = getValidLong(payload, PayloadKey.CUISINE_ID, Error.MISSING_CUISINE_ID);
        Cuisine cuisine = cuisineRepository.findById(cuisineId).orElse(null);
        if (cuisine == null) {
            throw new ProcessingException(Error.CUISINE_NOT_FOUND);
        }
        return cuisine;
    }

    public int getValidDifficulty(JSONObject payload) throws ProcessingException {
        int difficulty = getValidInt(payload, PayloadKey.DIFFICULTY, Error.MISSING_DIFFICULTY);
        if (difficulty < 1 || difficulty > 5) {
            throw new ProcessingException(Error.INVALID_DIFFICULTY);
        }
        return difficulty;
    }

    public int getValidMinDifficulty(JSONObject payload) throws ProcessingException {
        int minDifficulty = getValidInt(payload, PayloadKey.MIN_DIFFICULTY, Error.MISSING_MIN_DIFFICULTY);
        if (minDifficulty < 1 || minDifficulty > 5) {
            throw new ProcessingException(Error.INVALID_MIN_DIFFICULTY);
        }
        return minDifficulty;
    }

    public int getValidMaxDifficulty(JSONObject payload) throws ProcessingException {
        int maxDifficulty = getValidInt(payload, PayloadKey.MAX_DIFFICULTY, Error.MISSING_MAX_DIFFICULTY);
        if (maxDifficulty < 1 || maxDifficulty > 5) {
            throw new ProcessingException(Error.INVALID_MAX_DIFFICULTY);
        }
        return maxDifficulty;
    }

    public int getValidPlates(JSONObject payload) throws ProcessingException {
        int plates = getValidInt(payload, PayloadKey.PLATES, Error.MISSING_PLATES);
        if (plates < 1) {
            throw new ProcessingException(Error.INVALID_PLATES);
        }
        return plates;
    }

    public int getValidMinPlates(JSONObject payload) throws ProcessingException {
        int minPlates = getValidInt(payload, PayloadKey.MIN_PLATES, Error.MISSING_MIN_PLATES);
        if (minPlates < 1) {
            throw new ProcessingException(Error.INVALID_MIN_PLATES);
        }
        return minPlates;
    }

    public int getValidMaxPlates(JSONObject payload) throws ProcessingException {
        int maxPlates = getValidInt(payload, PayloadKey.MAX_PLATES, Error.MISSING_MAX_PLATES);
        if (maxPlates < 1) {
            throw new ProcessingException(Error.INVALID_MAX_PLATES);
        }
        return maxPlates;
    }

    public int getValidPreparationTime(JSONObject payload) throws ProcessingException {
        int preparationTime = getValidInt(payload, PayloadKey.PREPARATION_TIME, Error.MISSING_PREPARATION_TIME);
        if (preparationTime < 1) {
            throw new ProcessingException(Error.INVALID_PREPARATION_TIME);
        }
        return preparationTime;
    }

    public Recipe getValidRecipe(JSONObject payload) throws ProcessingException {
        long recipeId = getValidLong(payload, PayloadKey.RECIPE_ID, Error.MISSING_RECIPE_ID);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        if (recipe == null) {
            throw new ProcessingException(Error.RECIPE_NOT_FOUND);
        }
        return recipe;
    }

    public String getValidContent(JSONObject payload) throws ProcessingException {
        String content = getValidString(payload, PayloadKey.CONTENT, Error.MISSING_CONTENT);
        if (content.isEmpty()) {
            throw new ProcessingException(Error.EMPTY_CONTENT);
        }
        return content;
    }

    public Comment getValidParent(JSONObject payload) throws ProcessingException {
        long parentId = getValidLong(payload, PayloadKey.PARENT_ID, Error.MISSING_PARENT_ID);
        Comment parent = commentRepository.findById(parentId).orElse(null);
        if (parent == null) {
            throw new ProcessingException(Error.COMMENT_NOT_FOUND);
        }
        return parent;
    }

    public Collection<String> getValidIngredients(JSONObject payload) throws ProcessingException {
        JSONArray ingredients = getValidJSONArray(payload, PayloadKey.INGREDIENTS, Error.MISSING_INGREDIENTS);
        if (ingredients == null || ingredients.length() == 0) {
            throw new ProcessingException(Error.EMPTY_INGREDIENTS);
        }
        return JSONArrayToList(ingredients);
    }

    public Collection<String> getValidSteps(JSONObject payload) throws ProcessingException {
        JSONArray steps = getValidJSONArray(payload, PayloadKey.STEPS, Error.MISSING_STEPS);
        if (steps == null || steps.length() == 0) {
            throw new ProcessingException(Error.EMPTY_STEPS);
        }
        return JSONArrayToList(steps);
    }
}
