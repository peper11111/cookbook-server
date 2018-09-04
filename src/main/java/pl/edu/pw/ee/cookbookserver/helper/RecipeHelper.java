package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;

import java.time.ZoneOffset;

@Component
public class RecipeHelper {

    private UserHelper userHelper;

    @Autowired
    public RecipeHelper(UserHelper userHelper) {
        this.userHelper = userHelper;
    }

    public BasicRecipeDto recipeToBasicRecipeDto(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        BasicRecipeDto basicRecipeDto = new BasicRecipeDto();
        basicRecipeDto.setId(recipe.getId());
        basicRecipeDto.setAuthor(userHelper.userToBasicUserDto(recipe.getAuthor()));
        if (recipe.getBanner() != null) {
            basicRecipeDto.setBannerId(recipe.getBanner().getId());
        }
        basicRecipeDto.setTitle(recipe.getTitle());
        basicRecipeDto.setCreationTime(recipe.getCreationTime().toInstant(ZoneOffset.UTC).toEpochMilli());

        return basicRecipeDto;
    }

    public RecipeDto recipeToRecipeDto(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setCreationTime(recipe.getCreationTime().toInstant(ZoneOffset.UTC).toEpochMilli());
        recipeDto.setAuthorId(recipe.getAuthor().getId());
        if (recipe.getBanner() != null) {
            recipeDto.setBannerId(recipe.getBanner().getId());
        }
        recipeDto.setTitle(recipe.getTitle());
        recipeDto.setLead(recipe.getLead());
        recipeDto.setCuisineId(recipe.getCuisine().getId());
        recipeDto.setDifficulty(recipe.getDifficulty());
        recipeDto.setPlates(recipe.getPlates());
        recipeDto.setPreparationTime(recipe.getPreparationTime());
        return recipeDto;
    }
}
