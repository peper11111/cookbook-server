package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.CommentDto;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Comment;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.CommentHelper;
import pl.edu.pw.ee.cookbookserver.helper.PayloadHelper;
import pl.edu.pw.ee.cookbookserver.helper.RecipeHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.repository.CommentRepository;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.service.RecipeService;
import pl.edu.pw.ee.cookbookserver.util.Error;
import pl.edu.pw.ee.cookbookserver.util.PayloadKey;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private CommentHelper commentHelper;
    private CommentRepository commentRepository;
    private PayloadHelper payloadHelper;
    private RecipeHelper recipeHelper;
    private RecipeRepository recipeRepository;
    private UserHelper userHelper;

    @Autowired
    public RecipeServiceImpl(CommentHelper commentHelper, CommentRepository commentRepository,
                             PayloadHelper payloadHelper, RecipeHelper recipeHelper, RecipeRepository recipeRepository,
                             UserHelper userHelper) {
        this.commentHelper = commentHelper;
        this.commentRepository = commentRepository;
        this.payloadHelper = payloadHelper;
        this.recipeHelper = recipeHelper;
        this.recipeRepository = recipeRepository;
        this.userHelper = userHelper;
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

        String leadKey = PayloadKey.LEAD.value();
        if (payload.has(leadKey)) {
            recipe.setLead(payload.optString(leadKey));
        }

        recipe.setCuisine(payloadHelper.getValidCuisine(payload));
        recipe.setDifficulty(payloadHelper.getValidDifficulty(payload));
        recipe.setPlates(payloadHelper.getValidPlates(payload));
        recipe.setPreparationTime(payloadHelper.getValidPreparationTime(payload));
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
    public ResponseEntity readComments(Long id) throws Exception {
        Recipe recipe = recipeHelper.getRecipe(id);

        Collection<CommentDto> commentDtoList = new ArrayList<>();
        Iterable<Comment> comments = commentRepository.findByRecipeAndParentIsNull(recipe);
        for (Comment comment: comments) {
            CommentDto commentDto = commentHelper.mapCommentToCommentDto(comment);
            commentDtoList.add(commentDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(commentDtoList);
    }
}
