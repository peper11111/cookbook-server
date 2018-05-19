package pl.edu.pw.ee.cookbookserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CookbookProperties.class)
public class CookbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookbookApplication.class, args);
    }
}
