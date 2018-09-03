package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.mapper.RecipeMapper;
import pl.edu.pw.ee.cookbookserver.repository.CuisineRepository;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.service.RecipeService;

import java.time.LocalDateTime;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private CuisineRepository cuisineRepository;
    private RecipeMapper recipeMapper;
    private RecipeRepository recipeRepository;
    private UploadRepository uploadRepository;
    private UserHelper userHelper;

    @Autowired
    public RecipeServiceImpl(CuisineRepository cuisineRepository, RecipeMapper recipeMapper,
                             RecipeRepository recipeRepository, UploadRepository uploadRepository,
                             UserHelper userHelper) {
        this.cuisineRepository = cuisineRepository;
        this.recipeMapper = recipeMapper;
        this.recipeRepository = recipeRepository;
        this.uploadRepository = uploadRepository;
        this.userHelper = userHelper;
    }

    @Override
    public ResponseEntity read(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        RecipeDto recipeDto = recipeMapper.recipeToRecipeDto(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(recipeDto);
    }

    @Override
    public ResponseEntity create(RecipeDto recipeDto) throws Exception {
        Recipe recipe = new Recipe();
        recipe.setCreationTime(LocalDateTime.now());
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
