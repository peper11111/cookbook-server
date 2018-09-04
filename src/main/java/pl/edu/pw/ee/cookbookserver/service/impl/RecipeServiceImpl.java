package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.helper.RecipeHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.repository.CuisineRepository;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.service.RecipeService;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private CuisineRepository cuisineRepository;
    private RecipeHelper recipeHelper;
    private RecipeRepository recipeRepository;
    private UploadRepository uploadRepository;
    private UserHelper userHelper;

    @Autowired
    public RecipeServiceImpl(CuisineRepository cuisineRepository, RecipeHelper recipeHelper,
                             RecipeRepository recipeRepository, UploadRepository uploadRepository,
                             UserHelper userHelper) {
        this.cuisineRepository = cuisineRepository;
        this.recipeHelper = recipeHelper;
        this.recipeRepository = recipeRepository;
        this.uploadRepository = uploadRepository;
        this.userHelper = userHelper;
    }

    @Override
    public ResponseEntity read(Long id) throws Exception {
        Recipe recipe = recipeHelper.getRecipe(id);
        RecipeDto recipeDto = recipeHelper.mapRecipeToRecipeDto(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(recipeDto);
    }

    @Override
    public ResponseEntity create(RecipeDto recipeDto) throws Exception {
        Recipe recipe = new Recipe();
        recipe.setAuthor(userHelper.getCurrentUser());
        recipe.setBanner(uploadRepository.findById(recipeDto.getBannerId()).orElse(null));
        recipe.setTitle(recipeDto.getTitle());
        recipe.setLead(recipeDto.getLead());
        recipe.setCuisine(cuisineRepository.findById(recipeDto.getCuisineId()).orElse(null));
        recipe.setDifficulty(recipeDto.getDifficulty());
        recipe.setPlates(recipeDto.getPlates());
        recipe.setPreparationTime(recipeDto.getPreparationTime());
        recipeRepository.save(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe.getId());
    }
}
