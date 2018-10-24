package pl.edu.pw.ee.cookbookserver.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.BasicUserDto;
import pl.edu.pw.ee.cookbookserver.dto.UploadDto;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.Upload;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.*;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.PayloadKey;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private PayloadHelper payloadHelper;
    private RecipeHelper recipeHelper;
    private RecipeRepository recipeRepository;
    private StreamHelper streamHelper;
    private UploadHelper uploadHelper;
    private UserHelper userHelper;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PayloadHelper payloadHelper, RecipeHelper recipeHelper, RecipeRepository recipeRepository,
                           StreamHelper streamHelper, UploadHelper uploadHelper, UserHelper userHelper,
                           UserRepository userRepository) {
        this.payloadHelper = payloadHelper;
        this.recipeHelper = recipeHelper;
        this.recipeRepository = recipeRepository;
        this.streamHelper = streamHelper;
        this.uploadHelper = uploadHelper;
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
    public ResponseEntity search(JSONObject payload) throws Exception {
        String query = payloadHelper.getValidQuery(payload).toLowerCase();

        Stream<User> stream = StreamSupport.stream(userRepository.findAll().spliterator(), false);
        stream = stream.filter(user -> user.getUsername().toLowerCase().contains(query)
                || (user.getName() != null && user.getName().toLowerCase().contains(query)));
        stream = streamHelper.applyPagination(payload, stream);

        Iterable<User> users = stream.collect(Collectors.toList());
        Collection<BasicUserDto> basicUserDtos = userHelper.mapUserToBasicUserDto(users);
        return ResponseEntity.status(HttpStatus.OK).body(basicUserDtos);
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

        if (payload.has(PayloadKey.NAME.value())) {
            user.setName(payloadHelper.getValidName(payload));
        }

        if (payload.has(PayloadKey.BIOGRAPHY.value())) {
            user.setBiography(payloadHelper.getValidBiography(payload));
        }

        if (payload.has(PayloadKey.AVATAR_ID.value())) {
            user.setAvatar(payloadHelper.getValidAvatar(payload));
        }

        if (payload.has(PayloadKey.BANNER_ID.value())) {
            user.setBanner(payloadHelper.getValidBanner(payload));
        }

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

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity readImages(Long id, JSONObject payload) throws Exception {
        User user = userHelper.getUser(id);
        Stream<Upload> stream = user.getImages().stream();

        Comparator comparator = Comparator.comparing(Upload::getCreationTime);
        stream = streamHelper.applySorting(payload, stream, comparator);
        stream = streamHelper.applyPagination(payload, stream);

        Iterable<Upload> uploads = stream.collect(Collectors.toList());
        Collection<UploadDto> uploadDtos = uploadHelper.mapUploadToUploadDto(uploads);
        return ResponseEntity.status(HttpStatus.OK).body(uploadDtos);
    }

    @Override
    public ResponseEntity readRecommended(Long id, JSONObject payload) throws Exception {
        User user = userHelper.getUser(id);
        Stream<Recipe> stream = StreamSupport.stream(recipeRepository.findByAuthorIn(user.getFollowed()).spliterator(), false);

        stream = streamHelper.applyRecipeFiltering(payload, stream);
        Comparator comparator = Comparator.comparing(Recipe::getCreationTime);
        stream = streamHelper.applySorting(payload, stream, comparator);
        stream = streamHelper.applyPagination(payload, stream);

        Iterable<Recipe> recipes = stream.collect(Collectors.toList());
        Collection<BasicRecipeDto> basicRecipeDtos = recipeHelper.mapRecipeToBasicRecipeDto(recipes);
        return ResponseEntity.status(HttpStatus.OK).body(basicRecipeDtos);
    }

    @Override
    public ResponseEntity readRecipes(Long id, JSONObject payload) throws Exception {
        User user = userHelper.getUser(id);
        Stream<Recipe> stream = user.getRecipes().stream();

        Comparator comparator = Comparator.comparing(Recipe::getCreationTime);
        stream = streamHelper.applySorting(payload, stream, comparator);
        stream = streamHelper.applyPagination(payload, stream);

        Iterable<Recipe> recipes = stream.collect(Collectors.toList());
        Collection<BasicRecipeDto> basicRecipeDtos = recipeHelper.mapRecipeToBasicRecipeDto(recipes);
        return ResponseEntity.status(HttpStatus.OK).body(basicRecipeDtos);
    }

    @Override
    public ResponseEntity readFavourites(Long id, JSONObject payload) throws Exception {
        User user = userHelper.getUser(id);
        Stream<Recipe> stream = user.getFavouriteRecipes().stream();

        stream = streamHelper.applyRecipeFiltering(payload, stream);
        Comparator comparator = Comparator.comparing(Recipe::getCreationTime);
        stream = streamHelper.applySorting(payload, stream, comparator);
        stream = streamHelper.applyPagination(payload, stream);

        Iterable<Recipe> recipes = stream.collect(Collectors.toList());
        Collection<BasicRecipeDto> basicRecipeDtos = recipeHelper.mapRecipeToBasicRecipeDto(recipes);
        return ResponseEntity.status(HttpStatus.OK).body(basicRecipeDtos);
    }
}
