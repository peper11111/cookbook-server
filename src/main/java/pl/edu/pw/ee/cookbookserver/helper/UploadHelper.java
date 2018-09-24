package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.entity.Upload;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;

@Component
public class UploadHelper {

    private UploadRepository uploadRepository;

    @Autowired
    public UploadHelper(UploadRepository uploadRepository) {
        this.uploadRepository = uploadRepository;
    }

    public Upload getUpload(Long id) throws ProcessingException {
        Upload upload = uploadRepository.findById(id).orElse(null);
        if (upload == null) {
            throw new ProcessingException(Error.UPLOAD_NOT_FOUND);
        }
        return upload;
    }
}
