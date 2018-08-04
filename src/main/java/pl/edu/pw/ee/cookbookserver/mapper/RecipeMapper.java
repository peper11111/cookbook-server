package pl.edu.pw.ee.cookbookserver.mapper;

import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;

public interface RecipeMapper {

    BasicRecipeDto recipeToBasicRecipeDto(Recipe recipe);
    RecipeDto recipeToRecipeDto(Recipe recipe);
}
