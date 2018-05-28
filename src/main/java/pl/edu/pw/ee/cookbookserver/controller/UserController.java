package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ee.cookbookserver.dto.DetailsDto;
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
    public ResponseEntity current() {
        return userService.current();
    }

    @GetMapping("/{id}/details")
    public ResponseEntity readDetails(@PathVariable Long id) {
        return userService.readDetails(id);
    }

    @PutMapping("/{id}/details")
    public ResponseEntity update(@PathVariable Long id, @RequestBody DetailsDto detailsDto) {
        return userService.updateDetails(id, detailsDto);
    }
}
