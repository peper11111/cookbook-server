package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BasicUserDto {

    private String email;
    private String name;
    private String biography;
    private Long bannerId;
    private Long recipesCount;
    private Long followedCount;
    private Long followersCount;
    private Boolean isFollowed;
}
