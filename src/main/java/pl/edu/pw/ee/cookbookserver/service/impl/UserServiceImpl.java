package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.BasicUserDto;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.PayloadHelper;
import pl.edu.pw.ee.cookbookserver.helper.RecipeHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.UserService;
import pl.edu.pw.ee.cookbookserver.util.Error;
import pl.edu.pw.ee.cookbookserver.util.PayloadKey;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private PayloadHelper payloadHelper;
    private RecipeHelper recipeHelper;
    private RecipeRepository recipeRepository;
    private UserHelper userHelper;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PayloadHelper payloadHelper, RecipeHelper recipeHelper, RecipeRepository recipeRepository,
                           UserHelper userHelper, UserRepository userRepository) {
        this.payloadHelper = payloadHelper;
        this.recipeHelper = recipeHelper;
        this.recipeRepository = recipeRepository;
        this.userHelper = userHelper;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity current() throws Exception {
        User currentUser = userHelper.getCurrentUser();
        BasicUserDto basicUserDto = userHelper.mapUserToBasicUserDto(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body(basicUserDto);
    }

    @Override
    public ResponseEntity read(Long id) throws Exception {
        User user = userHelper.getUser(id);
        UserDto userDto = userHelper.mapUserToUserDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @Override
    public ResponseEntity modify(Long id, JSONObject payload) throws Exception {
        User currentUser = userHelper.getCurrentUser();
        User user = userHelper.getUser(id);

        if (!currentUser.getId().equals(user.getId())) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }

        if (payload.has(PayloadKey.EMAIL.value())) {
            user.setEmail(payloadHelper.getValidEmail(payload));
        }

        if (payload.has(PayloadKey.PASSWORD.value())) {
            user.setPassword(payloadHelper.getValidPassword(payload));
        }

        String nameKey = PayloadKey.NAME.value();
        if (payload.has(nameKey)) {
            user.setName(payload.optString(nameKey));
        }

        String biographyKey = PayloadKey.BIOGRAPHY.value();
        if (payload.has(biographyKey)) {
            user.setBiography(payload.optString(biographyKey));
        }

        if (payload.has(PayloadKey.AVATAR_ID.value())) {
            user.setAvatar(payloadHelper.getValidAvatar(payload));
        }

        if (payload.has(PayloadKey.BANNER_ID.value())) {
            user.setBanner(payloadHelper.getValidBanner(payload));
        }
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity follow(Long id) throws Exception {
        User currentUser = userHelper.getCurrentUser();
        User user = userHelper.getUser(id);

        if (currentUser.getId().equals(user.getId())) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }

        Collection<User> followers = user.getFollowers();
        if (followers.contains(currentUser)) {
            followers.remove(currentUser);
        } else {
            followers.add(currentUser);
        }
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity recipes(Long id) throws Exception {
        User user = userHelper.getUser(id);

        Collection<BasicRecipeDto> basicRecipeDtoList = new ArrayList<>();
        Iterable<Recipe> recipes = recipeRepository.findByAuthor(user);
        for (Recipe recipe : recipes) {
            BasicRecipeDto basicRecipeDto = recipeHelper.mapRecipeToBasicRecipeDto(recipe);
            basicRecipeDtoList.add(basicRecipeDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(basicRecipeDtoList);
    }
}
