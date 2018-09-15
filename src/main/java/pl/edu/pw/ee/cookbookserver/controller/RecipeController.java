package pl.edu.pw.ee.cookbookserver.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ee.cookbookserver.service.RecipeService;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody String body) throws Exception {
        return recipeService.create(new JSONObject(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Long id) throws Exception {
        return recipeService.read(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) throws Exception {
        return recipeService.delete(id);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity like(@PathVariable Long id) throws Exception {
        return recipeService.like(id);
    }

    @PostMapping("/{id}/favourite")
    public ResponseEntity favourite(@PathVariable Long id) throws Exception {
        return recipeService.favourite(id);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity readComments(@PathVariable Long id) throws Exception {
        return recipeService.readComments(id);
    }
}
