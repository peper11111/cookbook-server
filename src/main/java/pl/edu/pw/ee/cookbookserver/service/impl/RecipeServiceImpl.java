package pl.edu.pw.ee.cookbookserver.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.Properties;
import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.CommentDto;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Comment;
import pl.edu.pw.ee.cookbookserver.entity.Cuisine;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.CommentHelper;
import pl.edu.pw.ee.cookbookserver.helper.PayloadHelper;
import pl.edu.pw.ee.cookbookserver.helper.RecipeHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.PayloadKey;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.repository.CommentRepository;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.RecipeService;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecipeServiceImpl implements RecipeService {

    private CommentHelper commentHelper;
    private CommentRepository commentRepository;
    private PayloadHelper payloadHelper;
    private Properties properties;
    private RecipeHelper recipeHelper;
    private RecipeRepository recipeRepository;
    private UserHelper userHelper;
    private UserRepository userRepository;

    @Autowired
    public RecipeServiceImpl(CommentHelper commentHelper, CommentRepository commentRepository,
                             PayloadHelper payloadHelper, Properties properties, RecipeHelper recipeHelper,
                             RecipeRepository recipeRepository, UserHelper userHelper, UserRepository userRepository) {
        this.commentHelper = commentHelper;
        this.commentRepository = commentRepository;
        this.payloadHelper = payloadHelper;
        this.properties = properties;
        this.recipeHelper = recipeHelper;
        this.recipeRepository = recipeRepository;
        this.userHelper = userHelper;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity readAll(JSONObject payload) throws Exception {
        Stream<Recipe> stream = StreamSupport.stream(recipeRepository.findAll().spliterator(), false);

        if (payload.has(PayloadKey.CUISINE_ID.value())) {
            Cuisine cuisine = payloadHelper.getValidCuisine(payload);
            stream = stream.filter(recipe -> recipe.getCuisine().getId().equals(cuisine.getId()));
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

        long page = payload.has(PayloadKey.PAGE.value()) ? payloadHelper.getValidPage(payload) : 1;
        stream = stream.skip(properties.getPageSize() * (page - 1)).limit(properties.getPageSize());

        Iterable<Recipe> recipes = stream.collect(Collectors.toList());
        Collection<BasicRecipeDto> basicRecipeDtos = recipeHelper.mapRecipeToBasicRecipeDto(recipes);
        return ResponseEntity.status(HttpStatus.OK).body(basicRecipeDtos);
    }

    @Override
    public ResponseEntity create(JSONObject payload) throws Exception {
        User currentUser = userHelper.getCurrentUser();

        Recipe recipe = new Recipe();
        recipe.setAuthor(currentUser);
        if (payload.has(PayloadKey.BANNER_ID.value())) {
            recipe.setBanner(payloadHelper.getValidBanner(payload));
        }
        recipe.setTitle(payloadHelper.getValidTitle(payload));

        String leadKey = PayloadKey.DESCRIPTION.value();
        if (payload.has(leadKey)) {
            recipe.setDescription(payload.optString(leadKey));
        }

        recipe.setCuisine(payloadHelper.getValidCuisine(payload));
        recipe.setDifficulty(payloadHelper.getValidDifficulty(payload));
        recipe.setPlates(payloadHelper.getValidPlates(payload));
        recipe.setPreparationTime(payloadHelper.getValidPreparationTime(payload));
        recipe.setIngredients(payloadHelper.getValidIngredients(payload));
        recipe.setSteps(payloadHelper.getValidSteps(payload));
        recipeRepository.save(recipe);

        return ResponseEntity.status(HttpStatus.CREATED).body(recipe.getId());
    }

    @Override
    public ResponseEntity read(Long id) throws Exception {
        Recipe recipe = recipeHelper.getRecipe(id);
        RecipeDto recipeDto = recipeHelper.mapRecipeToRecipeDto(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(recipeDto);
    }

    @Override
    public ResponseEntity delete(Long id) throws Exception {
        User currentUser = userHelper.getCurrentUser();
        Recipe recipe = recipeHelper.getRecipe(id);

        if (!currentUser.getId().equals(recipe.getAuthor().getId())) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }

        recipeRepository.delete(recipe);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity like(Long id) throws Exception {
        User currentUser = userHelper.getCurrentUser();
        Recipe recipe = recipeHelper.getRecipe(id);

        if (currentUser.getId().equals(recipe.getAuthor().getId())) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }

        Collection<User> likes = recipe.getLikes();
        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        recipeRepository.save(recipe);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity favourite(Long id) throws Exception {
        User currentUser = userHelper.getCurrentUser();
        Recipe recipe = recipeHelper.getRecipe(id);

        if (currentUser.getId().equals(recipe.getAuthor().getId())) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }

        Collection<Recipe> favourites = currentUser.getFavourites();
        if (favourites.contains(recipe)) {
            favourites.remove(recipe);
        } else {
            favourites.add(recipe);
        }

        userRepository.save(currentUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity readComments(Long id) throws Exception {
        Recipe recipe = recipeHelper.getRecipe(id);
        Iterable<Comment> comments = commentRepository.findByRecipeAndParentIsNull(recipe);
        Collection<CommentDto> commentDtos = commentHelper.mapCommentToCommentDto(comments);
        return ResponseEntity.status(HttpStatus.OK).body(commentDtos);
    }
}
