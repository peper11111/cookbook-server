package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity current();
    ResponseEntity read(Long id);
    ResponseEntity modify(Long id, JSONObject payload) throws Exception;
    ResponseEntity follow(Long id);
    ResponseEntity recipes(Long id);
}
