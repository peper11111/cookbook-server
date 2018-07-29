package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class RecipeDto {

    private Long creationTime;
    private Long authorId;
    private Long bannerId;
    private String title;
    private String lead;
    private Long cuisineId;
    private Integer difficulty;
    private Integer plates;
    private Integer preparationTime;
}
