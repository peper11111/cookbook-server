package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.CookbookHelper;
import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.BasicUserDto;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.mapper.RecipeMapper;
import pl.edu.pw.ee.cookbookserver.mapper.UserMapper;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private CookbookHelper cookbookHelper;
    private RecipeMapper recipeMapper;
    private RecipeRepository recipeRepository;
    private UploadRepository uploadRepository;
    private UserMapper userMapper;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(CookbookHelper cookbookHelper, RecipeMapper recipeMapper, RecipeRepository recipeRepository,
                           UploadRepository uploadRepository, UserMapper userMapper, UserRepository userRepository) {
        this.cookbookHelper = cookbookHelper;
        this.recipeMapper = recipeMapper;
        this.recipeRepository = recipeRepository;
        this.uploadRepository = uploadRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity current() {
        User currentUser = cookbookHelper.getCurrentUser();
        BasicUserDto basicUserDto = userMapper.userToBasicUserDto(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body(basicUserDto);
    }

    @Override
    public ResponseEntity read(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserDto userDto = userMapper.userToUserDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @Override
    public ResponseEntity modify(Long id, Map userMap) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User currentUser = cookbookHelper.getCurrentUser();
        if (!user.getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (userMap.containsKey("email")) {
            String email = (String) userMap.get("email");
            if (email != null && !userRepository.findByEmail(email).isPresent()) {
                user.setEmail(email);
            }
        }
        if (userMap.containsKey("password")) {
            String password = (String) userMap.get("password");
            if (password != null) {
                user.setPassword(new BCryptPasswordEncoder().encode(password));
            }
        }
        if (userMap.containsKey("name")) {
            String name = (String) userMap.get("name");
            user.setName(name);
        }
        if (userMap.containsKey("biography")) {
            String biography = (String) userMap.get("biography");
            user.setBiography(biography);
        }
        if (userMap.containsKey("avatarId")) {
            Number avatarId = (Number) userMap.get("avatarId");
            user.setAvatar(avatarId != null
                    ? uploadRepository.findById(avatarId.longValue()).orElse(null)
                    : null);
        }
        if (userMap.containsKey("bannerId")) {
            Number bannerId = (Number) userMap.get("bannerId");
            user.setBanner(bannerId != null
                    ? uploadRepository.findById(bannerId.longValue()).orElse(null)
                    : null);
        }
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity follow(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User currentUser = cookbookHelper.getCurrentUser();
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
