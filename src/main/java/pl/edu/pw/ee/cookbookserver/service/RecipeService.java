package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;
import pl.edu.pw.ee.cookbookserver.dto.RecipeDto;

public interface RecipeService {

    ResponseEntity read(Long id) throws Exception;
    ResponseEntity create(RecipeDto recipeDto) throws Exception;
}
