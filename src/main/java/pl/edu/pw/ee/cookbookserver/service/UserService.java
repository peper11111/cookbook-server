package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity read(Long id);
}
