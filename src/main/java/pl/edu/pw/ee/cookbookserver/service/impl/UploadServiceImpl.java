package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.ee.cookbookserver.CookbookHelper;
import pl.edu.pw.ee.cookbookserver.CookbookProperties;
import pl.edu.pw.ee.cookbookserver.entity.Upload;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.service.UploadService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class UploadServiceImpl implements UploadService {

    private CookbookHelper cookbookHelper;
    private String uploadFolder;
    private UploadRepository uploadRepository;

    @Autowired
    public UploadServiceImpl(CookbookHelper cookbookHelper, CookbookProperties cookbookProperties,
                             UploadRepository uploadRepository) {
        this.cookbookHelper = cookbookHelper;
        this.uploadFolder = cookbookProperties.getUploadFolder();
        this.uploadRepository = uploadRepository;
    }

    @Override
    public ResponseEntity create(MultipartFile file) throws IOException {
        User currentUser = cookbookHelper.getCurrentUser();
        Upload upload = new Upload();
        upload.setFilename(this.writeImage(file.getBytes()));
        upload.setOwner(currentUser);
        upload.setCreationTime(LocalDateTime.now());
        uploadRepository.save(upload);
        return ResponseEntity.status(HttpStatus.CREATED).body(upload.getId());
    }

    @Override
    public ResponseEntity read(Long id) throws IOException {
        Upload upload = uploadRepository.findById(id).orElse(null);
        if (upload == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(this.readImage(upload.getFilename()));
    }

    @Override
    public ResponseEntity delete(Long id) {
        Upload upload = uploadRepository.findById(id).orElse(null);
        if (upload == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User currentUser = cookbookHelper.getCurrentUser();
        if (!upload.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        uploadRepository.delete(upload);
        new File(uploadFolder, upload.getFilename()).delete();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private byte[] readImage(String filename) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedImage bufferedImage = ImageIO.read(new File(uploadFolder, filename));
        ImageIO.write(bufferedImage, "jpg", out);
        return out.toByteArray();
    }

    private String writeImage(byte[] bytes) throws IOException {
        String filename = UUID.randomUUID().toString() + ".jpg";
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = this.removeAlphaChannel(ImageIO.read(in));
        ImageIO.write(bufferedImage, "jpg", new File(uploadFolder, filename));
        return filename;
    }

    private BufferedImage removeAlphaChannel(BufferedImage originalImage) {
        BufferedImage bufferedImage = originalImage;
        if (originalImage.getColorModel().hasAlpha()) {
            bufferedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);
        }
        return bufferedImage;
    }
}
