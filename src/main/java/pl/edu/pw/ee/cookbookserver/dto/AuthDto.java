package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class AuthDto {

    private String email;
    private String username;
    private String password;
    private String token;
}
