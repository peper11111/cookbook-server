package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
}
