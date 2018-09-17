package pl.edu.pw.ee.cookbookserver.service;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity register(JSONObject payload, String origin) throws Exception;
    ResponseEntity registerResend(JSONObject payload, String origin) throws Exception;
    ResponseEntity registerVerify(JSONObject payload) throws Exception;
    ResponseEntity reset(JSONObject payload, String origin) throws Exception;
    ResponseEntity resetResend(JSONObject payload, String origin) throws Exception;
    ResponseEntity resetConfirm(JSONObject payload) throws Exception;
}
