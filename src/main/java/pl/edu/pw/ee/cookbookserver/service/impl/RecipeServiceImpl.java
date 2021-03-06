package pl.edu.pw.ee.cookbookserver.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.CommentDto;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.*;
import pl.edu.pw.ee.cookbookserver.helper.*;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.PayloadKey;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.repository.CommentRepository;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.service.RecipeService;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecipeServiceImpl implements RecipeService {

    private CommentHelper commentHelper;
    private CommentRepository commentRepository;
    private PayloadHelper payloadHelper;
    private RecipeHelper recipeHelper;
    private RecipeRepository recipeRepository;
    private StreamHelper streamHelper;
    private UserHelper userHelper;

    @Autowired
    public RecipeServiceImpl(CommentHelper commentHelper, CommentRepository commentRepository,
                             PayloadHelper payloadHelper, RecipeHelper recipeHelper, RecipeRepository recipeRepository,
                             StreamHelper streamHelper, UserHelper userHelper) {
        this.commentHelper = commentHelper;
        this.commentRepository = commentRepository;
        this.payloadHelper = payloadHelper;
        this.recipeHelper = recipeHelper;
        this.recipeRepository = recipeRepository;
        this.streamHelper = streamHelper;
        this.userHelper = userHelper;
    }

    @Override
    public ResponseEntity readAll(JSONObject payload) throws Exception {
        Stream<Recipe> stream = StreamSupport.stream(recipeRepository.findAll().spliterator(), false);

        stream = streamHelper.applyRecipeFiltering(payload, stream);
        Comparator comparator = Comparator.comparing(Recipe::getCreationTime);
        stream = streamHelper.applySorting(payload, stream, comparator);
        stream = streamHelper.applyPagination(payload, stream);

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

        if (payload.has(PayloadKey.DESCRIPTION.value())) {
            recipe.setDescription(payloadHelper.getValidDescription(payload));
        }

        recipe.setCuisine(payloadHelper.getValidCuisine(payload));
        recipe.setCategory(payloadHelper.getValidCategory(payload));
        recipe.setDifficulty(payloadHelper.getValidDifficulty(payload));
        recipe.setPlates(payloadHelper.getValidPlates(payload));
        recipe.setPreparationTime(payloadHelper.getValidPreparationTime(payload));
        recipe.setIngredients(payloadHelper.getValidIngredients(payload));
        recipe.setSteps(payloadHelper.getValidSteps(payload));
        recipeRepository.save(recipe);

        return ResponseEntity.status(HttpStatus.CREATED).body(recipe.getId());
    }

    @Override
    public ResponseEntity search(JSONObject payload) throws Exception {
        String query = payloadHelper.getValidQuery(payload).toLowerCase();

        Stream<Recipe> stream = StreamSupport.stream(recipeRepository.findAll().spliterator(), false);
        stream = stream.filter(recipe -> recipe.getTitle().toLowerCase().contains(query));

        Comparator comparator = Comparator.comparing(Recipe::getCreationTime);
        stream = streamHelper.applySorting(payload, stream, comparator);
        stream = streamHelper.applyPagination(payload, stream);

        Iterable<Recipe> recipes = stream.collect(Collectors.toList());
        Collection<BasicRecipeDto> basicRecipeDtos = recipeHelper.mapRecipeToBasicRecipeDto(recipes);
        return ResponseEntity.status(HttpStatus.OK).body(basicRecipeDtos);
    }

    @Override
    public ResponseEntity read(Long id) throws Exception {
        Recipe recipe = recipeHelper.getRecipe(id);
        RecipeDto recipeDto = recipeHelper.mapRecipeToRecipeDto(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(recipeDto);
    }

    @Override
    public ResponseEntity modify(Long id, JSONObject payload) throws Exception {
        User currentUser = userHelper.getCurrentUser();
        Recipe recipe = recipeHelper.getRecipe(id);

        if (!currentUser.getId().equals(recipe.getAuthor().getId())) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }

        if (payload.has(PayloadKey.BANNER_ID.value())) {
            recipe.setBanner(payloadHelper.getValidBanner(payload));
        }

        if (payload.has(PayloadKey.TITLE.value())) {
            recipe.setTitle(payloadHelper.getValidTitle(payload));
        }

        if (payload.has(PayloadKey.DESCRIPTION.value())) {
            recipe.setDescription(payloadHelper.getValidDescription(payload));
        }

        if (payload.has(PayloadKey.CUISINE_ID.value())) {
            recipe.setCuisine(payloadHelper.getValidCuisine(payload));
        }

        if (payload.has(PayloadKey.CATEGORY_ID.value())) {
            recipe.setCategory(payloadHelper.getValidCategory(payload));
        }

        if (payload.has(PayloadKey.DIFFICULTY.value())) {
            recipe.setDifficulty(payloadHelper.getValidDifficulty(payload));
        }

        if (payload.has(PayloadKey.PLATES.value())) {
            recipe.setPlates(payloadHelper.getValidPlates(payload));
        }

        if (payload.has(PayloadKey.PREPARATION_TIME.value())) {
            recipe.setPreparationTime(payloadHelper.getValidPreparationTime(payload));
        }

        if (payload.has(PayloadKey.INGREDIENTS.value())) {
            recipe.setIngredients(payloadHelper.getValidIngredients(payload));
        }

        if (payload.has(PayloadKey.STEPS.value())) {
            recipe.setSteps(payloadHelper.getValidSteps(payload));
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity favourite(Long id) throws Exception {
        User currentUser = userHelper.getCurrentUser();
        Recipe recipe = recipeHelper.getRecipe(id);

        if (currentUser.getId().equals(recipe.getAuthor().getId())) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }

        Collection<User> favourites = recipe.getFavourites();
        if (favourites.contains(currentUser)) {
            favourites.remove(currentUser);
        } else {
            favourites.add(currentUser);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity readComments(Long id, JSONObject payload) throws Exception {
        Recipe recipe = recipeHelper.getRecipe(id);
        Stream<Comment> stream = StreamSupport.stream(commentRepository.findByRecipeAndParentIsNull(recipe).spliterator(), false);

        Comparator comparator = Comparator.comparing(Comment::getCreationTime);
        stream = streamHelper.applySorting(payload, stream, comparator);
        stream = streamHelper.applyPagination(payload, stream);

        Iterable<Comment> comments = stream.collect(Collectors.toList());
        Collection<CommentDto> commentDtos = commentHelper.mapCommentToCommentDto(comments);
        return ResponseEntity.status(HttpStatus.OK).body(commentDtos);
    }
}
