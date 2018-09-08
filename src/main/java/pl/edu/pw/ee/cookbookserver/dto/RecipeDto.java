package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecipeDto extends BasicRecipeDto {

    private String lead;
    private Long cuisineId;
    private Integer difficulty;
    private Integer plates;
    private Integer preparationTime;
    private Boolean isLiked;
    private Collection<String> ingredients;
}
