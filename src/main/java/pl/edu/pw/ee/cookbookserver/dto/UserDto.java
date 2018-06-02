package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class UserDto {

    private String username;
    private String description;
    private Long avatarId;
    private Long bannerId;
    private Long recipes;
    private Long followed;
    private Long followers;
    private Boolean following;
}
