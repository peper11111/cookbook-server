package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String name;
    private String biography;
    private Long avatarId;
    private Long bannerId;
    private Long recipes;
    private Long followed;
    private Long followers;
    private Boolean following;
}
