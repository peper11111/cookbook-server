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
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.mapper.RecipeMapper;
import pl.edu.pw.ee.cookbookserver.mapper.UserMapper;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
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

    private RecipeMapper recipeMapper;
    private RecipeRepository recipeRepository;
    private UploadRepository uploadRepository;
    private UserHelper userHelper;
    private UserMapper userMapper;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(RecipeMapper recipeMapper, RecipeRepository recipeRepository,
                           UploadRepository uploadRepository, UserHelper userHelper, UserMapper userMapper,
                           UserRepository userRepository) {
        this.recipeMapper = recipeMapper;
        this.recipeRepository = recipeRepository;
        this.uploadRepository = uploadRepository;
        this.userHelper = userHelper;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity current() throws Exception {
        User currentUser = userHelper.getCurrentUser();
        BasicUserDto basicUserDto = userMapper.userToBasicUserDto(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body(basicUserDto);
    }

    @Override
    public ResponseEntity read(Long id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserDto userDto = userMapper.userToUserDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @Override
    public ResponseEntity modify(Long id, JSONObject payload) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User currentUser = userHelper.getCurrentUser();
        if (!user.getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String emailKey = PayloadKey.EMAIL.value();
        if (payload.has(emailKey)) {
            user.setEmail(payload.getString(emailKey));
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new ProcessingException(Error.EMAIL_OCCUPIED);
            }
        }

        String passwordKey = PayloadKey.PASSWORD.value();
        if (payload.has(passwordKey)) {
            user.setPassword(payload.getString(passwordKey));
        }

        String nameKey = PayloadKey.NAME.value();
        if (payload.has(nameKey)) {
            user.setName(payload.getString(nameKey));
        }

        String biographyKey = PayloadKey.BIOGRAPHY.value();
        if (payload.has(biographyKey)) {
            user.setBiography(payload.getString(biographyKey));
        }

        String avatarIdKey = PayloadKey.AVATAR_ID.value();
        if (payload.has(avatarIdKey)) {
            long avatarId = payload.getLong(avatarIdKey);
            user.setAvatar(uploadRepository.findById(avatarId).orElse(null));
        }

        String bannerIdKey = PayloadKey.BANNER_ID.value();
        if (payload.has(bannerIdKey)) {
            long bannerId = payload.getLong(bannerIdKey);
            user.setBanner(uploadRepository.findById(bannerId).orElse(null));
        }
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity follow(Long id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User currentUser = userHelper.getCurrentUser();
        if (currentUser.getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (currentUser.getFollowed().contains(user)) {
            currentUser.getFollowed().remove(user);
        } else {
            currentUser.getFollowed().add(user);
        }
        userRepository.save(currentUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity recipes(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Collection<BasicRecipeDto> basicRecipeDtoList = new ArrayList<>();
        Iterable<Recipe> recipes = recipeRepository.findByAuthor(user);
        for (Recipe recipe : recipes) {
            BasicRecipeDto basicRecipeDto = recipeMapper.recipeToBasicRecipeDto(recipe);
            basicRecipeDtoList.add(basicRecipeDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(basicRecipeDtoList);
    }
}
