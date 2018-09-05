package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.util.Error;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

import java.time.ZoneOffset;

@Component
public class RecipeHelper {

    private RecipeRepository recipeRepository;
    private UserHelper userHelper;

    @Autowired
    public RecipeHelper(RecipeRepository recipeRepository, UserHelper userHelper) {
        this.recipeRepository = recipeRepository;
        this.userHelper = userHelper;
    }

    public BasicRecipeDto mapRecipeToBasicRecipeDto(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        BasicRecipeDto basicRecipeDto = new BasicRecipeDto();
        basicRecipeDto.setId(recipe.getId());
        basicRecipeDto.setAuthor(userHelper.mapUserToBasicUserDto(recipe.getAuthor()));
        if (recipe.getBanner() != null) {
            basicRecipeDto.setBannerId(recipe.getBanner().getId());
        }
        basicRecipeDto.setTitle(recipe.getTitle());
        basicRecipeDto.setCreationTime(recipe.getCreationTime().toInstant(ZoneOffset.UTC).toEpochMilli());

        return basicRecipeDto;
    }

    public RecipeDto mapRecipeToRecipeDto(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setCreationTime(recipe.getCreationTime().toInstant(ZoneOffset.UTC).toEpochMilli());
        recipeDto.setAuthor(userHelper.mapUserToBasicUserDto(recipe.getAuthor()));
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

    public Recipe getRecipe(Long id) throws ProcessingException {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) {
            throw new ProcessingException(Error.RECIPE_NOT_FOUND);
        }
        return recipe;
    }
}
