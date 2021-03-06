package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecipeDto extends BasicRecipeDto {

    private Long cuisineId;
    private Long categoryId;
    private Integer difficulty;
    private Integer plates;
    private Integer preparationTime;
    private Boolean isLiked;
    private Boolean isFavourite;
    private Collection<String> ingredients;
    private Collection<String> steps;
}
