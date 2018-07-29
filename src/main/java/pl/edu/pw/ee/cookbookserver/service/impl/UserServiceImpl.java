package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.dto.CurrentUserDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private RecipeRepository recipeRepository;
    private UploadRepository uploadRepository;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(RecipeRepository recipeRepository, UploadRepository uploadRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.uploadRepository = uploadRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity current() {
        User currentUser = getCurrentUser();
        CurrentUserDto currentUserDto = new CurrentUserDto();
        currentUserDto.setId(currentUser.getId());
        currentUserDto.setUsername(currentUser.getUsername());
        currentUserDto.setAuthorities(currentUser.getAuthorities());
        return ResponseEntity.status(HttpStatus.OK).body(currentUserDto);
    }

    @Override
    public ResponseEntity read(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalUser.get();
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setBiography(user.getBiography());
        if (user.getAvatar() != null) {
            userDto.setAvatarId(user.getAvatar().getId());
        }
        if (user.getBanner() != null) {
            userDto.setBannerId(user.getBanner().getId());
        }
        User currentUser = getCurrentUser();
        userDto.setFollowing(user.getFollowers().contains(currentUser));
        userDto.setFollowed((long) user.getFollowed().size());
        userDto.setFollowers((long) user.getFollowers().size());

        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @Override
    public ResponseEntity modify(Long id, Map userMap) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalUser.get();
        User currentUser = getCurrentUser();
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
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalUser.get();
        User currentUser = getCurrentUser();
        if (currentUser.getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!currentUser.getFollowed().contains(user)) {
            currentUser.getFollowed().add(user);
        } else {
            currentUser.getFollowed().remove(user);
        }
        userRepository.save(currentUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity recipes(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalUser.get();
        Collection<RecipeDto> recipeDtoList = new ArrayList<>();
        Iterable<Recipe> recipes = recipeRepository.findByAuthor(user);
        for (Recipe recipe : recipes) {
            RecipeDto recipeDto = new RecipeDto();
            recipeDto.setAuthorId(recipe.getAuthor().getId());
            if (recipe.getBanner() != null) {
                recipeDto.setBannerId(recipe.getBanner().getId());
            }
            recipeDto.setTitle(recipe.getTitle());
            recipeDto.setDescription(recipe.getDescription());
            recipeDto.setCuisineId(recipe.getCuisine().getId());
            recipeDto.setDifficulty(recipe.getDifficulty());
            recipeDto.setPlates(recipe.getPlates());
            recipeDto.setPreparationTime(recipe.getPreparationTime());
            recipeDtoList.add(recipeDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(recipeDtoList);
    }

    public User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.getId());
        return userRepository.findById(user.getId()).get();
    }
}
