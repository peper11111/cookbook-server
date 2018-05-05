package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.ee.cookbookserver.CookbookServerProperties;
import pl.edu.pw.ee.cookbookserver.service.UploadService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    private String uploadFolder;

    @Autowired
    public UploadServiceImpl (CookbookServerProperties cookbookServerProperties) {
        this.uploadFolder = cookbookServerProperties.getUploadFolder();
    }

    @Override
    public ResponseEntity create(MultipartFile file) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(file.getBytes());
            BufferedImage bufferedImage = ImageIO.read(in);
            if (bufferedImage.getColorModel().hasAlpha()) {
                BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
                bufferedImage = newBufferedImage;
            }
            String filename = UUID.randomUUID().toString() + ".jpg";
            ImageIO.write(bufferedImage, "jpg", new File(uploadFolder, filename));
            return ResponseEntity.ok().body(filename);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity read(String filename) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedImage bufferedImage = ImageIO.read(new File(uploadFolder, filename + ".jpg"));
            ImageIO.write(bufferedImage, "jpg", out);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
