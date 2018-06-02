package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class CurrentUserDto {

    private Long id;
    private String[] authorities;
}
