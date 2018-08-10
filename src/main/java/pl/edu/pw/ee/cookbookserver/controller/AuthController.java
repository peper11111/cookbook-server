package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ee.cookbookserver.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody JSONObject payload, @RequestHeader("Origin") String origin) throws JSONException {
        return authService.register(payload, origin);
    }

    @PostMapping("/verify")
    public ResponseEntity verify(@RequestBody JSONObject payload) throws JSONException {
        return authService.verify(payload);
    }

    @PostMapping("/reset")
    public ResponseEntity reset(@RequestBody JSONObject payload, @RequestHeader("Origin") String origin) throws JSONException {
        return authService.reset(payload, origin);
    }

    @PostMapping("/confirm")
    public ResponseEntity confirm(@RequestBody JSONObject payload) throws JSONException {
        return authService.confirm(payload);
    }
}
