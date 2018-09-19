package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.ee.cookbookserver.Properties;
import pl.edu.pw.ee.cookbookserver.entity.Upload;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.UploadHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.service.UploadService;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class UploadServiceImpl implements UploadService {

    private Properties properties;
    private UploadHelper uploadHelper;
    private UploadRepository uploadRepository;
    private UserHelper userHelper;

    @Autowired
    public UploadServiceImpl(Properties properties, UploadHelper uploadHelper, UploadRepository uploadRepository,
                             UserHelper userHelper) {
        this.properties = properties;
        this.uploadHelper = uploadHelper;
        this.uploadRepository = uploadRepository;
        this.userHelper = userHelper;
    }

    @Override
    public ResponseEntity create(MultipartFile file) throws Exception {
        User currentUser = userHelper.getCurrentUser();

        String[] validFileTypes = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE};
        if (!Arrays.asList(validFileTypes).contains(file.getContentType())) {
            throw new ProcessingException(Error.INVALID_FILE_TYPE);
        }

        Upload upload = new Upload();
        upload.setFilename(this.writeImage(file.getBytes()));
        upload.setOwner(currentUser);
        uploadRepository.save(upload);

        return ResponseEntity.status(HttpStatus.CREATED).body(upload.getId());
    }

    @Override
    public ResponseEntity read(Long id) throws Exception {
        Upload upload = uploadHelper.getUpload(id);
        byte[] image = this.readImage(upload.getFilename());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @Override
    public ResponseEntity delete(Long id) throws Exception {
        User currentUser = userHelper.getCurrentUser();
        Upload upload = uploadHelper.getUpload(id);

        if (!currentUser.getId().equals(upload.getOwner().getId())) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }

        uploadRepository.delete(upload);
        new File(properties.getUploadsPath(), upload.getFilename()).delete();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private byte[] readImage(String filename) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedImage bufferedImage = ImageIO.read(new File(properties.getUploadsPath(), filename));
        ImageIO.write(bufferedImage, "jpg", out);
        return out.toByteArray();
    }

    private String writeImage(byte[] bytes) throws IOException {
        String filename = UUID.randomUUID().toString() + ".jpg";
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = this.removeAlphaChannel(ImageIO.read(in));
        ImageIO.write(bufferedImage, "jpg", new File(properties.getUploadsPath(), filename));
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
