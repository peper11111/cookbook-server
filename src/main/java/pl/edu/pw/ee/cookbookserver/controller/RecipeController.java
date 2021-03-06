package pl.edu.pw.ee.cookbookserver.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ee.cookbookserver.service.RecipeService;

import java.util.Map;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity readAll(@RequestParam Map<String, String> params) throws Exception {
        return recipeService.readAll(new JSONObject(params));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody String body) throws Exception {
        return recipeService.create(new JSONObject(body));
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam Map<String, String> params) throws Exception {
        return recipeService.search(new JSONObject(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Long id) throws Exception {
        return recipeService.read(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity modify(@PathVariable Long id, @RequestBody String body) throws Exception {
        return recipeService.modify(id, new JSONObject(body));
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
    public ResponseEntity readComments(@PathVariable Long id, @RequestParam Map<String, String> params) throws Exception {
        return recipeService.readComments(id, new JSONObject(params));
    }
}
