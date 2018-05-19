package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.ee.cookbookserver.CookbookProperties;
import pl.edu.pw.ee.cookbookserver.entity.Upload;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.service.UploadService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    private String uploadFolder;
    private UploadRepository uploadRepository;

    @Autowired
    public UploadServiceImpl (CookbookProperties cookbookProperties, UploadRepository uploadRepository) {
        this.uploadFolder = cookbookProperties.getUploadFolder();
        this.uploadRepository = uploadRepository;
    }

    @Override
    public ResponseEntity create(MultipartFile file) throws IOException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Upload upload = new Upload();
        upload.setFilename(this.writeImage(file.getBytes()));
        upload.setOwner(currentUser);
        upload.setCreationTime(LocalDateTime.now());
        uploadRepository.save(upload);
        return ResponseEntity.status(HttpStatus.OK).body(upload.getId());
    }

    @Override
    public ResponseEntity read(Long id) throws IOException {
        Optional<Upload> optionalUpload = uploadRepository.findById(id);
        if (!optionalUpload.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Upload upload = optionalUpload.get();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(this.readImage(upload.getFilename()));
    }

    @Override
    public ResponseEntity delete(Long id) {
        Optional<Upload> optionalUpload = uploadRepository.findById(id);
        if (!optionalUpload.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Upload upload = optionalUpload.get();
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
