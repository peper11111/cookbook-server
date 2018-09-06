package pl.edu.pw.ee.cookbookserver.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import pl.edu.pw.ee.cookbookserver.CookbookProperties;

import java.io.File;

@Component
public class UploadRunner implements CommandLineRunner {

    private String ddlAuto;
    private String uploadFolder;

    @Autowired
    public UploadRunner(CookbookProperties cookbookProperties) {
        this.ddlAuto = cookbookProperties.getDdlAuto();
        this.uploadFolder = cookbookProperties.getUploadFolder();
    }

    @Override
    public void run(String... args) throws Exception {
        File uploads = new File(uploadFolder);

        if (ddlAuto.equals("create") || ddlAuto.equals("create-drop")) {
            FileSystemUtils.deleteRecursively(uploads);
            uploads.mkdirs();
        } else if (ddlAuto.equals("update")) {
            uploads.mkdirs();
        }
    }
}
