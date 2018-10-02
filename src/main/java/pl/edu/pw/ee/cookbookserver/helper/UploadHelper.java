package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.Properties;
import pl.edu.pw.ee.cookbookserver.entity.Upload;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Component
public class UploadHelper {

    private Properties properties;
    private UploadRepository uploadRepository;

    @Autowired
    public UploadHelper(Properties properties, UploadRepository uploadRepository) {
        this.properties = properties;
        this.uploadRepository = uploadRepository;
    }

    public Upload getUpload(Long id) throws ProcessingException {
        Upload upload = uploadRepository.findById(id).orElse(null);
        if (upload == null) {
            throw new ProcessingException(Error.UPLOAD_NOT_FOUND);
        }
        return upload;
    }

    public byte[] readImage(String filename, boolean thumbnail) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedImage bufferedImage = ImageIO.read(new File(getPath(thumbnail), filename));
        ImageIO.write(bufferedImage, "jpg", out);
        return out.toByteArray();
    }

    public void writeImage(String filename, boolean thumbnail, byte[] bytes) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(in);
        bufferedImage = this.removeAlphaChannel(bufferedImage);
        if (thumbnail) {
            bufferedImage = this.cropImage(bufferedImage);
            bufferedImage = this.resizeImage(bufferedImage, 300);
        }
        ImageIO.write(bufferedImage, "jpg", new File(getPath(thumbnail), filename));
    }

    public String getPath(boolean thumbnail) {
        return properties.getUploadsPath() + (thumbnail ? "/thumbnails" : "/images");
    }

    private BufferedImage removeAlphaChannel(BufferedImage originalImage) {
        if (originalImage.getColorModel().hasAlpha()) {
            BufferedImage bufferedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.drawImage(originalImage, 0, 0, Color.WHITE, null);
            graphics2D.dispose();
            return bufferedImage;
        }
        return originalImage;
    }

    private BufferedImage cropImage(BufferedImage originalImage) {
        int size = Math.min(originalImage.getWidth(), originalImage.getHeight());
        int x = (originalImage.getWidth() - size) / 2;
        int y = (originalImage.getHeight() - size) / 2;
        return originalImage.getSubimage(x, y, size, size);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int size) {
        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(originalImage, 0, 0, size, size, Color.WHITE, null);
        graphics2D.dispose();
        return bufferedImage;
    }
}
