package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class BasicRecipeDto {

    private Long id;
    private BasicUserDto author;
    private Long bannerId;
    private String title;
    private Long creationTime;
}
