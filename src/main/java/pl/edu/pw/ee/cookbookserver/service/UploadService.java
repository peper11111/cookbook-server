package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    ResponseEntity create(MultipartFile file);
    ResponseEntity read(String filename);
    ResponseEntity delete(String filename);
}
