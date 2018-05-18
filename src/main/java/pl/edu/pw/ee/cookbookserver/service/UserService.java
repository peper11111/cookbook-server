package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;

public interface UserService {

    ResponseEntity current();
    ResponseEntity read(Long id);
    ResponseEntity update(Long id, UserDto userDto);
}
