package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity register(@RequestBody String body, @RequestHeader("Origin") String origin) throws Exception {
        return authService.register(new JSONObject(body), origin);
    }

    @PostMapping("/verify")
    public ResponseEntity verify(@RequestBody String body) throws Exception {
        return authService.verify(new JSONObject(body));
    }

    @PostMapping("/reset")
    public ResponseEntity reset(@RequestBody String body, @RequestHeader("Origin") String origin) throws Exception {
        return authService.reset(new JSONObject(body), origin);
    }

    @PostMapping("/confirm")
    public ResponseEntity confirm(@RequestBody String body) throws Exception {
        return authService.confirm(new JSONObject(body));
    }
}
