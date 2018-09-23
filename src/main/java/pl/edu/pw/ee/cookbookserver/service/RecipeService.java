package pl.edu.pw.ee.cookbookserver.service;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface RecipeService {

    ResponseEntity readAll(JSONObject payload) throws Exception;
    ResponseEntity create(JSONObject payload) throws Exception;
    ResponseEntity read(Long id) throws Exception;
    ResponseEntity delete(Long id) throws Exception;
    ResponseEntity like(Long id) throws Exception;
    ResponseEntity favourite(Long id) throws Exception;
    ResponseEntity readComments(Long id) throws Exception;
}
