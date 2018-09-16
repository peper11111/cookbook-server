package pl.edu.pw.ee.cookbookserver.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/register/resend")
    public ResponseEntity registerResend(@RequestBody String body, @RequestHeader("Origin") String origin) throws Exception {
        return authService.registerResend(new JSONObject(body), origin);
    }

    @PostMapping("/register/confirm")
    public ResponseEntity registerConfirm(@RequestBody String body) throws Exception {
        return authService.registerConfirm(new JSONObject(body));
    }

    @PostMapping("/reset")
    public ResponseEntity reset(@RequestBody String body, @RequestHeader("Origin") String origin) throws Exception {
        return authService.reset(new JSONObject(body), origin);
    }

    @PostMapping("/reset/resend")
    public ResponseEntity resetResend(@RequestBody String body, @RequestHeader("Origin") String origin) throws Exception {
        return authService.resetResend(new JSONObject(body), origin);
    }

    @PostMapping("/reset/confirm")
    public ResponseEntity resetConfirm(@RequestBody String body) throws Exception {
        return authService.resetConfirm(new JSONObject(body));
    }
}
