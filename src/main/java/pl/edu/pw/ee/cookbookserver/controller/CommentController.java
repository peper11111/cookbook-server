package pl.edu.pw.ee.cookbookserver.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ee.cookbookserver.service.CommentService;

import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody String body) throws Exception {
        return commentService.create(new JSONObject(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Long id) throws Exception {
        return commentService.read(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity modify(@PathVariable Long id, @RequestBody String body) throws Exception {
        return commentService.modify(id, new JSONObject(body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) throws Exception {
        return commentService.delete(id);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity readComments(@PathVariable Long id, @RequestParam Map<String, String> params) throws Exception {
        return commentService.readComments(id, new JSONObject(params));
    }
}
