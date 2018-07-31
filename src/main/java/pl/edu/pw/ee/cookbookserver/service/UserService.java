package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;
import pl.edu.pw.ee.cookbookserver.entity.User;

import java.util.Map;

public interface UserService {

    ResponseEntity current();
    ResponseEntity read(Long id);
    ResponseEntity modify(Long id, Map userMap);
    ResponseEntity follow(Long id);
    ResponseEntity recipes(Long id);
}
