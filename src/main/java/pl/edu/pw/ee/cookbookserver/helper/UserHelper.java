package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.dto.BasicUserDto;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.repository.RecipeRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class UserHelper {

    private RecipeRepository recipeRepository;
    private UserRepository userRepository;

    @Autowired
    public UserHelper(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public Collection<BasicUserDto> mapUserToBasicUserDto(Iterable<User> users) {
        if (users == null) {
            return null;
        }
        Collection<BasicUserDto> basicUserDtos = new ArrayList<>();
        for (User user: users) {
            BasicUserDto basicUserDto = mapUserToBasicUserDto(user);
            basicUserDtos.add(basicUserDto);
        }
        return basicUserDtos;
    }

    public BasicUserDto mapUserToBasicUserDto(User user) {
        if (user == null) {
            return null;
        }
        BasicUserDto basicUserDto = new BasicUserDto();
        basicUserDto.setId(user.getId());
        basicUserDto.setUsername(user.getUsername());
        basicUserDto.setName(user.getName());
        if (user.getAvatar() != null) {
            basicUserDto.setAvatarId(user.getAvatar().getId());
        }
        return basicUserDto;
    }

    public UserDto mapUserToUserDto(User user) throws ProcessingException {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setBiography(user.getBiography());
        if (user.getAvatar() != null) {
            userDto.setAvatarId(user.getAvatar().getId());
        }
        if (user.getBanner() != null) {
            userDto.setBannerId(user.getBanner().getId());
        }
        userDto.setIsFollowed(user.getFollowers().contains(getCurrentUser()));
        userDto.setFollowersCount((long) user.getFollowers().size());
        userDto.setFollowedCount(userRepository.countByFollowersContaining(user));
        userDto.setRecipesCount(recipeRepository.countByAuthor(user));
        return userDto;
    }

    public User getCurrentUser() throws ProcessingException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findById(principal.getId()).orElse(null);
        if (currentUser == null) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }
        return currentUser;
    }

    public User getUser(Long id) throws ProcessingException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ProcessingException(Error.USER_NOT_FOUND);
        }
        return user;
    }

    public User getUser(String login) throws ProcessingException {
        User user = userRepository.findByLogin(login).orElse(null);
        if (user == null) {
            throw new ProcessingException(Error.USER_NOT_FOUND);
        }
        return user;
    }
}
