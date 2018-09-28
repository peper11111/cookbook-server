package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class CommentDto {

    private Long id;
    private Long creationTime;
    private Long modificationTime;
    private BasicUserDto author;
    private String content;
    private Long commentsCount;

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public void setModificationTime(LocalDateTime modificationTime) {
        this.modificationTime = modificationTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
