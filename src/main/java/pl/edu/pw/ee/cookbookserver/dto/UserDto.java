package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class UserDto {

    private String email;
    private String username;
    private String password;
}
