package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    ResponseEntity create(JSONObject payload) throws Exception;
    ResponseEntity delete(Long id) throws Exception;
}
