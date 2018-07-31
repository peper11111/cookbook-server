package pl.edu.pw.ee.cookbookserver.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.dto.BasicRecipeDto;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.mapper.RecipeMapper;
import pl.edu.pw.ee.cookbookserver.mapper.UserMapper;

import java.time.ZoneOffset;


@Component
public class RecipeMapperImpl implements RecipeMapper {

    private UserMapper userMapper;

    @Autowired
    public RecipeMapperImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public BasicRecipeDto recipeToBasicRecipeDto(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        BasicRecipeDto basicRecipeDto = new BasicRecipeDto();
        basicRecipeDto.setId(recipe.getId());
        basicRecipeDto.setAuthor(userMapper.userToBasicUserDto(recipe.getAuthor()));
        if (recipe.getBanner() != null) {
            basicRecipeDto.setBannerId(recipe.getBanner().getId());
        }
        basicRecipeDto.setTitle(recipe.getTitle());
        basicRecipeDto.setCreationTime(recipe.getCreationTime().toInstant(ZoneOffset.UTC).toEpochMilli());

        return basicRecipeDto;
    }
}
