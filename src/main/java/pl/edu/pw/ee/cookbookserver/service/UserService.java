package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;

public interface UserService {
    ResponseEntity register(UserDto userDto);
    ResponseEntity reset(UserDto userDto);
}
