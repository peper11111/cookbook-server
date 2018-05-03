package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ee.cookbookserver.service.UploadService;

@RestController
@RequestMapping("/uploads")
public class UploadController {

    private UploadService uploadService;

    @Autowired
    public UploadController (UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping("/{filename}")
    public ResponseEntity read(@PathVariable String filename) {
        return uploadService.read(filename);
    }
}
