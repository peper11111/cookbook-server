package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity register(JSONObject payload, String origin) throws Exception;
    ResponseEntity verify(JSONObject payload) throws Exception;
    ResponseEntity reset(JSONObject payload, String origin) throws Exception;
    ResponseEntity confirm(JSONObject payload) throws Exception;
}
