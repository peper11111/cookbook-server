package pl.edu.pw.ee.cookbookserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cookbook")
public class CookbookProperties {

    private String ddlAuto;
    private String uploadFolder;
}
