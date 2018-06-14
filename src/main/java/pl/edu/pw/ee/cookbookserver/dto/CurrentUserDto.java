package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;
import pl.edu.pw.ee.cookbookserver.entity.Role;

import java.util.Collection;

@Data
public class CurrentUserDto {

    private Long id;
    private String username;
    private Collection<Role> authorities;
}
