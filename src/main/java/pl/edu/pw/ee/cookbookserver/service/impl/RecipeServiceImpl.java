package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.CuisineRepository;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.service.RecipeService;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import java.util.Optional;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private CuisineRepository cuisineRepository;
    private RecipeRepository recipeRepository;
    private UploadRepository uploadRepository;
    private UserService userService;

    @Autowired
    public RecipeServiceImpl(CuisineRepository cuisineRepository, RecipeRepository recipeRepository, UploadRepository uploadRepository, UserService userService) {
        this.cuisineRepository = cuisineRepository;
        this.recipeRepository = recipeRepository;
        this.uploadRepository = uploadRepository;
        this.userService = userService;
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

    @Override
    public ResponseEntity create(RecipeDto recipeDto) {
        User currentUser = userService.getCurrentUser();
        Recipe recipe = new Recipe();
        recipe.setAuthor(currentUser);
        recipe.setBanner(uploadRepository.findById(recipeDto.getBannerId()).orElse(null));
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCuisine(cuisineRepository.findById(recipeDto.getCuisineId()).orElse(null));
        recipe.setDifficulty(recipeDto.getDifficulty());
        recipe.setPlates(recipeDto.getPlates());
        recipe.setPreparationTime(recipeDto.getPreparationTime());
        recipeRepository.save(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe.getId());
    }
}
