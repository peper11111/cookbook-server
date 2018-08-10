package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import pl.edu.pw.ee.cookbookserver.dto.AuthDto;

public interface AuthService {

    ResponseEntity register(AuthDto authDto, String origin);
    ResponseEntity verify(AuthDto authDto);
    ResponseEntity reset(JSONObject payload, String origin) throws JSONException;
    ResponseEntity confirm(AuthDto authDto);
}
