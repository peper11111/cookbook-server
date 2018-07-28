package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public ResponseEntity current() {
        return userService.current();
    }

    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Long id) {
        return userService.read(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity modify(@PathVariable Long id, @RequestBody Map userMap) {
        return userService.modify(id, userMap);
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity follow(@PathVariable Long id) {
        return userService.follow(id);
    }

    @GetMapping("/{id}/recipes")
    public ResponseEntity recipes(@PathVariable Long id) {
        return userService.recipes(id);
    }
}
