package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;

public interface UploadService {

    ResponseEntity read(String filename);
}
