package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class RecipeDto {

    private Long creationTime;
    private BasicUserDto author;
    private Long bannerId;
    private String title;
    private String lead;
    private Long cuisineId;
    private Integer difficulty;
    private Integer plates;
    private Integer preparationTime;
    private Collection<CommentDto> comments;
}
