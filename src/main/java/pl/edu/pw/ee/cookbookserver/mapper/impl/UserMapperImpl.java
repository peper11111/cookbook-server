package pl.edu.pw.ee.cookbookserver.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.CookbookHelper;
import pl.edu.pw.ee.cookbookserver.dto.BasicUserDto;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.mapper.UserMapper;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

@Component
public class UserMapperImpl implements UserMapper {

    private CookbookHelper cookbookHelper;

    @Autowired
    public UserMapperImpl(CookbookHelper cookbookHelper) {
        this.cookbookHelper = cookbookHelper;
    }

    @Override
    public BasicUserDto userToBasicUserDto(User user) {
        if (user == null) {
            return null;
        }
        BasicUserDto basicUserDto = new BasicUserDto();
        basicUserDto.setId(user.getId());
        basicUserDto.setUsername(user.getUsername());
        if (user.getAvatar() != null) {
            basicUserDto.setAvatarId(user.getAvatar().getId());
        }
        return basicUserDto;
    }

    @Override
    public UserDto userToUserDto(User user) throws ProcessingException {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setBiography(user.getBiography());
        if (user.getAvatar() != null) {
            userDto.setAvatarId(user.getAvatar().getId());
        }
        if (user.getBanner() != null) {
            userDto.setBannerId(user.getBanner().getId());
        }
        User currentUser = cookbookHelper.getCurrentUser();
        userDto.setFollowing(user.getFollowers().contains(currentUser));
        userDto.setFollowed((long) user.getFollowed().size());
        userDto.setFollowers((long) user.getFollowers().size());
        userDto.setRecipes((long) user.getRecipes().size());
        return userDto;
    }
}
