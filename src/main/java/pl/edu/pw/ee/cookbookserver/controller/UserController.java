package pl.edu.pw.ee.cookbookserver.controller;

import org.json.JSONObject;
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
    public ResponseEntity current() throws Exception {
        return userService.current();
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam Map<String, String> params) throws Exception {
        return userService.search(new JSONObject(params));
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
    public ResponseEntity readRecipes(@PathVariable Long id, @RequestParam Map<String, String> params) throws Exception {
        return userService.readRecipes(id, new JSONObject(params));
    }

    @GetMapping("/{id}/favourites")
    public ResponseEntity readFavourites(@PathVariable Long id, @RequestParam Map<String, String> params) throws Exception {
        return userService.readFavourites(id, new JSONObject(params));
    }
}
