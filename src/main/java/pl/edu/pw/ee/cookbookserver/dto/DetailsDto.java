package pl.edu.pw.ee.cookbookserver.dto;

import lombok.Data;

@Data
public class DetailsDto {

    private String name;
    private String description;
    private Long avatarId;
    private Long bannerId;
    private Long recipes;
    private Long followers;
    private Long following;
}
