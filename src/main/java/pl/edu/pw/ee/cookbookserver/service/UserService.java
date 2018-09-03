package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity current() throws Exception;
    ResponseEntity read(Long id) throws Exception;
    ResponseEntity modify(Long id, JSONObject payload) throws Exception;
    ResponseEntity follow(Long id) throws Exception;
    ResponseEntity recipes(Long id);
}
