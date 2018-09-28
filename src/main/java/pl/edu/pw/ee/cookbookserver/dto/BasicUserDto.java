package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class BasicUserDto {

    private Long id;
    private String username;
    private String name;
    private Long avatarId;
}
