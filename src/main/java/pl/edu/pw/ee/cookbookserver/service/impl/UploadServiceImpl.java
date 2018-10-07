package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pw.ee.cookbookserver.entity.Upload;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.UploadHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.service.UploadService;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class UploadServiceImpl implements UploadService {

    private UploadHelper uploadHelper;
    private UploadRepository uploadRepository;
    private UserHelper userHelper;

    @Autowired
    public UploadServiceImpl(UploadHelper uploadHelper, UploadRepository uploadRepository, UserHelper userHelper) {
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

        String filename = UUID.randomUUID().toString() + ".jpg";

        Upload upload = new Upload();
        upload.setFilename(filename);
        upload.setOwner(currentUser);
        uploadRepository.save(upload);

        uploadHelper.writeImage(filename, false, file.getBytes());
        uploadHelper.writeImage(filename, true, file.getBytes());

        return ResponseEntity.status(HttpStatus.CREATED).body(upload.getId());
    }

    @Override
    public ResponseEntity read(Long id) throws Exception {
        Upload upload = uploadHelper.getUpload(id);
        byte[] image = uploadHelper.readImage(upload.getFilename(), false);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @Override
    public ResponseEntity delete(Long id) throws Exception {
        User currentUser = userHelper.getCurrentUser();
        Upload upload = uploadHelper.getUpload(id);

        if (!currentUser.getId().equals(upload.getOwner().getId())) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }

        uploadHelper.removeReferences(id);
        uploadRepository.delete(upload);
        new File(uploadHelper.getPath(true), upload.getFilename()).delete();
        new File(uploadHelper.getPath(false), upload.getFilename()).delete();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity readThumbnail(Long id) throws Exception {
        Upload upload = uploadHelper.getUpload(id);
        byte[] image = uploadHelper.readImage(upload.getFilename(), true);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
