package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import pl.edu.pw.ee.cookbookserver.CookbookServerProperties;
import pl.edu.pw.ee.cookbookserver.service.UploadService;

import java.io.File;
import java.io.FileInputStream;

@Service
public class UploadServiceImpl implements UploadService {

    private String uploadFolder;

    @Autowired
    public UploadServiceImpl (CookbookServerProperties cookbookServerProperties) {
        this.uploadFolder = cookbookServerProperties.getUploadFolder();
    }

    @Override
    public ResponseEntity read(String filename) {
        try {
            FileInputStream fis = new FileInputStream(new File(uploadFolder, filename));
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(StreamUtils.copyToByteArray(fis));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
