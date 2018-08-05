package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class CommentDto {

    private Long id;
    private Long creationTime;
    private Long authorId;
    private String content;
    private Collection<CommentDto> comments;
}
