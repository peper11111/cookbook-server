package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;

public interface RecipeService {

    ResponseEntity read(Long id);
}
