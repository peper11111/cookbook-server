package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    ResponseEntity create(MultipartFile file) throws Exception;
    ResponseEntity read(Long id) throws Exception;
    ResponseEntity delete(Long id) throws Exception;
    ResponseEntity readThumbnail(Long id) throws Exception;
}
