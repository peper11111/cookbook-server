package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    ResponseEntity create(MultipartFile file) throws Exception;
    ResponseEntity read(Long id) throws IOException;
    ResponseEntity delete(Long id) throws Exception;
}
