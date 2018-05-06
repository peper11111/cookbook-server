package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.ee.cookbookserver.service.UploadService;

@RestController
@RequestMapping("/uploads")
public class UploadController {

    private UploadService uploadService;

    @Autowired
    public UploadController (UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    public ResponseEntity create(@RequestParam MultipartFile file) {
        return uploadService.create(file);
    }

    @GetMapping("/{filename}")
    public ResponseEntity read(@PathVariable String filename) {
        return uploadService.read(filename);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity delete(@PathVariable String filename) {
        return uploadService.delete(filename);
    }
}
