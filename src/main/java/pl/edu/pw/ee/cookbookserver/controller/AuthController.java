package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ee.cookbookserver.dto.AuthDto;
import pl.edu.pw.ee.cookbookserver.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/check")
    public ResponseEntity check() {
        return authService.check();
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
    public ResponseEntity reset(@RequestBody AuthDto authDto) {
        return authService.reset(authDto);
    }

    @PostMapping("/confirm")
    public ResponseEntity confirm(@RequestBody AuthDto authDto) {
        return authService.confirm(authDto);
    }
}
