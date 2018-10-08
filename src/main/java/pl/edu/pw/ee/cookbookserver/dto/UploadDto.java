package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class UploadDto {

    private Long id;
    private Long creationTime;

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
