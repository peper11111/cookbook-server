package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.ee.cookbookserver.service.UploadService;

import java.io.IOException;

@RestController
@RequestMapping("/uploads")
public class UploadController {

    private UploadService uploadService;

    @Autowired
    public UploadController (UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    public ResponseEntity create(@RequestParam MultipartFile file) throws Exception {
        return uploadService.create(file);
    }

    @GetMapping("/{id}")
    public ResponseEntity read(@PathVariable Long id) throws Exception {
        return uploadService.read(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) throws Exception {
        return uploadService.delete(id);
    }
}
