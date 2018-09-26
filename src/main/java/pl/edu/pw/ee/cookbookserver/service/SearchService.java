package pl.edu.pw.ee.cookbookserver.service;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface SearchService {

    ResponseEntity search(JSONObject payload) throws Exception;
}
