package pl.edu.pw.ee.cookbookserver.service;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity current() throws Exception;
    ResponseEntity search(JSONObject payload) throws Exception;
    ResponseEntity read(Long id) throws Exception;
    ResponseEntity modify(Long id, JSONObject payload) throws Exception;
    ResponseEntity follow(Long id) throws Exception;
    ResponseEntity readRecipes(Long id, JSONObject payload) throws Exception;
    ResponseEntity readFavourites(Long id, JSONObject payload) throws Exception;
}
