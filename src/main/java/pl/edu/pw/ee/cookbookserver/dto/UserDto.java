package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String avatar;
    private Long posts;
    private Long followers;
    private Long following;
}
