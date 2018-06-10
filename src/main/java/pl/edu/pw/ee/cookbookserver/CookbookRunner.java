package pl.edu.pw.ee.cookbookserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;

@Component
public class CookbookRunner implements ApplicationRunner {

    private String ddlAuto;
    private String uploadFolder;

    @Autowired
    public CookbookRunner(CookbookProperties cookbookProperties) {
        this.ddlAuto = cookbookProperties.getDdlAuto();
        this.uploadFolder = cookbookProperties.getUploadFolder();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File uploads = new File(uploadFolder);

        if (ddlAuto.equals("create") || ddlAuto.equals("create-drop")) {
            FileSystemUtils.deleteRecursively(uploads);
            uploads.mkdirs();
        } else if (ddlAuto.equals("update")) {
            uploads.mkdirs();
        }
    }
}
