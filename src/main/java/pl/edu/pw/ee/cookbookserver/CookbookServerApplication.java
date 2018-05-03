package pl.edu.pw.ee.cookbookserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CookbookServerProperties.class)
public class CookbookServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookbookServerApplication.class, args);
    }
}
