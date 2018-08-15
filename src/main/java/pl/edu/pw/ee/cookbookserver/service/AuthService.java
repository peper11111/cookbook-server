package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity register(JSONObject payload, String origin);
    ResponseEntity verify(JSONObject payload);
    ResponseEntity reset(JSONObject payload, String origin);
    ResponseEntity confirm(JSONObject payload);
}
