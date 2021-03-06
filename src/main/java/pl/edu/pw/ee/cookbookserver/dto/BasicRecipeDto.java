package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class BasicRecipeDto {

    private Long id;
    private BasicUserDto author;
    private Long creationTime;
    private Long bannerId;
    private String title;
    private String description;
    private Long commentsCount;
    private Long likesCount;
    private Long favouritesCount;

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
