package pl.edu.pw.ee.cookbookserver.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.Properties;
import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.BasicUserDto;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.PayloadHelper;
import pl.edu.pw.ee.cookbookserver.helper.RecipeHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.PayloadKey;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private PayloadHelper payloadHelper;
    private Properties properties;
    private RecipeHelper recipeHelper;
    private UserHelper userHelper;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PayloadHelper payloadHelper, Properties properties, RecipeHelper recipeHelper,
                           UserHelper userHelper, UserRepository userRepository) {
        this.payloadHelper = payloadHelper;
        this.properties = properties;
        this.recipeHelper = recipeHelper;
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
    public ResponseEntity readRecipes(Long id, JSONObject payload) throws Exception {
        User user = userHelper.getUser(id);
        Stream<Recipe> stream = user.getRecipes().stream();

        long page = payload.has(PayloadKey.PAGE.value()) ? payloadHelper.getValidPage(payload) : 1;
        stream = stream.skip(properties.getPageSize() * (page - 1)).limit(properties.getPageSize());

        Iterable<Recipe> recipes = stream.collect(Collectors.toList());
        Collection<BasicRecipeDto> basicRecipeDtos = recipeHelper.mapRecipeToBasicRecipeDto(recipes);
        return ResponseEntity.status(HttpStatus.OK).body(basicRecipeDtos);
    }

    @Override
    public ResponseEntity readFavourites(Long id, JSONObject payload) throws Exception {
        User user = userHelper.getUser(id);
        Stream<Recipe> stream = user.getFavourites().stream();

        long page = payload.has(PayloadKey.PAGE.value()) ? payloadHelper.getValidPage(payload) : 1;
        stream = stream.skip(properties.getPageSize() * (page - 1)).limit(properties.getPageSize());

        Iterable<Recipe> recipes = stream.collect(Collectors.toList());
        Collection<BasicRecipeDto> basicRecipeDtos = recipeHelper.mapRecipeToBasicRecipeDto(recipes);
        return ResponseEntity.status(HttpStatus.OK).body(basicRecipeDtos);
    }
}
