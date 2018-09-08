package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecipeDto extends BasicRecipeDto {

    private String lead;
    private Long cuisineId;
    private Integer difficulty;
    private Integer plates;
    private Integer preparationTime;
    private Boolean isLiked;
}
