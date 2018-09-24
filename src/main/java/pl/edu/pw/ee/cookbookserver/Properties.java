package pl.edu.pw.ee.cookbookserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cookbook")
public class Properties {

    private String ddlAuto;
    private String uploadsPath;
    private Long pageSize;
}
