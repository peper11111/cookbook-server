package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pw.ee.cookbookserver.dto.AuthDto;
import pl.edu.pw.ee.cookbookserver.service.AuthService;

@RestController
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AuthDto authDto) {
        return authService.register(authDto);
    }

    @PostMapping("/verify")
    public ResponseEntity verify(@RequestBody AuthDto authDto) {
        return authService.verify(authDto);
    }

    @PostMapping("/reset")
    public ResponseEntity reset() {
        return authService.reset();
    }

    @PostMapping("/confirm")
    public ResponseEntity confirm() {
        return authService.confirm();
    }
}
