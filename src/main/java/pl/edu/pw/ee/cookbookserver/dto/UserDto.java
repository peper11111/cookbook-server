package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String[] authorities;
}
