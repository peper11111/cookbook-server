package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.repository.CommentRepository;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class RecipeHelper {

    private CommentRepository commentRepository;
    private RecipeRepository recipeRepository;
    private UserHelper userHelper;

    @Autowired
    public RecipeHelper(CommentRepository commentRepository, RecipeRepository recipeRepository, UserHelper userHelper) {
        this.commentRepository = commentRepository;
        this.recipeRepository = recipeRepository;
        this.userHelper = userHelper;
    }

    public Collection<BasicRecipeDto> mapRecipeToBasicRecipeDto(Iterable<Recipe> recipes) {
        if (recipes == null) {
            return null;
        }
        Collection<BasicRecipeDto> basicRecipeDtos = new ArrayList<>();
        for (Recipe recipe: recipes) {
            BasicRecipeDto basicRecipeDto = mapRecipeToBasicRecipeDto(recipe);
            basicRecipeDtos.add(basicRecipeDto);
        }
        return basicRecipeDtos;
    }

    public BasicRecipeDto mapRecipeToBasicRecipeDto(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        BasicRecipeDto basicRecipeDto = new BasicRecipeDto();
        basicRecipeDto.setId(recipe.getId());
        basicRecipeDto.setAuthor(userHelper.mapUserToBasicUserDto(recipe.getAuthor()));
        basicRecipeDto.setCreationTime(recipe.getCreationTime());
        if (recipe.getBanner() != null) {
            basicRecipeDto.setBannerId(recipe.getBanner().getId());
        }
        basicRecipeDto.setTitle(recipe.getTitle());
        basicRecipeDto.setDescription(recipe.getDescription());
        basicRecipeDto.setCommentsCount(commentRepository.countByRecipe(recipe));
        basicRecipeDto.setLikesCount((long) recipe.getLikes().size());
        basicRecipeDto.setCategoryId(recipe.getCategory().getId());
        return basicRecipeDto;
    }

    public RecipeDto mapRecipeToRecipeDto(Recipe recipe) throws ProcessingException {
        if (recipe == null) {
            return null;
        }
        User currentUser = userHelper.getCurrentUser();
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipe.getId());
        recipeDto.setCreationTime(recipe.getCreationTime());
        recipeDto.setAuthor(userHelper.mapUserToBasicUserDto(recipe.getAuthor()));
        if (recipe.getBanner() != null) {
            recipeDto.setBannerId(recipe.getBanner().getId());
        }
        recipeDto.setTitle(recipe.getTitle());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setCommentsCount(commentRepository.countByRecipe(recipe));
        recipeDto.setLikesCount((long) recipe.getLikes().size());
        recipeDto.setCategoryId(recipe.getCategory().getId());
        recipeDto.setIsLiked(recipe.getLikes().contains(currentUser));
        recipeDto.setIsFavourite(currentUser.getFavourites().contains(recipe));
        recipeDto.setCuisineId(recipe.getCuisine().getId());
        recipeDto.setDifficulty(recipe.getDifficulty());
        recipeDto.setPlates(recipe.getPlates());
        recipeDto.setPreparationTime(recipe.getPreparationTime());
        recipeDto.setIngredients(recipe.getIngredients());
        recipeDto.setSteps(recipe.getSteps());
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
