package pl.edu.pw.ee.cookbookserver.helper;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.Properties;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.misc.PayloadKey;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.misc.SortType;

import java.util.Comparator;
import java.util.stream.Stream;

@Component
public class StreamHelper {

    private PayloadHelper payloadHelper;
    private Properties properties;

    @Autowired
    public StreamHelper(PayloadHelper payloadHelper, Properties properties) {
        this.payloadHelper = payloadHelper;
        this.properties = properties;
    }

    public Stream<Recipe> applyRecipeFiltering(JSONObject payload, Stream<Recipe> stream) throws ProcessingException {
        if (payload.has(PayloadKey.CUISINE_ID.value())) {
            long cuisineId = payloadHelper.getValidCuisine(payload).getId();
            stream = stream.filter(recipe -> recipe.getCuisine().getId().equals(cuisineId));
        }

        if (payload.has(PayloadKey.CATEGORY_ID.value())) {
            long categoryId = payloadHelper.getValidCategory(payload).getId();
            stream = stream.filter(recipe -> recipe.getCategory().getId().equals(categoryId));
        }

        if (payload.has(PayloadKey.MIN_DIFFICULTY.value())) {
            int minDifficulty = payloadHelper.getValidMinDifficulty(payload);
            stream = stream.filter(recipe -> recipe.getDifficulty() >= minDifficulty);
        }

        if (payload.has(PayloadKey.MAX_DIFFICULTY.value())) {
            int maxDifficulty = payloadHelper.getValidMaxDifficulty(payload);
            stream = stream.filter(recipe -> recipe.getDifficulty() <= maxDifficulty);
        }

        if (payload.has(PayloadKey.MIN_PLATES.value())) {
            int minPlates = payloadHelper.getValidMinPlates(payload);
            stream = stream.filter(recipe -> recipe.getPlates() >= minPlates);
        }

        if (payload.has(PayloadKey.MAX_PLATES.value())) {
            int maxPlates = payloadHelper.getValidMaxPlates(payload);
            stream = stream.filter(recipe -> recipe.getPlates() <= maxPlates);
        }

        if (payload.has(PayloadKey.MIN_PREPARATION_TIME.value())) {
            int minPreparationTime = payloadHelper.getValidMinPreparationTime(payload);
            stream = stream.filter(recipe -> recipe.getPreparationTime() >= minPreparationTime);
        }

        if (payload.has(PayloadKey.MAX_PREPARATION_TIME.value())) {
            int maxPreparationTime = payloadHelper.getValidMaxPreparationTime(payload);
            stream = stream.filter(recipe -> recipe.getPreparationTime() <= maxPreparationTime);
        }

        return stream;
    }

    public Stream applySorting(JSONObject payload, Stream stream, Comparator comparator) throws ProcessingException {
        SortType sortType = payload.has(PayloadKey.SORT.value()) ? payloadHelper.getValidSortType(payload) : SortType.DESC;
        if (sortType == SortType.ASC) {
            return stream.sorted(comparator);
        } else {
            return stream.sorted(comparator.reversed());
        }
    }

    public Stream applyPagination(JSONObject payload, Stream stream) throws ProcessingException {
        long page = payload.has(PayloadKey.PAGE.value()) ? payloadHelper.getValidPage(payload) : 1;
        return stream.skip(properties.getPageSize() * (page - 1)).limit(properties.getPageSize());
    }
}
