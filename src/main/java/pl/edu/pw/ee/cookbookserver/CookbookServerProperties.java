package pl.edu.pw.ee.cookbookserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cookbook")
public class CookbookServerProperties {

    private String uploadFolder;
}
