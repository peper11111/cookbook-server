package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRespository;
import pl.edu.pw.ee.cookbookserver.service.RecipeService;

import java.util.Optional;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private RecipeRespository recipeRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRespository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public ResponseEntity read(Long id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (!optionalRecipe.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Recipe recipe = optionalRecipe.get();
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setAuthorId(recipe.getAuthor().getId());
        if (recipe.getBanner() != null) {
            recipeDto.setBannerId(recipe.getBanner().getId());
        }
        recipeDto.setTitle(recipe.getTitle());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setCuisineId(recipe.getCuisine().getId());
        recipeDto.setDifficulty(recipe.getDifficulty());
        recipeDto.setPlates(recipe.getPlates());
        recipeDto.setPreparationTime(recipe.getPreparationTime());

        return ResponseEntity.status(HttpStatus.OK).body(recipeDto);
    }
}
