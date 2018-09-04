package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ee.cookbookserver.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public ResponseEntity current() throws Exception {
        return userService.current();
    }

    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Long id) throws Exception {
        return userService.read(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity modify(@PathVariable Long id, @RequestBody String body) throws Exception {
        return userService.modify(id, new JSONObject(body));
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity follow(@PathVariable Long id) throws Exception {
        return userService.follow(id);
    }

    @GetMapping("/{id}/recipes")
    public ResponseEntity recipes(@PathVariable Long id) throws Exception {
        return userService.recipes(id);
    }
}
