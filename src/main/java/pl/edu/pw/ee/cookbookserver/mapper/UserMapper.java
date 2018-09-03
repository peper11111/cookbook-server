package pl.edu.pw.ee.cookbookserver.mapper;

import pl.edu.pw.ee.cookbookserver.dto.BasicUserDto;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

public interface UserMapper {

    BasicUserDto userToBasicUserDto(User user);
    UserDto userToUserDto(User user) throws ProcessingException;
}
