package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface RecipeService {

    ResponseEntity create(JSONObject payload) throws Exception;
    ResponseEntity read(Long id) throws Exception;
    ResponseEntity delete(Long id) throws Exception;
    ResponseEntity readComments(Long id) throws Exception;
}
