package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;

public interface CommentService {

    ResponseEntity delete(Long id) throws Exception;
}
